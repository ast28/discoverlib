package com.example.discoverlib.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.User
import com.example.discoverlib.domain.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    fun getSavedTrips(): List<Trip> {
        return repository.getTrips()
    }

    fun getSavedOneTrip(tripId: String): Trip? {
        return repository.getOneTrip(tripId)
    }

    fun saveNewTrip(trip: Trip): ValidationResult{
        if (trip.title.isBlank() || trip.startDate.isAfter(trip.endDate)) {
            return ValidationResult(
                isSuccessful = false,
                message = "Invalid data. Please check the title and dates."
            )
        }

        val tripWithId = trip.copy(id = java.util.UUID.randomUUID().toString())
        val isSaved = repository.addTrip(tripWithId)

        return if (isSaved) {
            ValidationResult(true, "Trip saved successfully!")
        } else {
            ValidationResult(false, "Internal error saving the trip.")
        }
    }

    fun saveEditedTrip(trip: Trip): ValidationResult {
        if (trip.title.isBlank() || trip.startDate.isAfter(trip.endDate)) {
            return ValidationResult(
                isSuccessful = false,
                message = "Invalid data. Please check the title and dates."
            )
        }

        val isUpdated = repository.editTrip(trip)

        return if (isUpdated) {
            ValidationResult(true, "Trip updated successfully!")
        } else {
            ValidationResult(false, "Internal error: Trip not found.")
        }
    }

    fun saveDeletedTrip(tripId: String): Boolean {
        return repository.deleteTrip(tripId)
    }




    fun getSavedAtivities(trip: Trip): List<TripActivity> {
        return repository.getActivities(trip)
    }

    fun getSavedAtivity(tripId: String, activityId: String): TripActivity? {
        return repository.getOneActivity(tripId, activityId)
    }

    fun addActivity(tripId: String, activity: TripActivity): ValidationResult {
        if (activity.title.isBlank() || activity.description.isBlank() ||
            activity.location.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                message = "Invalid data. All fields are mandatory."
            )
        }

        val activityWithId = activity.copy(id = java.util.UUID.randomUUID().toString())
        val isAdded = repository.addActivity(tripId, activityWithId)

        return if (isAdded) {
            ValidationResult(true, "Activity added successfully!")
        } else {
            ValidationResult(false, "Internal error: Trip not found.")
        }
    }

    fun editActivity(tripId: String, activity: TripActivity): ValidationResult {
        if (activity.title.isBlank() || activity.description.isBlank() ||
            activity.location.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                message = "Invalid data. All fields must be completed."
            )
        }

        val isEdited = repository.editActivity(tripId, activity)

        return if (isEdited) {
            ValidationResult(true, "Activity updated successfully!")
        } else {
            ValidationResult(false, "Internal error: Activity or Trip not found.")
        }
    }

    fun deleteActivity(tripId: String, activityId: String): ValidationResult {
        val isDeleted = repository.deleteActivity(tripId, activityId)

        return if (isDeleted) {
            ValidationResult(true, "Activity deleted successfully!")
        } else {
            ValidationResult(false, "Internal error: Could not delete activity.")
        }
    }





    fun getSavedUsername(): String {
        val user = repository.getUser()
        if (user != null) { return user.username }
        else { return "Not defined user name" }
    }

    fun saveNewUsername(newName: String): Boolean {
        if (newName.isBlank()) {
            return false
        }
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(username = newName)
        } else {
            User(
                username = newName,
                dateOfBirth = LocalDate.now(),
                darkMode = false,
                language = "en"
            )
        }
        repository.saveUser(updatedUser)
        return true
    }

    fun getSavedDateOfBirth(): String {
        val user = repository.getUser()
        if (user != null) { return user.dateOfBirth.toString() }
        else { return "Not defined date of birth" }
    }

    fun saveNewDateOfBirth(newDate: LocalDate): Boolean {
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(dateOfBirth = newDate)
        } else {
            User(
                username = "",
                dateOfBirth = newDate,
                darkMode = false,
                language = "en"
            )
        }
        repository.saveUser(updatedUser)
        return true
    }

    fun getDarkMode(): Boolean {
        val user = repository.getUser()
        return user?.darkMode ?: false
    }

    fun saveDarkMode(isDark: Boolean) {
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(darkMode = isDark)
        } else {
            User(
                username = "",
                dateOfBirth = LocalDate.now(),
                darkMode = isDark,
                language = "en"
            )
        }
        repository.saveUser(updatedUser)
    }

    fun getLanguage(): String {
        val user = repository.getUser()
        return user?.language ?: "en"
    }

    fun saveLanguage(newLanguage: String) {
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(language = newLanguage)
        } else {
            User(
                username = "",
                dateOfBirth = LocalDate.now(),
                darkMode = false,
                language = newLanguage
            )
        }
        repository.saveUser(updatedUser)
    }
}