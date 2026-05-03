package com.example.discoverlib.domain

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
interface AuthRepository {
    suspend fun login(gmail: String, password: String): String
    fun getCurrentUser(): FirebaseUser?

    fun getCurrentUserFlow(): Flow<FirebaseUser?>

    suspend fun signup(gmail: String,
                       password: String,
                       username: String,
                       dob: String,
                       address: String,
                       country: String,
                       phone: String,
                       acceptEmails: Boolean): String
    fun logout()
    suspend fun resetPassword(gmail: String)
}