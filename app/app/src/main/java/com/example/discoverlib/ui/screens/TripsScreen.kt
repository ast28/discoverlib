package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.data.MockData
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf


@Composable
fun TripsScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }

    val tripsList = remember { mutableStateListOf(*MockData.trips.toTypedArray()) }

    DiscoverScaffold(
        navController = navController,
        selectedSection = MainSection.TRIPS,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Trips", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tripsList) { trip ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("tripDetail/${trip.city}")
                                }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(trip.city, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    Text(trip.period)
                                    Text("Price: ${trip.budgetEur} EUR")
                                }
                                Button(
                                    onClick = { navController.navigate("tripDetail/${trip.city}") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(
                                            id = R.color.logo
                                        )
                                    )
                                ) {
                                    Text("View", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir viaje",
                    tint = Color.White
                )
            }

            if (showAddDialog) {
                AddTripDialog(
                    onDismiss = { showAddDialog = false },
                    onConfirm = { city, dates ->

                        val newTrip = MockData.trips.first().copy(
                            city = city,
                            period = dates,
                            budgetEur = 0
                        )

                        tripsList.add(newTrip)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddTripDialog(
    onDismiss: () -> Unit,
    onConfirm: (city: String, dates: String) -> Unit
) {
    var city by remember { mutableStateOf("") }
    var dates by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Trip", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City (e.g. Rome)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = dates,
                    onValueChange = { dates = it },
                    label = { Text("Dates (e.g. 10-04-2026 to 14-04-2026)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(city, dates) },
                enabled = city.isNotBlank() && dates.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    DiscoverlibTheme { TripsScreen(rememberNavController()) }
}