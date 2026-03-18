package com.example.discoverlib.data.repository

import javax.inject.Inject
import com.example.discoverlib.data.fakeDB.FakeTripDataSource
import com.example.discoverlib.data.local.SharedPrefsManager
import com.example.discoverlib.domain.MockActivity
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TripRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPrefsManager
) : TripRepository {
    private val dataSource = FakeTripDataSource

    override fun getTeam(): List<TeamMember> {
        return listOf(
            TeamMember("AS", "Alba Senar Tejedor", "Developer")
        )
    }

    override fun getSuggestedActivities(): List<MockActivity> {
        return dataSource.getSuggestedActivities()
    }


    override fun getTrips(): Flow<List<Trip>> {
        return dataSource.getTripsFlow()
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
        val dateOfBirthStr = sharedPrefs.dateOfBirth
        val isDark = sharedPrefs.darkTheme
        val lang = sharedPrefs.userLanguage

        val parsedDate = try {
            LocalDate.parse(dateOfBirthStr)
        } catch (e: Exception) {
            LocalDate.now()
        }

        return User(
            username = name,
            dateOfBirth = parsedDate,
            darkMode = isDark,
            language = lang
        )
    }
    override fun saveUser(user: User): Boolean {
        sharedPrefs.username = user.username
        sharedPrefs.dateOfBirth = user.dateOfBirth.toString()
        sharedPrefs.darkTheme = user.darkMode
        sharedPrefs.userLanguage = user.language

        return true
    }

    override fun updateDarkMode(isDark: Boolean) {
        sharedPrefs.darkTheme = isDark
    }

    override fun isDarkMode(): Boolean {
        return sharedPrefs.darkTheme
    }
}
