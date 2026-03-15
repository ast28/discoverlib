package com.example.discoverlib.domain

import java.time.LocalDate
import java.time.LocalTime



data class User(
    val username: String,
    val dateOfBirth: LocalDate,
    val darkMode: Boolean,
    val language: String
)

data class Trip(
    val id: String,
    var title: String,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var description: String,
    var budgetEur: Int,
    var activities: MutableList<TripActivity> = mutableListOf()
)

data class TripActivity(
    val id: String,
    var title: String,
    var description: String,
    var location: String,
    var date: LocalDate,   // local date
    var time: LocalTime,   // local time
    var category: ActivityCategory,
    var costEur: Int,
    var photo: Int,
    var photo_maps: Int
)

enum class ActivityCategory {
    CULTURE,
    FOOD,
    NATURE
}


data class TeamMember(
    val initials: String,
    val name: String,
    val role: String
)


data class ValidationResult(
    val isSuccessful: Boolean,
    val message: String
)


