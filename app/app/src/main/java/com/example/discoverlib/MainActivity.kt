package com.example.discoverlib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.discoverlib.ui.screens.HomeScreen.HomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Instalar Splash ANTES de todo
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState) // 2. Luego el super
        enableEdgeToEdge()

        // 3. Configurar la lógica de espera
        var isChecking = true
        lifecycleScope.launch {
            delay(3000L)
            isChecking = false
        }

        splashScreen.setKeepOnScreenCondition { isChecking }

        // 4. Cargar el contenido
        setContent {
            DiscoverlibTheme {
                HomeScreen(onGoToItinerary = { })
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiscoverlibTheme {
        Greeting("Android")
    }
}