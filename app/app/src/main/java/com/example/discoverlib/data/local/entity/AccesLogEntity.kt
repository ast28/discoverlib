package com.example.discoverlib.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "access_logs")
data class AccessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val accessType: String,
    val dateTime: String
)