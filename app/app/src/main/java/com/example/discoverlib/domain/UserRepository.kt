package com.example.discoverlib.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun getOneUser(userId: String) : User?
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
    suspend fun isUsernameInUse(username: String): Boolean
}