package com.example.discoverlib.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.data.SharedPrefsManager
import com.example.discoverlib.domain.AuthRepository
import com.example.discoverlib.domain.UserRepository
import com.example.discoverlib.domain.User
import com.example.discoverlib.domain.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val authRepository: AuthRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _pendingLanguageSnackbarCode = MutableStateFlow<String?>(null)
    val pendingLanguageSnackbarCode: StateFlow<String?> = _pendingLanguageSnackbarCode.asStateFlow()

    init {
        loadUserFromDatabase()
    }

    fun getFirebaseUid(): String = authRepository.getCurrentUser()?.uid ?: ""

    fun loadUserFromDatabase() {
        val firebaseUser = authRepository.getCurrentUser()
        if (firebaseUser == null) {
            _currentUser.value = null
            return
        }
        val uid = firebaseUser.uid

        viewModelScope.launch {
            repository.getUserFlow(uid).collect { localUser ->
                if (localUser != null) {
                    _currentUser.value = localUser
                } else {
                    Log.i(TAG, "Creating default local profile for UID: $uid")
                    val defaultUser = User(
                        id = uid,
                        username = "Traveler",
                        dateOfBirth = LocalDate.now().minusYears(18),
                        darkMode = false,
                        language = "en",
                        address = "",
                        country = "",
                        phoneNumber = "",
                        acceptReceiveEmails = false
                    )
                    repository.addUser(defaultUser)
                }
            }
        }
    }

    fun checkUsernameAvailability(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isUsed = repository.isUsernameInUse(username)
            onResult(!isUsed)
        }
    }

    fun saveInitialUserProfile(
        username: String,
        dob: LocalDate,
        address: String,
        country: String,
        phone: String,
        acceptEmails: Boolean
    ) {
        val firebaseUser = authRepository.getCurrentUser() ?: return
        val newUser = User(
            id = firebaseUser.uid,
            username = username,
            dateOfBirth = dob,
            darkMode = false,
            language = "en",
            address = address,
            country = country,
            phoneNumber = phone,
            acceptReceiveEmails = acceptEmails
        )

        _currentUser.value = newUser

        viewModelScope.launch {
            try {
                repository.addUser(newUser)
            } catch (e: Exception) {
                Log.e(TAG, "Error saving initial user profile: ${e.message}")
            }
        }
    }

    fun updateFullProfile(
        username: String,
        dob: LocalDate,
        address: String,
        country: String,
        phone: String,
        acceptEmails: Boolean,
        onResult: (ValidationResult) -> Unit
    ) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(
            username = username,
            dateOfBirth = dob,
            address = address,
            country = country,
            phoneNumber = phone,
            acceptReceiveEmails = acceptEmails
        )

        _currentUser.value = updatedUser

        viewModelScope.launch {
            try {
                repository.updateUser(updatedUser)
                onResult(ValidationResult(true, "Profile updated!"))
            } catch (e: Exception) {
                onResult(ValidationResult(false, "Update failed"))
            }
        }
    }

    fun saveDarkMode(isDark: Boolean) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(darkMode = isDark)

        _currentUser.value = updatedUser

        viewModelScope.launch {
            try {
                repository.updateUser(updatedUser)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save dark mode", e)
            }
        }
    }

    fun saveLanguage(newLanguage: String) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(language = newLanguage)

        _currentUser.value = updatedUser

        viewModelScope.launch {
            try {
                repository.updateUser(updatedUser)
                sharedPrefs.userLanguage = newLanguage
                _pendingLanguageSnackbarCode.value = newLanguage
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save language", e)
            }
        }
    }

    fun consumePendingLanguageSnackbar() {
        _pendingLanguageSnackbarCode.value = null
    }
}