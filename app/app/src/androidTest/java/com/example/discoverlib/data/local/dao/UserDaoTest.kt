package com.example.discoverlib.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.discoverlib.data.local.AppDatabase
import com.example.discoverlib.data.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = database.userDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertUserAndRead() = runBlocking {
        val user = UserEntity(
            id = "test-uid-123",
            username = "TestUser",
            dateOfBirth = "2000-01-01",
            darkMode = true,
            language = "en",
            address = "Fake St 123",
            country = "Spain",
            phoneNumber = "123456789",
            acceptReceiveEmails = true
        )

        userDao.addUser(user)

        val loadedUser = userDao.getOneUser("test-uid-123")
        assertNotNull(loadedUser)
        assertEquals("TestUser", loadedUser?.username)
        assertEquals(true, loadedUser?.darkMode)
    }

    @Test
    fun deleteUserTest() = runBlocking {
        val user = UserEntity(
            id = "test-delete-123",
            username = "ToBeDeleted",
            dateOfBirth = "2000-01-01",
            darkMode = false,
            language = "es",
            address = "",
            country = "",
            phoneNumber = "",
            acceptReceiveEmails = false
        )
        userDao.addUser(user)
        userDao.deleteUser("test-delete-123")

        val loadedUser = userDao.getOneUser("test-delete-123")
        assertNull(loadedUser)
    }
}