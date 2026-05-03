package com.example.discoverlib.domain

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun getOneUser(userId: String) : User?
    fun getUserFlow(userId: String): Flow<User?>
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
    suspend fun isUsernameInUse(username: String): Boolean
}