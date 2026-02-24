package com.example.discoverlib.ui.screens.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.discoverlib.R
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun HomeScreen(onGoToItinerary: () -> Unit) { // Eliminamos @Composable del parámetro para evitar errores
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.logo))
                .padding(20.dp),
        ) {
            /*Text(
                text = "DISCOVERLIB",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif, // Cambia Serif por Cursive o Monospace
                fontWeight = FontWeight.Bold
            )*/
            /*Image(
                painter = painterResource(id = R.drawable.logo_color),
                contentDescription = "Logo Discoverlib",
                modifier = Modifier.size(50.dp)
            )*/
        }

        /*IconButton(onClick = { /* Acción */ }) {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Botón About Us",
                modifier = Modifier.size(25.dp),
                tint = Color.Unspecified
            )
        }*/

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Next trip ⮕ ROMA",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif, // Cambia Serif por Cursive o Monospace
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Text(
                text = "23-03-2026 to 28-03-2026",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif, // Cambia Serif por Cursive o Monospace
                fontSize = 15.sp
            )

        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // Lo centra horizontalmente
        ) {
            Button(
                onClick = { /* Acción para planear viaje */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.logo) // Tu naranja
                ),
                shape = RoundedCornerShape(10.dp), // Bordes muy redondeados
                modifier = Modifier.padding(bottom = 50.dp).height(56.dp)
            ) {
                Text(
                    text = "Plan new trip",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.5f) // Línea sutil
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(width = 1.dp, color = Color.White)
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly, // Distribuye los 5 botones por igual
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lista de nombres e iconos para tus 5 botones
                val botones = listOf(
                    "Activities" to R.drawable.itinerari_search,
                    "Maps" to R.drawable.mapa, // Cambia por tus iconos reales
                    "Photos" to R.drawable.photos,
                    "Trips" to R.drawable.trips,
                    "Suggestion" to R.drawable.suggestions
                )

                botones.forEach { (label, iconRes) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { /* Acción */ },
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                // Si el fondo de la barra es blanco, quizás quieras el botón
                                // de un color suave (como el naranja logo) para que resalte
                                .background(colorResource(id = R.color.logo))
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = label,
                                modifier = Modifier.size(28.dp),
                                tint = Color.Unspecified // Mantiene los colores originales de tus XML
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = label,
                            color = Color.Black, // Texto ahora en negro
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DiscoverlibTheme { // Es bueno añadir el tema para ver los colores reales
        HomeScreen(onGoToItinerary = { /* Dejar vacío para el preview */ })
    }
}