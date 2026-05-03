package com.example.discoverlib.ui.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.data.SharedPrefsManager
import com.example.discoverlib.domain.AuthRepository
import com.example.discoverlib.domain.User
import com.example.discoverlib.domain.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object AwaitingVerification : AuthState()
    data class Error(val message: String) : AuthState()
}



@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        Log.d(TAG, "Checking initial auth status...")
        if (authRepository.getCurrentUser() == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            Log.i(TAG, "User is already logged in: ${authRepository.getCurrentUser()?.uid}")
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, pass: String) {
        Log.d(TAG, "Attempting login...")

        if (email.isBlank() || pass.isBlank()) {
            Log.w(TAG, "Login failed: Empty fields")
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val uid = authRepository.login(email, pass)
                Log.i(TAG, "Login successful. UID: $uid")
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                Log.e(TAG, "Login error: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun signup(gmail: String, pass: String, username: String, dob: String, address: String, country: String, phone: String, acceptEmails: Boolean) {
        Log.d(TAG, "Attempting signup...")

        if (gmail.isBlank() || pass.isBlank()) {
            Log.w(TAG, "Signup failed: Empty fields")
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val uid = authRepository.signup(gmail, pass, username, dob, address, country, phone, acceptEmails)
                Log.i(TAG, "Signup successful. UID: $uid")
                _authState.value = AuthState.AwaitingVerification
            } catch (e: Exception) {
                Log.e(TAG, "Signup error: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun logout() {
        Log.d(TAG, "Logging out user...")
        authRepository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetPassword(gmail: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.resetPassword(gmail)
                onResult(true, "A reset link has been sent to your email.")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error sending reset email.")
            }
        }
    }
}