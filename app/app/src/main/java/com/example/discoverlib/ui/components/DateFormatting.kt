package com.example.discoverlib.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val appDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun LocalDate.toDisplayDate(): String = format(appDateFormatter)

fun parseAppDateOrNull(value: String): LocalDate? {
    return runCatching { LocalDate.parse(value, appDateFormatter) }.getOrNull()
        ?: runCatching { LocalDate.parse(value) }.getOrNull()
}
