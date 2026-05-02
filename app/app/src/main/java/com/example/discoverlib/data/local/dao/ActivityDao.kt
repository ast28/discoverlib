package com.example.discoverlib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.discoverlib.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities")
    fun getAllActivities(): Flow<List<ActivityEntity>>
    @Query("SELECT * FROM activities WHERE tripId = :tripId")
    fun getTripActivities(tripId: String) : Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE tripId = :tripId")
    suspend fun getTripActivitiesList(tripId: String): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE id = :activityId")
    suspend fun getOneActivity(activityId: String) : ActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActivity(activity: ActivityEntity)

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE tripId = :tripId")
    suspend fun deleteTripActivities(tripId: String)

    @Query("DELETE FROM activities WHERE tripId = :tripId AND id = :activityId")
    suspend fun deleteOneActivity(tripId: String, activityId: String)
}