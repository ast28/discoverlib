package com.example.discoverlib.domain

import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTeam(): List<TeamMember>


    fun getTrips(): Flow<List<Trip>>
    suspend fun getOneTrip(tripId: String): Trip?
    suspend fun addTrip(trip: Trip)
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(tripId: String)


    fun getTripActivities(tripId: String) : Flow<List<TripActivity>>
    suspend fun getTripActivitiesList(tripId: String): List<TripActivity>
    suspend fun getOneActivity(activityId: String): TripActivity?
    suspend fun addActivity(tripId: String, activity: TripActivity)
    suspend fun updateActivity(tripId: String, activity: TripActivity)
    suspend fun deleteTripActivities(tripId: String)
    suspend fun deleteOneActivity(tripId: String, activityId: String)

    fun getUsers(): Flow<List<User>>
    suspend fun getOneUser(userId: String) : User?
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
}
