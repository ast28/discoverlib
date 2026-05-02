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

    fun loadUserFromDatabase() {
        Log.d(TAG, "Checking for logged-in user in Firebase...")
        val firebaseUser = authRepository.getCurrentUser()

        if (firebaseUser == null) {
            Log.w(TAG, "No user logged in Firebase. Cannot load local profile.")
            _currentUser.value = null
            return
        }

        val uid = firebaseUser.uid

        viewModelScope.launch {
            try {
                Log.d(TAG, "Looking for local profile with UID: $uid in Room")
                _currentUser.value = repository.getOneUser(uid)

                if (_currentUser.value != null) {
                    Log.i(TAG, "User profile successfully loaded from Room DB")
                } else {
                    Log.i(TAG, "Profile not found in Room for this UID. A new one will be created upon saving.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user from Room: ${e.message}")
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
        viewModelScope.launch {
            try {
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
                repository.addUser(newUser)
                _currentUser.value = newUser
            } catch (e: Exception) {
                Log.e(TAG, "Error saving initial user profile: ${e.message}")
            }
        }
    }


    private suspend fun saveOrUpdateUser(updateAction: (User) -> User, createAction: (String) -> User) {
        val firebaseUser = authRepository.getCurrentUser() ?: return
        val uid = firebaseUser.uid
        val currentLocalUser = _currentUser.value

        try {
            if (currentLocalUser != null) {
                val updatedUser = updateAction(currentLocalUser)
                repository.updateUser(updatedUser)
                _currentUser.value = updatedUser
            } else {
                val newUser = createAction(uid)
                repository.addUser(newUser)
                _currentUser.value = newUser
            }
        } catch (e: Exception) {
            Log.e(TAG, "Database operation failed: ${e.message}")
            throw e
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
        viewModelScope.launch {
            try {
                saveOrUpdateUser(
                    updateAction = { it.copy(
                        username = username,
                        dateOfBirth = dob,
                        address = address,
                        country = country,
                        phoneNumber = phone,
                        acceptReceiveEmails = acceptEmails
                    )},
                    createAction = { uid -> User(uid, username, dob, false, "en", address, country, phone, acceptEmails) }
                )
                onResult(ValidationResult(true, "Profile updated!"))
            } catch (e: Exception) {
                onResult(ValidationResult(false, "Update failed"))
            }
        }
    }

    fun saveNewUsername(newName: String, onResult: (ValidationResult) -> Unit) {
        val cleanedName = newName.trim()
        if (cleanedName.isBlank() || cleanedName.length < 3 || cleanedName.length > 20) {
            onResult(ValidationResult(false, "Username must be between 3 and 20 characters."))
            return
        }

        viewModelScope.launch {
            try {
                saveOrUpdateUser(
                    updateAction = { it.copy(username = cleanedName) },
                    createAction = { uid -> User(id = uid, username = cleanedName, dateOfBirth = LocalDate.now(), darkMode = false, language = "en", address = "", country = "", phoneNumber = "", acceptReceiveEmails = false) }
                )
                onResult(ValidationResult(true, "Username updated successfully!"))
            } catch (e: Exception) {
                onResult(ValidationResult(false, "Internal error updating username."))
            }
        }
    }

    fun saveNewDateOfBirth(newDate: LocalDate, onResult: (ValidationResult) -> Unit) {
        if (newDate.isAfter(LocalDate.now())) {
            onResult(ValidationResult(false, "Date of birth cannot be in the future."))
            return
        }

        viewModelScope.launch {
            try {
                saveOrUpdateUser(
                    updateAction = { it.copy(dateOfBirth = newDate) },
                    createAction = { uid -> User(id = uid, username = "", dateOfBirth = newDate, darkMode = false, language = "en", address = "", country = "", phoneNumber = "", acceptReceiveEmails = false) }
                )
                onResult(ValidationResult(true, "Date of birth updated successfully!"))
            } catch (e: Exception) {
                onResult(ValidationResult(false, "Internal error updating date."))
            }
        }
    }

    fun getDarkMode(): Boolean = _currentUser.value?.darkMode ?: false

    fun saveDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            try {
                saveOrUpdateUser(
                    updateAction = { it.copy(darkMode = isDark) },
                    createAction = { uid -> User(id = uid, username = "", dateOfBirth = LocalDate.now(), darkMode = isDark, language = "en", address = "", country = "", phoneNumber = "", acceptReceiveEmails = false) }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save dark mode", e)
            }
        }
    }

    fun getLanguage(): String = sharedPrefs.userLanguage

    fun saveLanguage(newLanguage: String) {
        viewModelScope.launch {
            try {
                saveOrUpdateUser(
                    updateAction = { it.copy(language = newLanguage) },
                    createAction = { uid -> User(id = uid, username = "", dateOfBirth = LocalDate.now(), darkMode = false, language = newLanguage, address = "", country = "", phoneNumber = "", acceptReceiveEmails = false) }
                )
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