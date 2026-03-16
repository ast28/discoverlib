package com.example.discoverlib.data.fakeDB

import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.MockActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

object FakeTripDataSource {
    private val _trips = MutableStateFlow<List<Trip>>(
        listOf(
            Trip(
                id = java.util.UUID.randomUUID().toString(),
                title = "Rome",
                startDate = LocalDate.of(2026, 3, 23),
                endDate = LocalDate.of(2026, 3, 28),
                description = "Explore the heart of the Eternal City, from the historic Colosseum to the charming streets of Trastevere.",
                budgetEur = 257,
                activities = mutableListOf(
                    TripActivity(
                        id = "colosseum",
                        title = "Rome Colosseum Tour",
                        description = "Explore the heart of Ancient Rome with a guided tour of the Colosseum, including access to the arena floor and historical insights into the surrounding Roman Forum ruins.",
                        location = "Piazza del Colosseo, 1, Rome",
                        date = LocalDate.of(2026, 3, 23),
                        time = LocalTime.of(10, 0),
                        category = ActivityCategory.CULTURE,
                        costEur = 10,
                        photo = R.drawable.photo_roma,
                        photo_maps = R.drawable.roma_maps
                    ),
                    TripActivity(
                        id = "lunch",
                        title = "Lunch in Trastevere",
                        description = "Enjoy authentic Roman cuisine in the heart of the historic Trastevere district. Experience a curated local menu in a cozy, reserved setting away from the tourist crowds.",
                        location = "Trastevere, Rome",
                        date = LocalDate.of(2026, 3, 23),
                        time = LocalTime.of(14, 0),
                        category = ActivityCategory.FOOD,
                        costEur = 20,
                        photo = R.drawable.photo_lunch,
                        photo_maps = R.drawable.lunch_maps
                    ),
                    TripActivity(
                        id = "villa-borghese",
                        title = "Villa Borghese Walk",
                        description = "A relaxing stroll through Rome's most elegant park. Discover lush gardens, stunning panoramic views of the city, and the hidden gems of this historic villa.",
                        location = "Villa Borghese, Rome",
                        date = LocalDate.of(2026, 3, 24),
                        time = LocalTime.of(16, 0),
                        category = ActivityCategory.NATURE,
                        costEur = 5,
                        photo = R.drawable.photo_villa,
                        photo_maps = R.drawable.villa_maps
                    )
                )
            ),
            Trip(
                id = java.util.UUID.randomUUID().toString(),
                title = "London",
                startDate = LocalDate.of(2026, 4, 10),
                endDate = LocalDate.of(2026, 4, 14),
                description = "A cosmopolitan getaway to see the iconic Big Ben, explore the British Museum, and enjoy the vibrant markets of Camden Town.",
                budgetEur = 490,
                activities = mutableListOf()
            ),
            Trip(
                id = java.util.UUID.randomUUID().toString(),
                title = "Paris",
                startDate = LocalDate.of(2026, 5, 4),
                endDate = LocalDate.of(2026, 5, 8),
                description = "The City of Light. Enjoy walks along the Seine, world-class art at the Louvre, and the magical atmosphere of Montmartre.",
                budgetEur = 410,
                activities = mutableListOf()
            )
        )
    )

    private val _suggestedActivities = MutableStateFlow<List<MockActivity>>(listOf(
        MockActivity(java.util.UUID.randomUUID().toString(), "Rome Colosseum Tour", "Guided tour of the iconic Colosseum, Roman Forum, and Palatine Hill.", "Piazza del Colosseo, 1", "Tours", 35, 4.8),
        MockActivity(java.util.UUID.randomUUID().toString(), "Vatican Museums & Sistine Chapel", "Skip the line ticket to explore the vast art collection of the Vatican.", "Vatican City", "Museums", 40, 4.9),
        MockActivity(java.util.UUID.randomUUID().toString(), "Trattoria da Enzo al 29", "Traditional Roman dinner with authentic local specialties like carbonara.", "Via dei Vascellari, 29", "Food", 25, 4.7),
        MockActivity(java.util.UUID.randomUUID().toString(), "Villa Borghese Bike Rental", "Relaxing bike ride through Rome's biggest and most beautiful public park.", "Piazzale del Museo Borghese", "Nature", 15, 4.5),
        MockActivity(java.util.UUID.randomUUID().toString(), "Pasta Making Class", "Learn to make fresh pasta from scratch with a professional local chef.", "Via Nazionale, 42", "Food", 55, 4.9),
        MockActivity(java.util.UUID.randomUUID().toString(), "Pantheon Guided Visit", "Audio guide for the best-preserved monument of ancient Rome.", "Piazza della Rotonda", "Tours", 10, 4.6)
    ))

    fun getSuggestedActivities(): List<MockActivity> {
        return _suggestedActivities.value
    }

    fun getTripsFlow(): Flow<List<Trip>> {
        return _trips.asStateFlow()
    }

    fun getTrips(): List<Trip> {
        return _trips.value
    }

    fun getOneTrip(tripId: String): Trip? {
        return _trips.value.find { it.id == tripId }
    }

    fun addTrip(trip: Trip): Boolean {
        val currentList = _trips.value.toMutableList()
        val added = currentList.add(trip)
        if (added) {
            _trips.value = currentList
        }
        return added
    }

    fun editTrip(trip: Trip): Boolean {
        val currentList = _trips.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == trip.id }
        return if (index != -1) {
            currentList[index] = trip
            _trips.value = currentList
            true
        } else {
            false
        }
    }

    fun deleteTrip(tripId: String): Boolean {
        val currentList = _trips.value.toMutableList()
        val removed = currentList.removeIf { it.id == tripId }
        if (removed) {
            _trips.value = currentList
        }
        return removed
    }

    fun getOneActivity(tripId: String, activityId: String): TripActivity? {
        val trip = _trips.value.find { it.id == tripId }
        return trip?.activities?.find { it.id == activityId }
    }

    fun addActivity(tripId: String, activity: TripActivity): Boolean {
        val currentList = _trips.value.toMutableList()
        val tripIndex = currentList.indexOfFirst { it.id == tripId }
        return if (tripIndex != -1) {
            val trip = currentList[tripIndex]
            val newActivities = trip.activities.toMutableList()
            newActivities.add(activity)
            currentList[tripIndex] = trip.copy(activities = newActivities)
            _trips.value = currentList
            true
        } else {
            false
        }
    }

    fun editActivity(tripId: String, activity: TripActivity): Boolean {
        val currentList = _trips.value.toMutableList()
        val tripIndex = currentList.indexOfFirst { it.id == tripId }
        return if (tripIndex != -1) {
            val trip = currentList[tripIndex]
            val activityIndex = trip.activities.indexOfFirst { it.id == activity.id }
            if (activityIndex != -1) {
                val newActivities = trip.activities.toMutableList()
                newActivities[activityIndex] = activity
                currentList[tripIndex] = trip.copy(activities = newActivities)
                _trips.value = currentList
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    fun deleteActivity(tripId: String, activityId: String): Boolean {
        val currentList = _trips.value.toMutableList()
        val tripIndex = currentList.indexOfFirst { it.id == tripId }
        return if (tripIndex != -1) {
            val trip = currentList[tripIndex]
            val newActivities = trip.activities.toMutableList()
            val removed = newActivities.removeIf { it.id == activityId }
            if (removed) {
                currentList[tripIndex] = trip.copy(activities = newActivities)
                _trips.value = currentList
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
