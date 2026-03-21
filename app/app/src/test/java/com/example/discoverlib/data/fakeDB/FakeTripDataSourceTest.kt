package com.example.discoverlib.data.fakeDB

import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class FakeTripDataSourceTest {

    @Test
    fun addTripTest() {
        val initialSize = FakeTripDataSource.getTrips().size
        val newId = UUID.randomUUID().toString()
        val newTrip = Trip(
            id = newId,
            title = "Tokyo",
            startDate = LocalDate.of(2026, 10, 1),
            endDate = LocalDate.of(2026, 10, 15),
            description = "Trip to Tokyo",
            budgetEur = 0,
            activities = mutableListOf()
        )

        val result = FakeTripDataSource.addTrip(newTrip)

        assertTrue(result)
        val trips = FakeTripDataSource.getTrips()
        assertEquals(initialSize + 1, trips.size)

        val fetchedTrip = FakeTripDataSource.getOneTrip(newId)
        assertNotNull(fetchedTrip)
        assertEquals("Tokyo", fetchedTrip?.title)
    }

    @Test
    fun getOneTripTestTrue() {
        val newId = UUID.randomUUID().toString()
        val trip = Trip(
            id = newId,
            title = "New York",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(5),
            description = "Prueba de lectura",
            budgetEur = 800,
            activities = mutableListOf()
        )
        FakeTripDataSource.addTrip(trip)

        val fetchedTrip = FakeTripDataSource.getOneTrip(newId)

        assertNotNull(fetchedTrip)
        assertEquals("New York", fetchedTrip?.title)
    }

    @Test
    fun getOneTripTestFalse() {
        val fakeId = UUID.randomUUID().toString()
        val fetchedTrip = FakeTripDataSource.getOneTrip(fakeId)

        assertNull(fetchedTrip)
    }

    @Test
    fun editTripTest() {
        val newId = UUID.randomUUID().toString()
        val originalTrip = Trip(
            id = newId,
            title = "Berlin",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(3),
            description = "Update test",
            budgetEur = 0,
            activities = mutableListOf()
        )
        FakeTripDataSource.addTrip(originalTrip)

        val result = FakeTripDataSource.editTrip(Trip(
            id = newId,
            title = "Updated Berlin",
            startDate = LocalDate.now().plusDays(1),
            endDate = LocalDate.now().plusDays(3),
            description = "Update test",
            budgetEur = 0,
            activities = mutableListOf()
        ))

        assertTrue(result)
        val fetchedTrip = FakeTripDataSource.getOneTrip(newId)
        assertEquals("Updated Berlin", fetchedTrip?.title)
        assertEquals(LocalDate.now().plusDays(1), fetchedTrip?.startDate)
    }

    @Test
    fun editTripTestTripDoesNotExist() {
        val trip = Trip(
            id = UUID.randomUUID().toString(),
            title = "No existing trip",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(3),
            description = "This trip doesn't exist",
            budgetEur = 0,
            activities = mutableListOf()
        )

        val result = FakeTripDataSource.editTrip(trip)

        assertFalse(result)
    }

    @Test
    fun deleteTripTest() {
        val newId = UUID.randomUUID().toString()
        val trip = Trip(
            id = newId,
            title = "Madrid",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(2),
            description = "Delete test",
            budgetEur = 0,
            activities = mutableListOf()
        )
        FakeTripDataSource.addTrip(trip)
        assertNotNull(FakeTripDataSource.getOneTrip(newId))

        val result = FakeTripDataSource.deleteTrip(newId)

        assertTrue(result)
        assertNull(FakeTripDataSource.getOneTrip(newId))
    }

    @Test
    fun deleteTripTestTripDoesNotExist() {
        val fakeId = UUID.randomUUID().toString()
        val result = FakeTripDataSource.deleteTrip(fakeId)

        assertFalse(result)
    }






    @Test
    fun addActivityTest() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val newActivityId = UUID.randomUUID().toString()
        val newActivity = TripActivity(
            id = newActivityId,
            title = "Museum tour",
            description = "..",
            location = "..",
            date = LocalDate.now(),
            time = LocalTime.of(10, 0),
            category = ActivityCategory.CULTURE,
            costEur = 15,
            photo = 0,
            photo_maps = 0
        )

        val result = FakeTripDataSource.addActivity(tripId, newActivity)

        assertTrue(result)
        val fetchedActivity = FakeTripDataSource.getOneActivity(tripId, newActivityId)
        assertNotNull(fetchedActivity)
        assertEquals("Museum tour", fetchedActivity?.title)
    }

    @Test
    fun addActivityTestTripDoesNotExist() {
        val fakeTripId = UUID.randomUUID().toString()
        val newActivity = TripActivity("1", "Test", "", "", LocalDate.now(), LocalTime.now(), ActivityCategory.FOOD, 0, 0, 0)

        val result = FakeTripDataSource.addActivity(fakeTripId, newActivity)

        assertFalse(result)
    }

    @Test
    fun getOneActivityTest() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val actId = UUID.randomUUID().toString()
        val activity = TripActivity(actId, "Dinner", "", "", LocalDate.now(), LocalTime.now(), ActivityCategory.FOOD, 20, 0, 0)
        FakeTripDataSource.addActivity(tripId, activity)

        val fetchedActivity = FakeTripDataSource.getOneActivity(tripId, actId)

        assertNotNull(fetchedActivity)
        assertEquals("Dinner", fetchedActivity?.title)
    }

    @Test
    fun getOneActivityTestActivityDoesNotExist() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val fetchedActivity = FakeTripDataSource.getOneActivity(tripId, UUID.randomUUID().toString())

        assertNull(fetchedActivity)
    }

    @Test
    fun editActivityTest_ReturnsTrueAndModifiesData() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val actId = UUID.randomUUID().toString()
        val originalActivity = TripActivity(actId, "Walking", "", "", LocalDate.now(), LocalTime.now(), ActivityCategory.NATURE, 0, 0, 0)
        FakeTripDataSource.addActivity(tripId, originalActivity)

        val modifiedActivity = originalActivity.copy(title = "Walking in the morning", costEur = 5)
        val result = FakeTripDataSource.editActivity(tripId, modifiedActivity)

        assertTrue(result)
        val fetchedActivity = FakeTripDataSource.getOneActivity(tripId, actId)
        assertEquals("Walking in the morning", fetchedActivity?.title)
        assertEquals(5, fetchedActivity?.costEur)
    }

    @Test
    fun editActivityTestActivityDoesNotExist() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val ghostActivity = TripActivity(UUID.randomUUID().toString(), "Ghost", "", "", LocalDate.now(), LocalTime.now(), ActivityCategory.CULTURE, 0, 0, 0)
        val result = FakeTripDataSource.editActivity(tripId, ghostActivity)

        assertFalse(result)
    }

    @Test
    fun deleteActivityTest() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))

        val actId = UUID.randomUUID().toString()
        val activity = TripActivity(actId, "Cinema", "", "", LocalDate.now(), LocalTime.now(), ActivityCategory.CULTURE, 10, 0, 0)
        FakeTripDataSource.addActivity(tripId, activity)

        assertNotNull(FakeTripDataSource.getOneActivity(tripId, actId))

        val result = FakeTripDataSource.deleteActivity(tripId, actId)

        assertTrue(result)
        assertNull(FakeTripDataSource.getOneActivity(tripId, actId))
    }

    @Test
    fun deleteActivityTestActivityDoesNotExist() {
        val tripId = UUID.randomUUID().toString()
        FakeTripDataSource.addTrip(Trip(tripId, "Test Trip", LocalDate.now(), LocalDate.now().plusDays(2), "", 0, mutableListOf()))
        val result = FakeTripDataSource.deleteActivity(tripId, UUID.randomUUID().toString())

        assertFalse(result)
    }
}