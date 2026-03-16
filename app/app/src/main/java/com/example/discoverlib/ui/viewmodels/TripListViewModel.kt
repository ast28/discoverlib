package com.example.discoverlib.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.domain.MockActivity
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

    val trips: StateFlow<List<Trip>> = repository.getTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getTeam(): List<TeamMember> {
        return repository.getTeam()
    }

    fun getSuggestedActivities(): List<MockActivity> {
        return repository.getSuggestedActivities()
    }




    fun getSavedOneTrip(tripId: String): Trip? {
        return repository.getOneTrip(tripId)
    }

    fun saveNewTrip(trip: Trip): ValidationResult{
        if (trip.title.isBlank() || trip.title.length > 30 ) {
            return ValidationResult(false, "Title max 30")
        }

        if (trip.startDate.isBefore(LocalDate.now()) || trip.startDate.isAfter(trip.endDate)) {
            return ValidationResult(false, "Check dates: cannot start in the past or end before starting.")
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





    fun getSavedActivity(tripId: String, activityId: String): TripActivity? {
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
        val trip = repository.getOneTrip(tripId)
        if (trip != null) {
            if (activity.date.isBefore(trip.startDate) || activity.date.isAfter(trip.endDate)) {
                return ValidationResult(false, "Activity date must be during the trip dates.")
            }
        }
        val exists = trip?.activities?.any { it.title.lowercase() == activity.title.trim().lowercase() } ?: false
        if (exists) {
            return ValidationResult(false, "This activity already exists in your plan.")
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
}
