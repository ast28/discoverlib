package com.example.discoverlib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    onDismiss: () -> Unit,
    onConfirm: (String, LocalDate, LocalDate) -> Unit
) {
    var title by remember { mutableStateOf(initialTrip?.title ?: "") }
    var startText by remember { mutableStateOf(initialTrip?.startDate?.toString() ?: LocalDate.now().toString()) }
    var endText by remember { mutableStateOf(initialTrip?.endDate?.toString() ?: LocalDate.now().plusDays(3).toString()) }

    val startDate = try { LocalDate.parse(startText) } catch (e: Exception) { null }
    val endDate = try { LocalDate.parse(endText) } catch (e: Exception) { null }

    val isTitleError = title.isBlank()
    val isDateParseError = startDate == null || endDate == null
    val isDateRangeError = if (!isDateParseError) endDate!!.isBefore(startDate) else false

    val today = LocalDate.now()
    val isPastDateError = startDate != null && startDate.isBefore(today) && startDate != initialTrip?.startDate

    val earliestActivity = initialTrip?.activities?.minOfOrNull { it.date }
    val latestActivity = initialTrip?.activities?.maxOfOrNull { it.date }

    val activityConflict = if (initialTrip != null && initialTrip.activities.isNotEmpty() && !isDateParseError) {
        val startConflict = earliestActivity != null && startDate!!.isAfter(earliestActivity)
        val endConflict = latestActivity != null && endDate!!.isBefore(latestActivity)
        startConflict || endConflict
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
                    isError = isTitleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerField(
                    label = stringResource(id = R.string.form_trip_start_date),
                    selectedDate = startText,
                    onDateSelected = { startText = it },
                    isError = isDateParseError || isPastDateError,
                    modifier = Modifier.fillMaxWidth()
                )

                DatePickerField(
                    label = stringResource(id = R.string.form_trip_end_date),
                    selectedDate = endText,
                    onDateSelected = { endText = it },
                    isError = isDateParseError || isDateRangeError,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isPastDateError) {
                    Text(stringResource(id = R.string.form_trip_past_error), color = Color.Red, fontSize = 12.sp)
                }

                if (isDateRangeError) {
                    Text(stringResource(id = R.string.form_trip_date_error), color = Color.Red, fontSize = 12.sp)
                }

                if (activityConflict) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.form_trip_conflict_error),
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (startDate != null && endDate != null) onConfirm(title, startDate, endDate) },
                enabled = !isTitleError && !isDateParseError && !isDateRangeError && !activityConflict && !isPastDateError,
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