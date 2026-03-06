package com.example.discoverlib.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.ui.screens.GalleryScreen
import com.example.discoverlib.ui.screens.HomeScreen
import com.example.discoverlib.ui.screens.SplashScreen
import com.example.discoverlib.ui.screens.ActivityScreen
import com.example.discoverlib.ui.screens.TripsScreen
import com.example.discoverlib.ui.screens.TripDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) { SplashScreen(navController) }
        composable(Routes.Home) { HomeScreen(navController) }
        composable(Routes.Trips) { TripsScreen(navController) }
        composable(Routes.TripDetail) { TripDetailScreen(navController) }
        composable(Routes.Activity) { ActivityScreen(navController) }
        composable(Routes.Gallery) { GalleryScreen(navController) }
        composable(Routes.Preferences) { PreferencesScreen(navController) }
        composable(Routes.About) { AboutScreen(navController) }
        composable(Routes.Terms) { TermsScreen(navController) }
    }
}