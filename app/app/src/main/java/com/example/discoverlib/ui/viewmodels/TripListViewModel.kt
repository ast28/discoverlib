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
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val TAG = "TripViewModel"

    fun getTeam(): List<TeamMember> {
        Log.d(TAG, "Fetching team information")
        return repository.getTeam()
    }

    val trips: StateFlow<List<Trip>> = repository.getTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getOneTrip(tripId: String, onResult: (Trip?) -> Unit) {
        Log.d(TAG, "Request to get trip details: $tripId")
        viewModelScope.launch {
            try {
                val trip = repository.getOneTrip(tripId)
                if (trip != null) {
                    Log.i(TAG, "Trip $tripId successfully fetched from DB")
                } else {
                    Log.i(TAG, "Trip $tripId not found in DB")
                }
                onResult(trip)
            } catch (e: Exception) {
                Log.e(TAG, "Error in getOneTrip: ${e.message}")
                onResult(null)
            }
        }
    }

    fun addTrip(trip: Trip, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to add new trip: '${trip.title}'")

        if (trip.title.isBlank() || trip.title.length > 30) {
            Log.e(TAG, "Validation failed: Title is empty or exceeds 30 characters")
            onResult(ValidationResult(false, "Title max 30"))
            return
        }

        if (trip.startDate.isBefore(LocalDate.now()) || trip.startDate.isAfter(trip.endDate)) {
            Log.e(TAG, "Validation failed: Incorrect dates (in the past or start date after end date)")
            onResult(ValidationResult(false, "Check dates: cannot start in the past or end before starting."))
            return
        }

        val tripWithId = trip.copy(id = java.util.UUID.randomUUID().toString())

        viewModelScope.launch {
            try {
                repository.addTrip(tripWithId)
                Log.i(TAG, "Trip '${tripWithId.title}' (ID: ${tripWithId.id}) successfully saved in Room")
                onResult(ValidationResult(true, "Trip saved successfully!"))
            } catch (e: Exception) {
                Log.e(TAG, "Error in addTrip: ${e.message}")
                onResult(ValidationResult(false, "Internal error saving the trip."))
            }
        }
    }

    fun updateTrip(trip: Trip, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to update trip: '${trip.id}'")

        if (trip.title.isBlank() || trip.startDate.isAfter(trip.endDate)) {
            Log.e(TAG, "Validation failed: Empty title or start date after end date")
            onResult(ValidationResult(false, "Invalid data. Please check the title and dates."))
            return
        }

        viewModelScope.launch {
            try {
                repository.updateTrip(trip)
                Log.i(TAG, "Trip '${trip.id}' successfully updated in Room")
                onResult(ValidationResult(true, "Trip updated successfully!"))
            } catch (e: Exception) {
                Log.e(TAG, "Error in updateTrip: ${e.message}")
                onResult(ValidationResult(false, "Internal error updating the trip."))
            }
        }
    }

    fun deleteTrip(tripId: String, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "Request to delete trip with ID: $tripId")
        viewModelScope.launch {
            try {
                repository.deleteTrip(tripId)
                Log.i(TAG, "Trip $tripId successfully deleted from Room")
                onResult(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error in deleteTrip: ${e.message}")
                onResult(false)
            }
        }
    }


    fun getOneActivity(activityId: String, onResult: (TripActivity?) -> Unit) {
        Log.d(TAG, "Request to fetch activity: $activityId")
        viewModelScope.launch {
            try {
                val activity = repository.getOneActivity(activityId)
                if (activity != null) {
                    Log.i(TAG, "Activity $activityId successfully fetched")
                } else {
                    Log.i(TAG, "Activity $activityId not found")
                }
                onResult(activity)
            } catch (e: Exception) {
                Log.e(TAG, "Error in getOneActivity: ${e.message}")
                onResult(null)
            }
        }
    }

    fun addActivity(tripId: String, activity: TripActivity, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to add activity '${activity.title}' to trip $tripId")

        if (activity.title.isBlank() || activity.description.isBlank() || activity.location.isBlank()) {
            Log.e(TAG, "Validation failed: Missing mandatory fields in the activity")
            onResult(ValidationResult(false, "Invalid data. All fields are mandatory."))
            return
        }

        viewModelScope.launch {
            try {
                val trip = repository.getOneTrip(tripId)
                if (trip != null) {
                    if (activity.date.isBefore(trip.startDate) || activity.date.isAfter(trip.endDate)) {
                        Log.e(TAG, "Validation failed: Activity date is outside the trip dates")
                        onResult(ValidationResult(false, "Activity date must be during the trip dates."))
                        return@launch
                    }

                    val existsTitle = trip.activities.any { it.title.trim().equals(activity.title.trim(), ignoreCase = true) }
                    if (existsTitle) {
                        Log.e(TAG, "Validation failed: An activity with the title '${activity.title}' already exists")
                        onResult(ValidationResult(false, "This activity already exists in your plan."))
                        return@launch
                    }

                    val timeConflict = trip.activities.any { it.date == activity.date && it.time == activity.time }
                    if (timeConflict) {
                        Log.e(TAG, "Validation failed: Schedule conflict (there is already an activity at this date and time)")
                        onResult(ValidationResult(false, "You already have an activity at this same date and time."))
                        return@launch
                    }
                }

                val activityWithId = activity.copy(id = java.util.UUID.randomUUID().toString())
                repository.addActivity(tripId, activityWithId)
                Log.i(TAG, "Activity '${activityWithId.title}' successfully added to trip $tripId in Room")
                onResult(ValidationResult(true, "Activity added successfully!"))

            } catch (e: Exception) {
                Log.e(TAG, "Error in addActivity: ${e.message}")
                onResult(ValidationResult(false, "Internal error adding activity."))
            }
        }
    }

    fun updateActivity(tripId: String, activity: TripActivity, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to update activity '${activity.id}' for trip $tripId")

        if (activity.title.isBlank() || activity.description.isBlank() || activity.location.isBlank()) {
            Log.e(TAG, "Validation failed on update: Missing mandatory fields")
            onResult(ValidationResult(false, "Invalid data. All fields must be completed."))
            return
        }

        viewModelScope.launch {
            try {
                val trip = repository.getOneTrip(tripId)
                if (trip != null) {
                    val timeConflict = trip.activities.any {
                        it.id != activity.id && it.date == activity.date && it.time == activity.time
                    }
                    if (timeConflict) {
                        Log.e(TAG, "Validation failed on update: Schedule conflict with another activity")
                        onResult(ValidationResult(false, "You already have another activity at this same date and time."))
                        return@launch
                    }
                }

                repository.updateActivity(tripId, activity)
                Log.i(TAG, "Activity '${activity.id}' successfully updated in Room")
                onResult(ValidationResult(true, "Activity updated successfully!"))

            } catch (e: Exception) {
                Log.e(TAG, "Error in updateActivity: ${e.message}")
                onResult(ValidationResult(false, "Internal error updating activity."))
            }
        }
    }

    fun deleteOneActivity(tripId: String, activityId: String, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Request to delete activity $activityId from trip $tripId")
        viewModelScope.launch {
            try {
                repository.deleteOneActivity(tripId, activityId)
                Log.i(TAG, "Activity $activityId successfully deleted from Room")
                onResult(ValidationResult(true, "Activity deleted successfully!"))
            } catch (e: Exception) {
                Log.e(TAG, "Error in deleteOneActivity: ${e.message}")
                onResult(ValidationResult(false, "Internal error: Could not delete activity."))
            }
        }
    }
}