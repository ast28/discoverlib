package com.example.discoverlib.ui.viewmodels

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

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: TripRepository,
    private val sharedPrefs: SharedPrefsManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow(repository.getUser())
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun getSavedUsername(): String {
        val user = repository.getUser()
        if (user != null) { return user.username }
        else { return "Not defined user name" }
    }

    fun saveNewUsername(newName: String): Boolean {
        val cleanedName = newName.trim()
        if (cleanedName.isBlank() || cleanedName.length < 3 || cleanedName.length > 20) {
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
        repository.saveUser(updatedUser)
        _currentUser.value = updatedUser
        return true
    }

    fun getSavedDateOfBirth(): String {
        val user = repository.getUser()
        if (user != null) { return user.dateOfBirth.toString() }
        else { return "Not defined date of birth" }
    }

    fun saveNewDateOfBirth(newDate: LocalDate): Boolean {
        if (newDate.isAfter(LocalDate.now())) {
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
        repository.saveUser(updatedUser)
        _currentUser.value = updatedUser
        return true
    }

    fun getDarkMode(): Boolean {
        val user = repository.getUser()
        return user?.darkMode ?: false
    }

    fun saveDarkMode(isDark: Boolean) {
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
        _currentUser.value = updatedUser
    }

    fun getLanguage(): String {
        return sharedPrefs.userLanguage
    }

    fun saveLanguage(newLanguage: String) {
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
        _currentUser.value = updatedUser
    }
}