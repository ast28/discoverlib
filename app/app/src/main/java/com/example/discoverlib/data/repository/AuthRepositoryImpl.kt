package com.example.discoverlib.data.repository

import android.util.Log
import com.example.discoverlib.data.local.dao.AccessLogDao
import com.example.discoverlib.data.local.entity.AccessLogEntity
import javax.inject.Inject
import com.example.discoverlib.domain.AuthRepository
import com.example.discoverlib.domain.User
import com.example.discoverlib.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime


class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val accessLogDao: AccessLogDao,
    private val userRepository: UserRepository
) : AuthRepository {

    private val TAG = "AuthRepository"

    override suspend fun login(gmail: String, password: String): String {
        Log.d(TAG, "Attempting login for email: $gmail")

        try {
            val authResult = auth.signInWithEmailAndPassword(gmail, password).await()
            val user = authResult.user ?: throw Exception("User is null after successful login")

            if (user.isEmailVerified) {
                Log.i(TAG, "Login successful for UID: ${user.uid}")
                accessLogDao.insertLog(
                    AccessLogEntity(
                        userId = user.uid,
                        accessType = "LOGIN",
                        dateTime = LocalDateTime.now().toString()
                    )
                )
                return user.uid
            } else {
                auth.signOut()

                throw Exception("Email address not verified. Please check your email before continuing.")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.message}", e)
            throw e
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        val user = auth.currentUser
        if (user != null) {
            Log.d(TAG, "getCurrentUser: User found with UID: ${user.uid}")
        } else {
            Log.d(TAG, "getCurrentUser: No user currently logged in.")
        }
        return user
    }

    override fun getCurrentUserFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }


    override suspend fun signup(gmail: String,
                                password: String,
                                username: String,
                                dob: String,
                                address: String,
                                country: String,
                                phone: String,
                                acceptEmails: Boolean): String {
        Log.d(TAG, "Attempting signup for email: $gmail")

        try {
            val authResult = auth.createUserWithEmailAndPassword(gmail, password).await()
            val user = authResult.user ?: throw Exception("User is null after successful signup")

            user.sendEmailVerification().await()
            Log.d(TAG, "Email sent.")

            val newUser = User(
                id = user.uid,
                username = username,
                dateOfBirth = LocalDate.parse(dob),
                darkMode = false,
                language = "en",
                email = gmail,
                address = address,
                country = country,
                phoneNumber = phone,
                acceptReceiveEmails = acceptEmails
            )

            userRepository.addUser(newUser)

            auth.signOut()

            Log.i(TAG, "Signup successful for UID: ${user.uid}. Awaiting email verification.")

            return user.uid
        } catch (e: Exception) {
            Log.e(TAG, "Signup failed: ${e.message}", e)
            throw e
        }
    }

    override fun logout() {
        Log.d(TAG, "Attempting logout...")
        try {
            val userId = auth.currentUser?.uid ?: "unknown"

            auth.signOut()
            Log.i(TAG, "Logout successful.")

            CoroutineScope(Dispatchers.IO).launch {
                accessLogDao.insertLog(
                    AccessLogEntity(
                        userId = userId,
                        accessType = "LOGOUT",
                        dateTime = LocalDateTime.now().toString()
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed: ${e.message}", e)
        }
    }

    override suspend fun resetPassword(gmail: String) {
        Log.d(TAG, "Attempting to send password reset email to: $gmail")
        try {
            auth.sendPasswordResetEmail(gmail).await()
            Log.i(TAG, "Password reset email sent successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Password reset failed: ${e.message}", e)
            throw e
        }
    }
}