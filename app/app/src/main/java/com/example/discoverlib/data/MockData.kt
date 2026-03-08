package com.example.discoverlib.data

import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity

object MockData {
    val featuredTrip = Trip(
        id = "roma-2026",
        city = "Roma",
        period = "23-03-2026 to 28-03-2026",
        nights = 5,
        budgetEur = 257,
        activities = listOf(
            TripActivity(
                id = "colosseum",
                title = "Rome Colosseum Tour",
                description = "Explore the heart of Ancient Rome with a guided tour of the Colosseum, including access to the arena floor and historical insights into the surrounding Roman Forum ruins.",
                location = "Piazza del Colosseo, 1, Rome",
                dayLabel = "Mon 23",
                time = "10:00",
                category = ActivityCategory.CULTURE,
                costEur = 10,
                photo = R.drawable.photo_roma,
                photo_mapa = R.drawable.roma_maps
            ),
            TripActivity(
                id = "lunch",
                title = "Lunch in Trastevere",
                description = "Enjoy authentic Roman cuisine in the heart of the historic Trastevere district. Experience a curated local menu in a cozy, reserved setting away from the tourist crowds.",
                location = "Trastevere, Rome",
                dayLabel = "Mon 23",
                time = "14:00",
                category = ActivityCategory.FOOD,
                costEur = 20,
                photo = R.drawable.photo_lunch,
                photo_mapa = R.drawable.lunch_maps
            ),
            TripActivity(
                id = "villa-borghese",
                title = "Villa Borghese Walk",
                description = "A relaxing stroll through Rome's most elegant park. Discover lush gardens, stunning panoramic views of the city, and the hidden gems of this historic villa.",
                location = "Villa Borghese, Rome",
                dayLabel = "Tue 24",
                time = "16:00",
                category = ActivityCategory.NATURE,
                costEur = 5,
                photo = R.drawable.photo_villa,
                photo_mapa = R.drawable.villa_maps
            )
        )
    )

    val trips = listOf(
        featuredTrip,
        featuredTrip.copy(id = "london-2026", city = "London", period = "10-04-2026 to 14-04-2026", budgetEur = 490),
        featuredTrip.copy(id = "paris-2026", city = "Paris", period = "04-05-2026 to 08-05-2026", budgetEur = 410)
    )

    val galleryTrips = listOf("Italia","Roma", "London", "Paris")

    val team = listOf(
        TeamMember("AS", "Alba Senar Tejedor", "Developer")
    )

    val termsSections = listOf(
        "Acceptance of terms" to "By using Discoverlib, you accept these terms and conditions.",
        "Privacy and data" to "We only collect data necessary for the app's local experience.",
        "Use of the application" to "The information provided is for guidance only and should be verified with official providers.",
        "Third-party services" to "The app may integrate external services subject to their own terms.",
        "Modifications" to "The terms may be updated in future versions of the application."
    )
}
