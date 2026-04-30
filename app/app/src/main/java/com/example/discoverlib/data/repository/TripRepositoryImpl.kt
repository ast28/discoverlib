package com.example.discoverlib.data.repository

import android.util.Log
import javax.inject.Inject
import com.example.discoverlib.data.local.dao.ActivityDao
import com.example.discoverlib.data.local.dao.TripDao
import com.example.discoverlib.data.local.dao.UserDao
import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.mapper.toDomain
import com.example.discoverlib.data.local.mapper.toEntity
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val activityDao: ActivityDao,
    private val userDao: UserDao
) : TripRepository {

    private val TAG = "TripRepository"

    override fun getTeam(): List<TeamMember> {
        Log.d(TAG, "Fetching team members list")
        return listOf(
            TeamMember("AS", "Alba Senar Tejedor", "Developer")
        )
    }

    override fun getTrips(): Flow<List<Trip>> {
        Log.d(TAG, "Observing the full list of trips (Flow) from SQLite")
        val tripsEntity = tripDao.getTrips()

        return tripsEntity.map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getOneTrip(tripId: String): Trip? {
        Log.d(TAG, "Querying trip details in SQLite: $tripId")
        val tripEntity = tripDao.getOneTrip(tripId) ?: return null

        val activitiesEntities = activityDao.getTripActivitiesList(tripId)
        return tripEntity.toDomain(activitiesEntities)
    }

    override suspend fun addTrip(trip: Trip) {
        Log.i(TAG, "Inserting new trip into SQLite: ${trip.id}")
        val tripEntity = trip.toEntity()
        tripDao.addTrip(tripEntity)

        trip.activities.forEach { activity ->
            val activityEntity = activity.toEntity(trip.id)
            activityDao.addActivity(activityEntity)
        }
    }

    override suspend fun updateTrip(trip: Trip) {
        Log.i(TAG, "Updating trip in SQLite: ${trip.id}")
        val tripEntity = trip.toEntity()
        tripDao.updateTrip(tripEntity)
    }

    override suspend fun deleteTrip(tripId: String) {
        Log.i(TAG, "Deleting trip and all its activities in SQLite: $tripId")
        activityDao.deleteTripActivities(tripId)
        tripDao.deleteTrip(tripId)
    }

    override fun getTripActivities(tripId: String): Flow<List<TripActivity>> {
        Log.d(TAG, "Observing activities for trip $tripId (Flow) from SQLite")
        val activitiesEntity = activityDao.getTripActivities(tripId)

        return activitiesEntity.map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getTripActivitiesList(tripId: String): List<TripActivity> {
        Log.d(TAG, "Querying static list of activities for trip $tripId from SQLite")
        val activitiesEntity = activityDao.getTripActivitiesList(tripId)
        return activitiesEntity.map { entity ->
            entity.toDomain()
        }
    }

    override suspend fun getOneActivity(activityId: String): TripActivity? {
        Log.d(TAG, "Querying activity details in SQLite: $activityId")
        val activityEntity = activityDao.getOneActivity(activityId) ?: return null

        return activityEntity.toDomain()
    }

    override suspend fun addActivity(tripId: String, activity: TripActivity) {
        Log.i(TAG, "Inserting new activity '${activity.title}' for trip $tripId into SQLite")
        val activityEntity = activity.toEntity(tripId)
        activityDao.addActivity(activityEntity)
    }

    override suspend fun updateActivity(tripId: String, activity: TripActivity) {
        Log.i(TAG, "Updating activity '${activity.id}' of trip $tripId in SQLite")
        val activityEntity = activity.toEntity(tripId)
        activityDao.updateActivity(activityEntity)
    }

    override suspend fun deleteTripActivities(tripId: String) {
        Log.i(TAG, "Deleting ALL activities for trip $tripId in SQLite")
        activityDao.deleteTripActivities(tripId)
    }

    override suspend fun deleteOneActivity(tripId: String, activityId: String) {
        Log.i(TAG, "Deleting activity $activityId of trip $tripId in SQLite")
        activityDao.deleteOneActivity(tripId, activityId)
    }

    override fun getUsers(): Flow<List<User>> {
        Log.d(TAG, "Observing users list (Flow) from SQLite")
        val usersEntities = userDao.getUsers()

        return usersEntities.map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getOneUser(userId: String): User? {
        Log.d(TAG, "Querying user details in SQLite: $userId")
        val userEntity = userDao.getOneUser(userId) ?: return null
        return userEntity.toDomain()
    }

    override suspend fun addUser(user: User) {
        Log.i(TAG, "Inserting new user into SQLite: ${user.id}")
        val userEntity = user.toEntity()
        userDao.addUser(userEntity)
    }

    override suspend fun updateUser(user: User) {
        Log.i(TAG, "Updating user data in SQLite: ${user.id}")
        val userEntity = user.toEntity()
        userDao.updateUser(userEntity)
    }

    override suspend fun deleteUser(userId: String) {
        Log.i(TAG, "Deleting user in SQLite: $userId")
        userDao.deleteUser(userId)
    }
}