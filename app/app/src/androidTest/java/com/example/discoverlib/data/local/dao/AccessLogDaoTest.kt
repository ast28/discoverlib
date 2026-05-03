package com.example.discoverlib.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.discoverlib.data.local.AppDatabase
import com.example.discoverlib.data.local.entity.AccessLogEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessLogDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var accessLogDao: AccessLogDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        accessLogDao = database.accessLogDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAccessLog() = runBlocking {
        val log = AccessLogEntity(
            userId = "user-123",
            accessType = "LOGIN",
            dateTime = "2026-05-03T18:00:00"
        )

        accessLogDao.insertLog(log)
    }
}