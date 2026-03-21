package com.example.discoverlib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.discoverlib.R
import com.example.discoverlib.domain.Trip
import java.time.LocalDate

@Composable
fun TripFormDialog(
    initialTrip: Trip?,
    existingTrips: List<Trip>,
    onDismiss: () -> Unit,
    onConfirm: (String, LocalDate, LocalDate) -> Unit
) {
    var title by remember { mutableStateOf(initialTrip?.title ?: "") }
    var titleTouched by remember { mutableStateOf(false) }

    var startText by remember { mutableStateOf(initialTrip?.startDate?.toString() ?: LocalDate.now().toString()) }
    var endText by remember { mutableStateOf(initialTrip?.endDate?.toString() ?: LocalDate.now().plusDays(3).toString()) }

    val startDate = try { LocalDate.parse(startText) } catch (e: Exception) { null }
    val endDate = try { LocalDate.parse(endText) } catch (e: Exception) { null }

    val isTitleInvalid = title.isBlank()
    val isTitleError = isTitleInvalid && titleTouched

    val isDateParseError = startDate == null || endDate == null

    val today = LocalDate.now()
    val isPastDateError = startDate != null && startDate.isBefore(today) && startDate != initialTrip?.startDate

    val isOrderError = startDate != null && endDate != null && endDate.isBefore(startDate)

    val earliestActivity = initialTrip?.activities?.minOfOrNull { it.date }
    val latestActivity = initialTrip?.activities?.maxOfOrNull { it.date }

    val activityConflict = if (initialTrip != null && initialTrip.activities.isNotEmpty() && !isDateParseError) {
        val startConflict = earliestActivity != null && startDate!!.isAfter(earliestActivity)
        val endConflict = latestActivity != null && endDate!!.isBefore(latestActivity)
        startConflict || endConflict
    } else false

    val isDuplicateError = if (startDate != null && endDate != null && title.isNotBlank()) {
        existingTrips.any {
            it.title.trim().equals(title.trim(), ignoreCase = true) &&
                    it.startDate == startDate &&
                    it.endDate == endDate &&
                    it.id != initialTrip?.id
        }
    } else false

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (initialTrip == null) stringResource(id = R.string.form_trip_new_title)
                else stringResource(id = R.string.form_trip_edit_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(id = R.string.form_trip_city)) },
                    isError = isTitleError || isDuplicateError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) titleTouched = true
                        }
                )

                if (isTitleError) {
                    Text(stringResource(id = R.string.form_trip_title_empty), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }

                DatePickerField(
                    label = "Start",
                    selectedDate = startText,
                    onDateSelected = { startText = it },
                    isError = isDateParseError || isPastDateError || isOrderError || isDuplicateError,
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerField(
                    label = "End",
                    selectedDate = endText,
                    onDateSelected = { endText = it },
                    isError = isDateParseError || isOrderError || isDuplicateError,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isPastDateError) {
                    Text(stringResource(id = R.string.form_trip_past_error), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }

                if (isOrderError) {
                    Text(stringResource(id = R.string.form_trip_date_error), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }

                if (isDuplicateError) {
                    Text(
                        text = stringResource(id = R.string.form_trip_duplicate_error),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }

                if (activityConflict) {
                    Text(
                        text = stringResource(id = R.string.form_trip_conflict_error),
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (startDate != null && endDate != null) onConfirm(title, startDate, endDate) },
                enabled = !isTitleInvalid && !isDateParseError && !activityConflict && !isPastDateError && !isDuplicateError && !isOrderError,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.form_trip_save_btn), color = Color.White) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.dialog_cancel_btn), fontWeight = FontWeight.Bold) }
        }
    )
}
