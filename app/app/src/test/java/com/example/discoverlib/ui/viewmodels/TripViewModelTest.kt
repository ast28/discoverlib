/*package com.example.discoverlib.ui.viewmodels

import com.example.discoverlib.data.SharedPrefsManager
import com.example.discoverlib.data.repository.TripRepositoryImpl
import com.example.discoverlib.domain.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TripViewModelTest {

    private lateinit var viewModel: TripViewModel
    private lateinit var repository: TripRepositoryImpl

    @Before
    fun setup() {
        val mockSharedPrefs = mock(SharedPrefsManager::class.java)

        repository = TripRepositoryImpl(mockSharedPrefs)
        viewModel = TripViewModel(repository)
    }



    @Test
    fun saveNewTrip() {
        val goodTrip = Trip("1", "Paris", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        val result = viewModel.saveNewTrip(goodTrip)

        assertTrue(result.isSuccessful)
        assertEquals("Trip saved successfully!", result.message)
    }

    @Test
    fun saveNewTripTitleBlank() {
        val badTrip = Trip("1", "   ", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        val result = viewModel.saveNewTrip(badTrip)

        assertFalse(result.isSuccessful)
        assertEquals("Title max 30", result.message)
    }

    @Test
    fun saveNewTripTitleExceeds30Characters() {
        val longTitle = "A".repeat(31)
        val badTrip = Trip("1", longTitle, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        val result = viewModel.saveNewTrip(badTrip)

        assertFalse(result.isSuccessful)
        assertEquals("Title max 30", result.message)
    }

    @Test
    fun saveNewTripStartDateInThePast() {
        val badTrip = Trip("1", "Paris", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        val result = viewModel.saveNewTrip(badTrip)

        assertFalse(result.isSuccessful)
        assertEquals("Check dates: cannot start in the past or end before starting.", result.message)
    }

    @Test
    fun saveNewTripStartDateAfterEndDate() {
        val badTrip = Trip("1", "Paris", LocalDate.now().plusDays(5), LocalDate.now().plusDays(2), "", 0, mutableListOf())
        val result = viewModel.saveNewTrip(badTrip)

        assertFalse(result.isSuccessful)
        assertEquals("Check dates: cannot start in the past or end before starting.", result.message)
    }

    @Test
    fun editTripSuccess() {
        val tripId = UUID.randomUUID().toString()
        val trip = Trip(tripId, "Rome", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        repository.addTrip(trip)

        val editedTrip = trip.copy(title = "Rome Updated")
        val result = viewModel.saveEditedTrip(editedTrip)

        assertTrue(result.isSuccessful)
        assertEquals("Trip updated successfully!", result.message)
    }

    @Test
    fun editTripFailsInvalidData() {
        // Titulo vacío y fechas del reves
        val badTrip = Trip("1", "", LocalDate.now().plusDays(5), LocalDate.now().plusDays(2), "", 0, mutableListOf())
        val result = viewModel.saveEditedTrip(badTrip)

        assertFalse(result.isSuccessful)
        assertEquals("Invalid data. Please check the title and dates.", result.message)
    }

    @Test
    fun deleteTripSuccess() {
        val tripId = UUID.randomUUID().toString()
        val trip = Trip(tripId, "London", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        repository.addTrip(trip)

        val result = viewModel.saveDeletedTrip(tripId)
        assertTrue(result)
    }




    @Test
    fun addActivitySuccess() {
        val tripId = UUID.randomUUID().toString()
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        repository.addTrip(trip)

        val goodActivity = TripActivity("1", "Museum", "Desc", "Loc", LocalDate.now().plusDays(2), LocalTime.of(10,0), ActivityCategory.CULTURE, 0, 0, 0)
        val result = viewModel.addActivity(tripId, goodActivity)

        assertTrue(result.isSuccessful)
        assertEquals("Activity added successfully!", result.message)
    }

    @Test
    fun addActivityFieldsBlank() {
        val tripId = UUID.randomUUID().toString()
        val badActivity = TripActivity("1", " ", " ", " ", LocalDate.now(), LocalTime.now(), ActivityCategory.FOOD, 0, 0, 0)

        val result = viewModel.addActivity(tripId, badActivity)

        assertFalse(result.isSuccessful)
        assertEquals("Invalid data. All fields are mandatory.", result.message)
    }

    @Test
    fun addActivityIncorrectDate() {
        val tripId = UUID.randomUUID().toString()
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf())
        repository.addTrip(trip)

        val badActivity = TripActivity("1", "Museum", "Desc", "Loc", LocalDate.now(), LocalTime.now(), ActivityCategory.CULTURE, 0, 0, 0)

        val result = viewModel.addActivity(tripId, badActivity)

        assertFalse(result.isSuccessful)
        assertEquals("Activity date must be during the trip dates.", result.message)
    }

    @Test
    fun addActivityTitleAlreadyExists() {
        val tripId = UUID.randomUUID().toString()
        val existingActivity = TripActivity("1", "Museum", "Desc", "Loc", LocalDate.now().plusDays(2), LocalTime.now(), ActivityCategory.CULTURE, 0, 0, 0)
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf(existingActivity))
        repository.addTrip(trip)

        val badActivity = TripActivity("2", "museum", "Other", "Other", LocalDate.now().plusDays(3), LocalTime.now(), ActivityCategory.CULTURE, 0, 0, 0)

        val result = viewModel.addActivity(tripId, badActivity)

        assertFalse(result.isSuccessful)
        assertEquals("This activity already exists in your plan.", result.message)
    }

    @Test
    fun addActivityExistingActivity() {
        val tripId = UUID.randomUUID().toString()
        val conflictDate = LocalDate.now().plusDays(2)
        val conflictTime = LocalTime.of(10, 0)

        val existingActivity = TripActivity("1", "Museum", "Desc", "Loc", conflictDate, conflictTime, ActivityCategory.CULTURE, 0, 0, 0)
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf(existingActivity))
        repository.addTrip(trip)

        val badActivity = TripActivity("2", "Lunch", "Food", "Restaurant", conflictDate, conflictTime, ActivityCategory.FOOD, 0, 0, 0)

        val result = viewModel.addActivity(tripId, badActivity)

        assertFalse(result.isSuccessful)
        assertEquals("You already have an activity at this same date and time.", result.message)
    }

    @Test
    fun editActivitySuccess() {
        val tripId = UUID.randomUUID().toString()
        val activity = TripActivity("1", "Museum", "Desc", "Loc", LocalDate.now().plusDays(2), LocalTime.of(10,0), ActivityCategory.CULTURE, 0, 0, 0)
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf(activity))
        repository.addTrip(trip)

        val editedActivity = activity.copy(title = "Art Museum")
        val result = viewModel.editActivity(tripId, editedActivity)

        assertTrue(result.isSuccessful)
        assertEquals("Activity updated successfully!", result.message)
    }

    @Test
    fun editActivityFailsBlankFields() {
        val badActivity = TripActivity("1", " ", "", "  ", LocalDate.now(), LocalTime.now(), ActivityCategory.CULTURE, 0, 0, 0)
        val result = viewModel.editActivity("tripId", badActivity)

        assertFalse(result.isSuccessful)
        assertEquals("Invalid data. All fields must be completed.", result.message)
    }

    @Test
    fun deleteActivitySuccess() {
        val tripId = UUID.randomUUID().toString()
        val activityId = "1"
        val activity = TripActivity(activityId, "Museum", "Desc", "Loc", LocalDate.now().plusDays(2), LocalTime.of(10,0), ActivityCategory.CULTURE, 0, 0, 0)
        val trip = Trip(tripId, "Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "", 0, mutableListOf(activity))
        repository.addTrip(trip)

        val result = viewModel.deleteActivity(tripId, activityId)

        assertTrue(result.isSuccessful)
        assertEquals("Activity deleted successfully!", result.message)
    }
}*/