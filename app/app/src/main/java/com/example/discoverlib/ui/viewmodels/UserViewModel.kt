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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: StateFlow<User?> = authRepository.getCurrentUserFlow()
        .flatMapLatest { firebaseUser ->
            if (firebaseUser == null) {
                flowOf(null)
            } else {
                repository.getUserFlow(firebaseUser.uid)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isDarkMode: StateFlow<Boolean> = authRepository.getCurrentUserFlow()
        .flatMapLatest { firebaseUser ->
            if (firebaseUser == null) {
                flowOf(false)
            } else {
                repository.getUserFlow(firebaseUser.uid)
                    .map { it?.darkMode ?: false }
                    .onStart { emit(sharedPrefs.darkTheme) } // Caché rápida para el Splash Screen
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = authRepository.getCurrentUser() != null && sharedPrefs.darkTheme
        )

    private val _pendingLanguageSnackbarCode = MutableStateFlow<String?>(null)
    val pendingLanguageSnackbarCode: StateFlow<String?> = _pendingLanguageSnackbarCode.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { firebaseUser ->
                if (firebaseUser != null) {
                    val localUser = repository.getOneUser(firebaseUser.uid)
                    if (localUser == null) {
                        Log.i(TAG, "Creating default local profile for UID: ${firebaseUser.uid}")
                        val defaultUser = User(
                            id = firebaseUser.uid,
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
    }

    fun getFirebaseUid(): String = authRepository.getCurrentUser()?.uid ?: ""


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
        val user = currentUser.value ?: return
        val updatedUser = user.copy(
            username = username,
            dateOfBirth = dob,
            address = address,
            country = country,
            phoneNumber = phone,
            acceptReceiveEmails = acceptEmails
        )

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
        val user = currentUser.value ?: return

        sharedPrefs.darkTheme = isDark

        val updatedUser = user.copy(darkMode = isDark)
        viewModelScope.launch {
            try {
                repository.updateUser(updatedUser)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save dark mode", e)
            }
        }
    }

    fun saveLanguage(newLanguage: String) {
        val user = currentUser.value ?: return
        val updatedUser = user.copy(language = newLanguage)

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