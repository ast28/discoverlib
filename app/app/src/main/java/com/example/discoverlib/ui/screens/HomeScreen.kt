package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold // Importación clave
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        // 1. BARRA SUPERIOR
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.logo))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Discoverlib",
                    fontSize = 15.sp, // Aquí controlas el tamaño (grande)
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            // Aquí va la acción que quieras ejecutar
                            navController.navigate("home")
                        }.padding(top = 8.dp)
                )
                /*IconButton(
                    onClick = { /* Acción */ },
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_color),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                }*/


                Spacer(modifier = Modifier.weight(1f))
                val iconosTop = listOf(
                    R.drawable.about_us,
                    R.drawable.settings
                )

                iconosTop.forEach { iconRes ->
                    IconButton(
                        onClick = { /* Acción */ },
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        },
        // 2. BARRA INFERIOR DE NAVEGACIÓN
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black.copy(alpha = 0.5f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val botones = listOf(
                        "Activities" to R.drawable.itinerari_search,
                        "Maps" to R.drawable.mapa,
                        "Gallery" to R.drawable.photos,
                        "Trips" to R.drawable.trips,
                        "Hotels" to R.drawable.hotel
                    )

                    botones.forEach { (label, iconRes) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { if (label == "Gallery") navController.navigate("gallery")},
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(colorResource(id = R.color.logo))
                            ) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = label,
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Unspecified
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label,
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        // 3. CONTENIDO PRINCIPAL (En medio de las dos barras)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Fondo general
                .padding(paddingValues) // MUY IMPORTANTE: Aplica el margen de las barras
                .padding(horizontal = 16.dp) // Márgenes laterales
        ) {
            Spacer(modifier = Modifier.height(25.dp)) // Espacio tras la barra superior

            Text(
                text = "Next trip ⮕ ROMA",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Text(
                text = "23-03-2026 to 28-03-2026",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            // --- TABLA SEMANAL ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colorResource(id = R.color.logo))
                    .background(Color.White)
            ) {
                // CABECERA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF8257).copy(alpha = 0.1f))
                        .border(0.5.dp, colorResource(id = R.color.logo))
                ) {
                    Spacer(modifier = Modifier.width(45.dp))

                    val diasSemana = listOf("Lun 23", "Mar 24", "Mie 25", "Jue 26", "Vie 27")
                    diasSemana.forEach { dia ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, colorResource(id = R.color.logo))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dia,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }
                }

                // CUERPO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val horas = listOf(8, 10, 12, 14, 16, 18)

                    horas.forEach { hora ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$hora:00",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(45.dp)
                                    .padding(end = 6.dp)
                            )

                            repeat(5) { columna ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .border(0.5.dp, colorResource(id = R.color.logo)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (columna == 0 && hora == 10) {
                                        Button(
                                            onClick = { /* Acción */ },
                                            modifier = Modifier.size(width = 40.dp, height = 30.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.Unspecified
                                            ),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.monument),
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                    if (columna == 3 && hora == 14) {
                                        Button(
                                            onClick = { /* Acción */ },
                                            modifier = Modifier.size(width = 40.dp, height = 30.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.Unspecified
                                            ),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.restaurant),
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                    if (columna == 2 && hora == 16) {
                                        Button(
                                            onClick = { /* Acción */ },
                                            modifier = Modifier.size(width = 40.dp, height = 30.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.Unspecified
                                            ),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.forest),
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Esto empuja el botón inferior hacia abajo del todo (por encima de la barra de navegación)
            Spacer(modifier = Modifier.height(90.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Separación de la barra de iconos inferior
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Acción para planear viaje */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.logo)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(
                        text = "Plan new trip",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DiscoverlibTheme {
        HomeScreen(navController = rememberNavController())
    }
}
