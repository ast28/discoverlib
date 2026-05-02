package com.example.discoverlib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.discoverlib.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Query("SELECT * FROM trips")
    fun getTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE userId = :currentUserId OR userId = 'DEFAULT_TEST_USER'")
    fun getTripsForUser(currentUserId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getOneTrip(tripId: String): TripEntity?

    @Insert
    suspend fun addTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTrip(tripId: String)

}