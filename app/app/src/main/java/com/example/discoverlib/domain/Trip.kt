package com.example.discoverlib.domain

import java.time.LocalDate
import java.time.LocalTime



data class User(
    val id: String,
    val username: String,
    val dateOfBirth: LocalDate,
    val darkMode: Boolean,
    val language: String,
    val email: String,
    val address: String,
    val country: String,
    val phoneNumber: String,
    val acceptReceiveEmails: Boolean
)

data class Trip(
    val id: String,
    val userId: String,
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
    var date: LocalDate,
    var time: LocalTime,
    var category: ActivityCategory,
    var costEur: Int,
    var photo: Int,
    var photo_maps: Int
)

data class MockActivity(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val category: String,
    val priceEur: Int
)

enum class ActivityCategory {
    TRANSPORT,
    TOURS,
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


