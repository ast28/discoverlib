package com.example.discoverlib.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun GalleryScreen(navController: NavController) {
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
                        "Gallery" to R.drawable.photos_selected,
                        "Trips" to R.drawable.trips,
                        "Hotels" to R.drawable.hotel
                    )

                    botones.forEach { (label, iconRes) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(color = if (label.equals("Gallery")) Color(0xFF8257).copy(alpha = 1f) else colorResource(id = R.color.logo))
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
                text = "Gallery",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )

            var expanded by remember { mutableStateOf(false) }
            var selectedTrip by remember { mutableStateOf("London") }
            val trips = listOf("London", "Paris", "Roma")

            Text(
                if(selectedTrip == "Roma") "O items" else "102 items",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )

            //Spacer(modifier = Modifier.height(10.dp))



            Box(
                modifier = Modifier
                    .clickable { expanded = true } // Al hacer clic, se abre
                    .padding(vertical = 8.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = "$selectedTrip ▾",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                // 2. El menú que aparece "flotando"
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    trips.forEach { trip ->
                        DropdownMenuItem(
                            text = { Text(trip) },
                            onClick = {
                                selectedTrip = trip
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            val scrollState = rememberScrollState()

            // FOTOOOSSS
            if (selectedTrip != "Roma") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .verticalScroll(scrollState)
                ) {
                    repeat(10) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Repetimos 3 veces para las 3 columnas de imagen + icono
                            repeat(3) {
                                Column(
                                    modifier = Modifier.weight(1f), // El peso lo lleva ahora la columna
                                    horizontalAlignment = Alignment.CenterHorizontally // Centra el icono bajo la foto
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.photo),
                                        contentDescription = "Foto",
                                        modifier = Modifier.size(90.dp)
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.delete),
                                        contentDescription = "Icono",
                                        modifier = Modifier
                                            .size(20.dp) // Tamaño pequeño para el icono
                                            .padding(top = 4.dp), // Separación de la foto
                                        tint = Color.Unspecified // Color opcional
                                    )
                                }
                            }
                        }
                    }

                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // <--- También le damos peso aquí
                    verticalArrangement = Arrangement.Center, // Centra verticalmente
                    horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
                ) {
                    Text("No photos found for Roma", color = Color.Gray)
                }
            }

            // Esto empuja el botón inferior hacia abajo del todo (por encima de la barra de navegación)
            Spacer(modifier = Modifier.height(60.dp))

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
                        text = "Add photos",
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
fun GalleryPreview() {
    DiscoverlibTheme { // Es bueno añadir el tema para ver los colores reales
        GalleryScreen (navController = rememberNavController())
    }
}