package com.example.discoverlib.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.discoverlib.data.local.AppDatabase
import com.example.discoverlib.data.local.entity.ActivityEntity
import com.example.discoverlib.data.local.entity.TripEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var activityDao: ActivityDao
    private lateinit var tripDao: TripDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        activityDao = database.activityDao()
        tripDao = database.tripDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadActivity() = runBlocking {
        val trip = TripEntity(
            id = "trip-parent",
            userId = "user-1",
            title = "Viaje",
            startDate = "2026-01-01",
            endDate = "2026-01-05",
            description = "Desc",
            price = 100
        )
        tripDao.addTrip(trip)

        val activity = ActivityEntity(
            id = "act-1",
            tripId = "trip-parent",
            title = "Visita Museo",
            description = "Arte moderno",
            location = "Centro",
            date = "2026-01-02",
            time = "10:00",
            category = "CULTURE",
            price = 15,
            photo = 0,
            photo_maps = 0
        )

        activityDao.addActivity(activity)

        val loadedActivity = activityDao.getOneActivity("act-1")
        assertNotNull(loadedActivity)
        assertEquals("Visita Museo", loadedActivity?.title)
    }

    @Test
    fun deleteTripActivities() = runBlocking {
        val trip = TripEntity("trip-parent", "user-1", "Viaje", "2026-01-01", "2026-01-05", "Desc", 100)
        tripDao.addTrip(trip)

        val activity = ActivityEntity("act-1", "trip-parent", "Visita", "Desc", "Loc", "2026-01-02", "10:00", "CULTURE", 15, 0, 0)
        activityDao.addActivity(activity)

        activityDao.deleteTripActivities("trip-parent")

        val loadedActivities = activityDao.getTripActivitiesList("trip-parent")
        assertTrue(loadedActivities.isEmpty())
    }
}