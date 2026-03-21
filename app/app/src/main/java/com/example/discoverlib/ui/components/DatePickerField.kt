package com.example.discoverlib.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    val calendarState = rememberUseCaseState()

    val parsedDate = remember(selectedDate) {
        try { LocalDate.parse(selectedDate) } catch (e: Exception) { LocalDate.now() }
    }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            boundary = LocalDate.of(1900, 1, 1)..LocalDate.of(2100, 12, 31)
        ),
        selection = CalendarSelection.Date(
            selectedDate = parsedDate
        ) { newDate ->
            onDateSelected(newDate.toString())
        }
    )

    Box(modifier = modifier.clickable { calendarState.show() }) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            isError = isError,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { calendarState.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.matchParentSize().clickable { calendarState.show() })
    }
}