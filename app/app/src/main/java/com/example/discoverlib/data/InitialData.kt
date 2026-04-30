package com.example.discoverlib.data

import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object InitialData {
    fun getInitialTrips(): List<Trip> {
        return listOf(
            Trip(
                id = UUID.randomUUID().toString(),
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
                        category = ActivityCategory.TOURS,
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
                id = UUID.randomUUID().toString(),
                title = "London",
                startDate = LocalDate.of(2026, 4, 10),
                endDate = LocalDate.of(2026, 4, 14),
                description = "A cosmopolitan getaway to see the iconic Big Ben, explore the British Museum, and enjoy the vibrant markets of Camden Town.",
                budgetEur = 490,
                activities = mutableListOf()
            ),
            Trip(
                id = UUID.randomUUID().toString(),
                title = "Paris",
                startDate = LocalDate.of(2026, 5, 4),
                endDate = LocalDate.of(2026, 5, 8),
                description = "The City of Light. Enjoy walks along the Seine, world-class art at the Louvre, and the magical atmosphere of Montmartre.",
                budgetEur = 410,
                activities = mutableListOf()
            )
        )
    }
}