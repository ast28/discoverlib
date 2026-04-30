package com.example.discoverlib.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoverlib.data.SharedPrefsManager
import com.example.discoverlib.domain.TripRepository
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
private const val DEFAULT_USER_ID = "user_local_1"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: TripRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _pendingLanguageSnackbarCode = MutableStateFlow<String?>(null)
    val pendingLanguageSnackbarCode: StateFlow<String?> = _pendingLanguageSnackbarCode.asStateFlow()

    init {
        Log.d(TAG, "Initializing UserViewModel and loading base user...")
        viewModelScope.launch {
            try {
                _currentUser.value = repository.getOneUser(DEFAULT_USER_ID)
                if (_currentUser.value != null) {
                    Log.i(TAG, "User successfully loaded from DB")
                } else {
                    Log.i(TAG, "User not found in DB, a new one will be created when saving preferences")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user: ${e.message}")
            }
        }
    }


    fun saveNewUsername(newName: String, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to change username to: '$newName'")
        val cleanedName = newName.trim()

        if (cleanedName.isBlank() || cleanedName.length < 3 || cleanedName.length > 20) {
            Log.e(TAG, "Validation failed: Username must be between 3 and 20 characters")
            onResult(ValidationResult(false, "Username must be between 3 and 20 characters."))
            return
        }

        viewModelScope.launch {
            try {
                val user = _currentUser.value
                if (user != null) {
                    val updatedUser = user.copy(username = cleanedName)
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    Log.i(TAG, "Username successfully updated in Room (UPDATE)")
                } else {
                    val newUser = User(
                        id = DEFAULT_USER_ID,
                        username = cleanedName,
                        dateOfBirth = LocalDate.now(),
                        darkMode = false,
                        language = "en"
                    )
                    repository.addUser(newUser)
                    _currentUser.value = newUser
                    Log.i(TAG, "New user created with username '$cleanedName' in Room (ADD)")
                }
                onResult(ValidationResult(true, "Username updated successfully!"))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save username: ${e.message}")
                onResult(ValidationResult(false, "Internal error updating username."))
            }
        }
    }

    fun saveNewDateOfBirth(newDate: LocalDate, onResult: (ValidationResult) -> Unit) {
        Log.d(TAG, "Validating request to change date of birth to: $newDate")

        if (newDate.isAfter(LocalDate.now())) {
            Log.e(TAG, "Validation failed: Date of birth cannot be in the future")
            onResult(ValidationResult(false, "Date of birth cannot be in the future."))
            return
        }

        viewModelScope.launch {
            try {
                val user = _currentUser.value
                if (user != null) {
                    val updatedUser = user.copy(dateOfBirth = newDate)
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    Log.i(TAG, "Date of birth successfully updated in Room (UPDATE)")
                } else {
                    val newUser = User(
                        id = DEFAULT_USER_ID,
                        username = "",
                        dateOfBirth = newDate,
                        darkMode = false,
                        language = "en"
                    )
                    repository.addUser(newUser)
                    _currentUser.value = newUser
                    Log.i(TAG, "New user created with date of birth in Room (ADD)")
                }
                onResult(ValidationResult(true, "Date of birth updated successfully!"))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save date of birth: ${e.message}")
                onResult(ValidationResult(false, "Internal error updating date."))
            }
        }
    }



    fun getDarkMode(): Boolean {
        return _currentUser.value?.darkMode ?: false
    }

    fun saveDarkMode(isDark: Boolean) {
        Log.d(TAG, "Request to change Dark Mode to: $isDark")
        viewModelScope.launch {
            try {
                val user = _currentUser.value
                if (user != null) {
                    val updatedUser = user.copy(darkMode = isDark)
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    Log.i(TAG, "Dark Mode preference updated to $isDark (UPDATE)")
                } else {
                    val newUser = User(
                        id = DEFAULT_USER_ID,
                        username = "",
                        dateOfBirth = LocalDate.now(),
                        darkMode = isDark,
                        language = "en"
                    )
                    repository.addUser(newUser)
                    _currentUser.value = newUser
                    Log.i(TAG, "User created with Dark Mode preference $isDark (ADD)")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save dark mode: ${e.message}")
            }
        }
    }

    fun getLanguage(): String {
        return sharedPrefs.userLanguage
    }

    fun saveLanguage(newLanguage: String) {
        Log.d(TAG, "Request to change Language to: $newLanguage")
        viewModelScope.launch {
            try {
                val user = _currentUser.value
                if (user != null) {
                    val updatedUser = user.copy(language = newLanguage)
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    Log.i(TAG, "Language preference updated to $newLanguage in DB (UPDATE)")
                } else {
                    val newUser = User(
                        id = DEFAULT_USER_ID,
                        username = "",
                        dateOfBirth = LocalDate.now(),
                        darkMode = false,
                        language = newLanguage
                    )
                    repository.addUser(newUser)
                    _currentUser.value = newUser
                    Log.i(TAG, "User created with language preference $newLanguage in DB (ADD)")
                }

                sharedPrefs.userLanguage = newLanguage
                _pendingLanguageSnackbarCode.value = newLanguage
                Log.i(TAG, "Language $newLanguage also saved in SharedPreferences")

            } catch (e: Exception) {
                Log.e(TAG, "Failed to save language: ${e.message}")
            }
        }
    }

    fun consumePendingLanguageSnackbar() {
        _pendingLanguageSnackbarCode.value = null
    }
}