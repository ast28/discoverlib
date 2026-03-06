package com.example.discoverlib.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColorScheme = lightColorScheme(
    primary = BrandOrange,
    secondary = BrandOrange,
    tertiary = BrandOrange,
    background = SurfaceLight,
    surface = SurfaceLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandOrange,
    secondary = BrandOrange,
    tertiary = BrandOrange,
    background = SurfaceDark,
    surface = SurfaceDark,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun DiscoverlibTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}