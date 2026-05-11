package com.example.discoverlib.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.data.SharedPrefsManager
import com.example.discoverlib.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
        if (authRepository.getCurrentUser() == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(gmail: String, pass: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                authRepository.login(gmail, pass)
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signup(gmail: String, pass: String, username: String, dob: String, address: String, country: String, phone: String, acceptEmails: Boolean) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                authRepository.signup(gmail, pass, username, dob, address, country, phone, acceptEmails)
                _authState.value = AuthState.AwaitingVerification
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Signup failed")
            }
        }
    }

    fun logout() {
        Log.d(TAG, "Logging out user and resetting local theme preference")
        sharedPrefs.darkTheme = false
        authRepository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetPassword(gmail: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.resetPassword(gmail)
                onResult(true, "Reset link sent to your email.")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error sending reset email.")
            }
        }
    }
}