package com.example.discoverlib.data.fakeDB

import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import java.time.LocalDate
import java.time.LocalTime

object FakeTripDataSource {
    private val trips = mutableListOf<Trip>(
        Trip(
            id = "0",
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
            id = "1",
            title = "London",
            startDate = LocalDate.of(2026, 4, 10),
            endDate = LocalDate.of(2026, 4, 14),
            description = "A cosmopolitan getaway to see the iconic Big Ben, explore the British Museum, and enjoy the vibrant markets of Camden Town.",
            budgetEur = 490,
            activities = mutableListOf()
        ),
        Trip(
            id = "2",
            title = "Paris",
            startDate = LocalDate.of(2026, 5, 4),
            endDate = LocalDate.of(2026, 5, 8),
            description = "The City of Light. Enjoy walks along the Seine, world-class art at the Louvre, and the magical atmosphere of Montmartre.",
            budgetEur = 410,
            activities = mutableListOf()
        ))

    fun getTrips(): List<Trip> {
        return trips
    }
    fun getOneTrip(tripId: String): Trip? {
        return trips.find { it.id == tripId }
    }
    fun addTrip(trip: Trip): Boolean {
        return trips.add(trip)
    }
    fun editTrip(trip: Trip): Boolean {
        val notUpdatedTrip = trips.find { it.id == trip.id }
        return if (notUpdatedTrip != null) {
            notUpdatedTrip?.title = trip.title
            notUpdatedTrip?.startDate = trip.startDate
            notUpdatedTrip?.endDate = trip.endDate
            notUpdatedTrip?.description = trip.description
            notUpdatedTrip?.budgetEur = trip.budgetEur
            notUpdatedTrip?.activities = trip.activities
            true
        }
        else { false /*no existeix viatge*/ }
    }
    fun deleteTrip(tripId: String): Boolean {
        val trip = trips.find { it.id == tripId }
        return if (trip != null) {
            trips.remove(trip)
        } else { false }
    }

    fun getActivities(trip: Trip): List<TripActivity> {
        return trip.activities
    }
    fun getOneActivity(tripId: String, activityId: String): TripActivity? {
        val trip = trips.find { it.id == tripId }
        val activity = trip?.activities?.find { it.id == activityId }
        return activity
    }
    fun addActivity(tripId: String, activity: TripActivity): Boolean {
        val trip = trips.find { it.id == tripId }
        return if (trip != null) {
            trip.activities.add(activity)
        } else {
            false
        }
    }
    fun editActivity(tripId: String, activity: TripActivity): Boolean {
        val notUpdatedTrip = trips.find { it.id == tripId }
        val notUpdatedTripActivities = notUpdatedTrip?.activities?.find {it.id == activity.id}
        return if (notUpdatedTripActivities != null) {
            notUpdatedTripActivities?.title = activity.title
            notUpdatedTripActivities?.description = activity.description
            notUpdatedTripActivities?.date = activity.date
            notUpdatedTripActivities?.time = activity.time
            notUpdatedTripActivities?.location = activity.location
            notUpdatedTripActivities?.category = activity.category
            notUpdatedTripActivities?.costEur = activity.costEur
            notUpdatedTripActivities?.photo = activity.photo
            notUpdatedTripActivities?.photo_maps = activity.photo_maps
            true
        } else { false }
    }
    fun deleteActivity(tripId: String, activityId: String): Boolean {
        val trip = trips.find { it.id == tripId }
        val activity = trip?.activities?.find { it.id == activityId }

        return if (trip != null && activity != null) {
            trip.activities.remove(activity)
        } else {
            false
        }
    }

}

