package com.example.discoverlib.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.discoverlib.domain.TripRepository
import com.example.discoverlib.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.discoverlib.data.local.SharedPrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: TripRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow(repository.getUser())
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun getSavedUsername(): String {
        val user = repository.getUser()
        return if (user != null) { user.username }
        else { "Not defined user name" }
    }

    fun saveNewUsername(newName: String): Boolean {
        Log.d(TAG, "Simulating username change: $newName")
        val cleanedName = newName.trim()
        if (cleanedName.isBlank() || cleanedName.length < 3 || cleanedName.length > 20) {
            Log.e(TAG, "Validation failed: Username must be between 3 and 20 characters and not blank.")
            return false
        }
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(username = cleanedName)
        } else {
            User(
                username = cleanedName,
                dateOfBirth = LocalDate.now(),
                darkMode = false,
                language = "en"
            )
        }
        val success = repository.saveUser(updatedUser)
        if (success) {
            Log.i(TAG, "Username updated successfully in repository")
            _currentUser.value = updatedUser
        } else {
            Log.e(TAG, "Failed to save username in repository")
        }
        return success
    }

    fun getSavedDateOfBirth(): String {
        val user = repository.getUser()
        return if (user != null) { user.dateOfBirth.toString() }
        else { "Not defined date of birth" }
    }

    fun saveNewDateOfBirth(newDate: LocalDate): Boolean {
        Log.d(TAG, "Simulating DOB change: $newDate")
        if (newDate.isAfter(LocalDate.now())) {
            Log.e(TAG, "Validation failed: Date of birth cannot be in the future.")
            return false
        }
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(dateOfBirth = newDate)
        } else {
            User(
                username = "",
                dateOfBirth = newDate,
                darkMode = false,
                language = "en"
            )
        }
        val success = repository.saveUser(updatedUser)
        if (success) {
            Log.i(TAG, "Date of birth updated successfully")
            _currentUser.value = updatedUser
        } else {
            Log.e(TAG, "Failed to save date of birth")
        }
        return success
    }

    fun getDarkMode(): Boolean {
        val user = repository.getUser()
        return user?.darkMode ?: false
    }

    fun saveDarkMode(isDark: Boolean) {
        Log.d(TAG, "Simulating dark mode toggle: $isDark")
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(darkMode = isDark)
        } else {
            User(
                username = "",
                dateOfBirth = LocalDate.now(),
                darkMode = isDark,
                language = "en"
            )
        }
        repository.saveUser(updatedUser)
        Log.i(TAG, "Dark mode preference saved")
        _currentUser.value = updatedUser
    }

    fun getLanguage(): String {
        return sharedPrefs.userLanguage
    }

    fun saveLanguage(newLanguage: String) {
        Log.d(TAG, "Simulating language change: $newLanguage")
        val currentUser = repository.getUser()
        val updatedUser = if (currentUser != null) {
            currentUser.copy(language = newLanguage)
        } else {
            User(
                username = "",
                dateOfBirth = LocalDate.now(),
                darkMode = false,
                language = newLanguage
            )
        }
        repository.saveUser(updatedUser)
        sharedPrefs.userLanguage = newLanguage
        Log.i(TAG, "Language preference saved: $newLanguage")
        _currentUser.value = updatedUser
    }
}
