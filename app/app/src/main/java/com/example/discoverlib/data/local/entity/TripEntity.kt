package com.example.discoverlib.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val price: Int
)
