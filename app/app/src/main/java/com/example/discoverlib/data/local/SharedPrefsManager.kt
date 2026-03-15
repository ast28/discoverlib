package com.example.discoverlib.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.discoverlib.utils.LanguageChangeUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.getValue


@Singleton // Hilt se asegura de que solo exista uno [cite: 1595]
class SharedPrefsManager @Inject constructor(
    private val preferences: SharedPreferences,
    @ApplicationContext private val context: Context // [cite: 1596]
) {
    val languageChangeUtil by lazy { LanguageChangeUtil() }

    var username: String
        get() = preferences.getString("username", "") ?: ""
        set(value) = preferences.edit().putString("username", value).apply()

    var dateOfBirth: String
        get() = preferences.getString("dob", "") ?: ""
        set(value) = preferences.edit().putString("dob", value).apply()

    var darkTheme: Boolean
        get() = preferences.getBoolean("dark_theme", false)
        set(value) = preferences.edit().putBoolean("dark_theme", value).apply()

    var userLanguage: String
        get() = preferences.getString("user_language", "en") ?: "en"
        set(value) {
            preferences.edit().putString("user_language", value).apply()
            languageChangeUtil.changeLanguage(context, value)
        }
}