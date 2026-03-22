package com.example.discoverlib.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val TAG = "TripViewModel"

    val trips: StateFlow<List<Trip>> = repository.getTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getTeam(): List<TeamMember> = repository.getTeam()
    fun getSavedActivity(tripId: String, activityId: String): TripActivity? = repository.getOneActivity(tripId, activityId)

    fun saveNewTrip(trip: Trip): ValidationResult {
        if (trip.title.isBlank() || trip.title.length > 30) {
            Log.e(TAG, "Error saving trip: Title is blank or exceeds 30 characters.")
            return ValidationResult(false, "Title max 30")
        }

        if (trip.startDate.isBefore(LocalDate.now()) || trip.startDate.isAfter(trip.endDate)) {
            Log.e(TAG, "Error saving trip: Invalid dates.")
            return ValidationResult(false, "Check dates: cannot start in the past or end before starting.")
        }

        val tripWithId = trip.copy(id = java.util.UUID.randomUUID().toString())
        val isSaved = repository.addTrip(tripWithId)

        return if (isSaved) {
            Log.i(TAG, "Success: Trip '${trip.title}' saved.")
            ValidationResult(true, "Trip saved successfully!")
        } else {
            Log.e(TAG, "Error: Repository failed to save the trip.")
            ValidationResult(false, "Internal error saving the trip.")
        }
    }

    fun saveEditedTrip(trip: Trip): ValidationResult {
        if (trip.title.isBlank() || trip.startDate.isAfter(trip.endDate)) {
            Log.e(TAG, "Error editing trip: Invalid title or dates.")
            return ValidationResult(false, "Invalid data. Please check the title and dates.")
        }

        val isUpdated = repository.editTrip(trip)

        return if (isUpdated) {
            Log.i(TAG, "Success: Trip updated.")
            ValidationResult(true, "Trip updated successfully!")
        } else {
            Log.e(TAG, "Error editing trip: Trip not found.")
            ValidationResult(false, "Internal error: Trip not found.")
        }
    }

    fun saveDeletedTrip(tripId: String): Boolean {
        val deleted = repository.deleteTrip(tripId)
        if (deleted) Log.i(TAG, "Success: Trip deleted.")
        else Log.e(TAG, "Error deleting trip $tripId.")
        return deleted
    }

    fun addActivity(tripId: String, activity: TripActivity): ValidationResult {
        if (activity.title.isBlank() || activity.description.isBlank() || activity.location.isBlank()) {
            Log.e(TAG, "Error adding activity: Blank fields.")
            return ValidationResult(false, "Invalid data. All fields are mandatory.")
        }

        val trip = repository.getOneTrip(tripId)
        if (trip != null) {
            if (activity.date.isBefore(trip.startDate) || activity.date.isAfter(trip.endDate)) {
                Log.e(TAG, "Error: Activity date outside trip range.")
                return ValidationResult(false, "Activity date must be during the trip dates.")
            }

            val existsTitle = trip.activities.any { it.title.trim().equals(activity.title.trim(), ignoreCase = true) }
            if (existsTitle) {
                Log.e(TAG, "Error: Title already exists in this trip.")
                return ValidationResult(false, "This activity already exists in your plan.")
            }

            val timeConflict = trip.activities.any { it.date == activity.date && it.time == activity.time }
            if (timeConflict) {
                Log.e(TAG, "Error: Time conflict. Another activity exists at the same date and time.")
                return ValidationResult(false, "You already have an activity at this same date and time.")
            }
        }

        val activityWithId = activity.copy(id = java.util.UUID.randomUUID().toString())
        val isAdded = repository.addActivity(tripId, activityWithId)

        return if (isAdded) {
            Log.i(TAG, "Success: Activity added.")
            ValidationResult(true, "Activity added successfully!")
        } else {
            Log.e(TAG, "Error: Trip not found.")
            ValidationResult(false, "Internal error: Trip not found.")
        }
    }

    fun editActivity(tripId: String, activity: TripActivity): ValidationResult {
        if (activity.title.isBlank() || activity.description.isBlank() || activity.location.isBlank()) {
            Log.e(TAG, "Error editing activity: Blank fields.")
            return ValidationResult(false, "Invalid data. All fields must be completed.")
        }

        val trip = repository.getOneTrip(tripId)
        if (trip != null) {
            val timeConflict = trip.activities.any {
                it.id != activity.id && it.date == activity.date && it.time == activity.time
            }
            if (timeConflict) {
                Log.e(TAG, "Error editing activity: Time conflict.")
                return ValidationResult(false, "You already have another activity at this same date and time.")
            }
        }

        val isEdited = repository.editActivity(tripId, activity)

        return if (isEdited) {
            Log.i(TAG, "Success: Activity updated.")
            ValidationResult(true, "Activity updated successfully!")
        } else {
            Log.e(TAG, "Error editing activity: Not found.")
            ValidationResult(false, "Internal error: Activity or Trip not found.")
        }
    }

    fun deleteActivity(tripId: String, activityId: String): ValidationResult {
        val isDeleted = repository.deleteActivity(tripId, activityId)
        return if (isDeleted) {
            Log.i(TAG, "Success: Activity deleted.")
            ValidationResult(true, "Activity deleted successfully!")
        } else {
            Log.e(TAG, "Error deleting activity.")
            ValidationResult(false, "Internal error: Could not delete activity.")
        }
    }
}
