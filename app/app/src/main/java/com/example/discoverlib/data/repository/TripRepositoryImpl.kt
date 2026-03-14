package com.example.discoverlib.data.repository

import javax.inject.Inject
import com.example.discoverlib.data.fakeDB.FakeTripDataSource
import com.example.discoverlib.data.local.SharedPrefsManager
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.User
import java.time.LocalDate

class TripRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPrefsManager
) : TripRepository {
    private val dataSource = FakeTripDataSource


    override fun getTrips(): List<Trip> {
        return dataSource.getTrips()
    }
    override fun getOneTrip(tripId: String): Trip? {
        return dataSource.getOneTrip(tripId)
    }
    override fun addTrip(trip: Trip): Boolean {
        return dataSource.addTrip(trip)
    }
    override fun editTrip(trip: Trip): Boolean {
        return dataSource.editTrip(trip)
    }
    override fun deleteTrip(tripId: String): Boolean {
        return dataSource.deleteTrip(tripId)
    }


    override fun getActivities(trip: Trip): List<TripActivity> {
        return dataSource.getActivities(trip)
    }
    override fun getOneActivity(tripId: String, activityId: String): TripActivity? {
        return dataSource.getOneActivity(tripId, activityId)
    }
    override fun addActivity(tripId: String, activity: TripActivity): Boolean {
        return dataSource.addActivity(tripId, activity)
    }
    override fun editActivity(tripId: String, activity: TripActivity): Boolean {
        return dataSource.editActivity(tripId, activity)
    }
    override fun deleteActivity(tripId: String, activityId: String): Boolean {
        return dataSource.deleteActivity(tripId, activityId)
    }

    override fun getUser(): User? {
        val name = sharedPrefs.username
        val dateOfBirth = sharedPrefs.dateOfBirth
        val isDark = sharedPrefs.darkTheme
        val lang = sharedPrefs.userLanguage

        if (name.isEmpty() || dateOfBirth.isEmpty()) {
            return null
        }

        return User(
            username = name,
            dateOfBirth = LocalDate.parse(dateOfBirth),
            darkMode = isDark,
            language = lang
        )
    }
    override fun saveUser(user: User): Boolean {
        sharedPrefs.username = user.username
        sharedPrefs.dateOfBirth = user.dateOfBirth.toString() // Passar-lo a text
        sharedPrefs.darkTheme = user.darkMode
        sharedPrefs.userLanguage = user.language

        return true
    }
}