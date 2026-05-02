package com.example.discoverlib.data

import com.example.discoverlib.R
import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.entity.TripEntity
import java.util.UUID

object InitialData {
    private val romeId = UUID.randomUUID().toString()
    const val DEFAULT_USER_ID = "DEFAULT_TEST_USER"

    fun getInitialTrips(): List<TripEntity> {
        return listOf(
            TripEntity(
                id = romeId,
                userId = DEFAULT_USER_ID,
                title = "Rome (Example Trip)",
                startDate = "2026-06-23",
                endDate = "2026-06-28",
                description = "Explore the heart of the Eternal City, from the historic Colosseum to the charming streets of Trastevere.",
                price = 257
            )
        )
    }

    fun getInitialActivities(): List<ActivityEntity> {
        return listOf(
            ActivityEntity(
                id = UUID.randomUUID().toString(),
                tripId = romeId,
                title = "Rome Colosseum Tour",
                description = "Explore the heart of Ancient Rome with a guided tour of the Colosseum, including access to the arena floor and historical insights into the surrounding Roman Forum ruins.",
                location = "Piazza del Colosseo, 1, Rome",
                date = "2026-06-23",
                time = "10:00",
                category = "TOURS",
                price = 10,
                photo = R.drawable.photo_roma,
                photo_maps = R.drawable.roma_maps
            ),
            ActivityEntity(
                id = UUID.randomUUID().toString(),
                tripId = romeId,
                title = "Lunch in Trastevere",
                description = "Enjoy authentic Roman cuisine in the heart of the historic Trastevere district. Experience a curated local menu in a cozy, reserved setting away from the tourist crowds.",
                location = "Trastevere, Rome",
                date = "2026-06-23",
                time = "14:00",
                category = "FOOD",
                price = 20,
                photo = R.drawable.photo_lunch,
                photo_maps = R.drawable.lunch_maps
            ),
            ActivityEntity(
                id = UUID.randomUUID().toString(),
                tripId = romeId,
                title = "Villa Borghese Walk",
                description = "A relaxing stroll through Rome's most elegant park. Discover lush gardens, stunning panoramic views of the city, and the hidden gems of this historic villa.",
                location = "Villa Borghese, Rome",
                date = "2026-06-24",
                time = "16:00",
                category = "NATURE",
                price = 5,
                photo = R.drawable.photo_villa,
                photo_maps = R.drawable.villa_maps
            )
        )
    }
}