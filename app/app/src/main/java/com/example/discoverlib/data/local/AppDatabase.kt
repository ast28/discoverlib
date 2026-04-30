package com.example.discoverlib.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.discoverlib.data.local.dao.ActivityDao
import com.example.discoverlib.data.local.dao.TripDao
import com.example.discoverlib.data.local.dao.UserDao
import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.entity.TripEntity
import com.example.discoverlib.data.local.entity.UserEntity


@Database(
    entities = [TripEntity::class, ActivityEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
}