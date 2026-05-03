package com.example.discoverlib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.navegation.AppNavigation
import com.example.discoverlib.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            val userViewModel: UserViewModel = hiltViewModel()
            val user by userViewModel.currentUser.collectAsState()

            val isDarkTheme = user?.darkMode ?: isSystemInDarkTheme()

            DiscoverlibTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { nuevoTema ->
                        userViewModel.saveDarkMode(nuevoTema)
                    }
                )
            }
        }
    }
}