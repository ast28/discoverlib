package com.example.discoverlib.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TripActivity
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormDialog(
    initialActivity: TripActivity? = null,
    tripStartDate: LocalDate,
    tripEndDate: LocalDate,
    existingActivities: List<TripActivity>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, location: String, description: String, date: LocalDate, time: LocalTime, costEur: Int, category: ActivityCategory) -> Unit
) {
    var title by remember { mutableStateOf(initialActivity?.title ?: "") }
    var location by remember { mutableStateOf(initialActivity?.location ?: "") }
    var description by remember { mutableStateOf(initialActivity?.description ?: "") }
    var cost by remember { mutableStateOf(initialActivity?.costEur?.toString() ?: "") }

    var titleTouched by remember { mutableStateOf(false) }
    var locationTouched by remember { mutableStateOf(false) }
    var costTouched by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(initialActivity?.category ?: ActivityCategory.CULTURE) }

    var showTimePicker by remember { mutableStateOf(false) }

    val categoryOptions = listOf(
        ActivityCategory.TRANSPORT to stringResource(id = R.string.cat_transport),
        ActivityCategory.TOURS to stringResource(id = R.string.cat_tours),
        ActivityCategory.CULTURE to stringResource(id = R.string.cat_culture),
        ActivityCategory.FOOD to stringResource(id = R.string.cat_food),
        ActivityCategory.NATURE to stringResource(id = R.string.cat_nature)
    )

    var dateText by remember { mutableStateOf(initialActivity?.date?.toString() ?: tripStartDate.toString()) }
    var timeText by remember {
        mutableStateOf(
            initialActivity?.time?.let { String.format(Locale.getDefault(), "%02d:%02d", it.hour, it.minute) } ?: "10:00"
        )
    }

    val parsedDate = try { LocalDate.parse(dateText) } catch (e: Exception) { null }
    val parsedTime = try { LocalTime.parse(timeText) } catch (e: Exception) { null }

    val isTitleInvalid = title.isBlank()
    val isCostInvalid = cost.toIntOrNull() == null
    val isLocationInvalid = location.isBlank()
    val isDateError = parsedDate == null || parsedDate.isBefore(tripStartDate) || parsedDate.isAfter(tripEndDate)
    val isTimeError = parsedTime == null

    val isTitleError = isTitleInvalid && titleTouched
    val isLocationError = isLocationInvalid && locationTouched
    val isCostError = isCostInvalid && costTouched

    val isConflictError = if (parsedDate != null && parsedTime != null) {
        existingActivities.any { it.date == parsedDate && it.time == parsedTime && it.id != initialActivity?.id }
    } else false

    val isFormValid = !isTitleInvalid && !isCostInvalid && !isLocationInvalid && !isDateError && !isTimeError && !isConflictError

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialActivity == null) stringResource(id = R.string.form_new_custom) else "Edit Activity",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(id = R.string.form_title)) },
                    isError = isTitleError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) titleTouched = true
                        }
                )
                if (isTitleError) {
                    Text(stringResource(id = R.string.form_error_title_empty), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(stringResource(id = R.string.form_location)) },
                    isError = isLocationError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) locationTouched = true
                        }
                )
                if (isLocationError) {
                    Text(stringResource(id = R.string.form_error_location_empty), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = categoryOptions.find { it.first == selectedCategory }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.form_category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoryOptions.forEach { (category, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(id = R.string.form_description)) },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DatePickerField(
                        label = stringResource(id = R.string.form_date),
                        selectedDate = dateText,
                        onDateSelected = { dateText = it },
                        isError = isDateError || isConflictError,
                        modifier = Modifier.weight(1.4f)
                    )

                    OutlinedTextField(
                        value = timeText,
                        onValueChange = { },
                        label = { Text(stringResource(id = R.string.form_time)) },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = "Seleccionar Hora")
                        },
                        isError = isTimeError || isConflictError,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = if (isTimeError || isConflictError) Color.Red else MaterialTheme.colorScheme.outline,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = if (isTimeError || isConflictError) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTimePicker = true }
                    )
                }

                if (showTimePicker) {
                    TimePickerDialog(
                        initialTime = parsedTime ?: LocalTime.of(10, 0),
                        onDismissRequest = { showTimePicker = false },
                        onTimeSelected = { selectedTime ->
                            timeText = String.format(Locale.getDefault(), "%02d:%02d", selectedTime.hour, selectedTime.minute)
                            showTimePicker = false
                        }
                    )
                }

                if (isDateError && parsedDate != null) {
                    Text(
                        stringResource(id = R.string.form_date_error, tripStartDate.toString(), tripEndDate.toString()),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                if (isConflictError) {
                    Text(
                        text = stringResource(id = R.string.form_conflict_error),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text(stringResource(id = R.string.form_cost)) },
                    isError = isCostError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) costTouched = true
                        }
                )
                if (isCostError) {
                    Text(stringResource(id = R.string.form_error_cost_empty), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onConfirm(title, location, description, parsedDate!!, parsedTime!!, cost.toIntOrNull() ?: 0, selectedCategory)
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.form_save), color = Color.White) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.form_cancel), fontWeight = FontWeight.Bold) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    onDismissRequest: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
            }) {
                Text(stringResource(id = R.string.form_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(id = R.string.form_cancel))
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}