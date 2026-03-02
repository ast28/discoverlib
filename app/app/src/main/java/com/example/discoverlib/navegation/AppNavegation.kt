package com.example.discoverlib.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.ui.screens.GalleryScreen
import com.example.discoverlib.ui.screens.HomeScreen
import com.example.discoverlib.ui.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("gallery") { GalleryScreen(navController) }
    }
}