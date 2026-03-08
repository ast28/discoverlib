package com.example.discoverlib.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.discoverlib.ui.screens.AboutScreen
import com.example.discoverlib.ui.screens.GalleryScreen
import com.example.discoverlib.ui.screens.HomeScreen
import com.example.discoverlib.ui.screens.SplashScreen
import com.example.discoverlib.ui.screens.ActivityScreen
import com.example.discoverlib.ui.screens.DiscoverActivitiesScreen
import com.example.discoverlib.ui.screens.PreferencesScreen
import com.example.discoverlib.ui.screens.TermsScreen
import com.example.discoverlib.ui.screens.TripsScreen
import com.example.discoverlib.ui.screens.TripDetailScreen

@Composable
fun AppNavigation(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) { SplashScreen(navController) }
        composable(Routes.Home) { HomeScreen(navController) }
        composable(Routes.Trips) { TripsScreen(navController) }
        composable(
            route = Routes.TripDetail,
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName")
            TripDetailScreen(navController, cityName)
        }
        composable(
            route = Routes.Activity,
            arguments = listOf(
                navArgument("activityId") { type = NavType.StringType },
                navArgument("showBack") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId")
            val showBack = backStackEntry.arguments?.getBoolean("showBack") ?: false
            ActivityScreen(navController, activityId, showBack)
        }
        composable(Routes.Gallery) { GalleryScreen(navController) }
        composable(Routes.Preferences) {
            PreferencesScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
        composable(Routes.About) { AboutScreen(navController) }
        composable(Routes.Terms) { TermsScreen(navController) }
        composable(Routes.DiscoverActivities) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Roma"
            DiscoverActivitiesScreen(
                navController = navController,
                cityName = city
            )
        }
    }
}