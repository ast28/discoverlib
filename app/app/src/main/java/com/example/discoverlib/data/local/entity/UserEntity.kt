package com.example.discoverlib.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val dateOfBirth: String,
    val darkMode: Boolean,
    val language: String,
    val address: String,
    val country: String,
    val phoneNumber: String,
    val acceptReceiveEmails: Boolean
)