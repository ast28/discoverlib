package com.example.discoverlib.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.discoverlib.data.local.AppDatabase
import com.example.discoverlib.data.local.entity.TripEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var tripDao: TripDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        tripDao = database.tripDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadTrip() = runBlocking {
        val trip = TripEntity(
            id = "trip-123",
            userId = "user-abc",
            title = "Viaje a Japón",
            startDate = "2026-10-01",
            endDate = "2026-10-15",
            description = "Turismo por Tokio",
            price = 2000
        )

        tripDao.addTrip(trip)

        val loadedTrip = tripDao.getOneTrip("trip-123")
        assertNotNull(loadedTrip)
        assertEquals("Viaje a Japón", loadedTrip?.title)
        assertEquals(2000, loadedTrip?.price)
    }

    @Test
    fun updateTrip() = runBlocking {
        val trip = TripEntity(
            id = "trip-update",
            userId = "user-abc",
            title = "Viaje Inicial",
            startDate = "2026-01-01",
            endDate = "2026-01-05",
            description = "Desc",
            price = 500
        )
        tripDao.addTrip(trip)

        val updatedTrip = trip.copy(title = "Viaje Editado", price = 800)
        tripDao.updateTrip(updatedTrip)

        val loaded = tripDao.getOneTrip("trip-update")
        assertEquals("Viaje Editado", loaded?.title)
        assertEquals(800, loaded?.price)
    }

    @Test
    fun deleteTrip() = runBlocking {
        val trip = TripEntity(
            id = "trip-delete",
            userId = "user-abc",
            title = "Borrar esto",
            startDate = "2026-01-01",
            endDate = "2026-01-05",
            description = "Desc",
            price = 100
        )
        tripDao.addTrip(trip)
        tripDao.deleteTrip("trip-delete")

        val loaded = tripDao.getOneTrip("trip-delete")
        assertNull(loaded)
    }
}