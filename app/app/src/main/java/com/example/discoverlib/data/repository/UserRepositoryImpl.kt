package com.example.discoverlib.data.repository

import android.util.Log
import javax.inject.Inject
import com.example.discoverlib.data.local.dao.UserDao
import com.example.discoverlib.data.local.mapper.toDomain
import com.example.discoverlib.data.local.mapper.toEntity
import com.example.discoverlib.domain.UserRepository
import com.example.discoverlib.domain.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    private val TAG = "UserRepository"

    override fun getUsers(): Flow<List<User>> {
        Log.d(TAG, "Observing users list (Flow) from SQLite")
        val usersEntities = userDao.getUsers()

        return usersEntities.map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getOneUser(userId: String): User? {
        Log.d(TAG, "Querying user details in SQLite: $userId")
        val userEntity = userDao.getOneUser(userId) ?: return null
        return userEntity.toDomain()
    }

    override fun getUserFlow(userId: String): Flow<User?> {
        return userDao.getUserFlow(userId).map { userEntity ->
            userEntity?.toDomain()
        }
    }

    override suspend fun addUser(user: User) {
        Log.i(TAG, "Inserting new user into SQLite: ${user.id}")
        val userEntity = user.toEntity()
        userDao.addUser(userEntity)
    }

    override suspend fun updateUser(user: User) {
        Log.i(TAG, "Updating user data in SQLite: ${user.id}")
        val userEntity = user.toEntity()
        userDao.updateUser(userEntity)
    }

    override suspend fun deleteUser(userId: String) {
        Log.i(TAG, "Deleting user in SQLite: $userId")
        userDao.deleteUser(userId)
    }

    override suspend fun isUsernameInUse(username: String): Boolean {
        Log.d(TAG, "Checking if username is in use: $username")
        val count = userDao.isUsernameInUse(username)
        return count > 0
    }
}