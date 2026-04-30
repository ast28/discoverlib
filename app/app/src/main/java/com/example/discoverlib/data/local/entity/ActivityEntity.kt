package com.example.discoverlib.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "activities", foreignKeys = [
    ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class ActivityEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val tripId: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val time: String,
    val category: String,
    val price: Int,
    val photo: Int,
    val photo_maps: Int
)

@Entity(tableName = "suggested_activities")
data class SuggestedActivityEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val location: String,
    val category: String,
    val price: Int
)