package com.example.discoverlib.ui.screens

import com.example.discoverlib.R
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.discoverlib.data.MockData
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun TripDetailScreen(navController: NavController, cityName: String?) {
    val trip = MockData.trips.find { it.city == cityName } ?: MockData.featuredTrip

    DiscoverScaffold(navController = navController, selectedSection = MainSection.TRIPS) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),

                ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    contentAlignment = androidx.compose.ui.Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver atrás",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }
                Text(trip.city, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Text(trip.period)
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("${trip.nights}", "NIGHTS", Modifier.weight(1f))
                    StatCard("${trip.budgetEur} EUR", "PRICE", Modifier.weight(1f))
                    StatCard("${trip.activities.size}", "ACTIVITIES", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Itinerary",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF3D00)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))

                trip.activities.forEach { activity ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp).clickable {
                                navController.navigate("activity/${activity.id}")
                            },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "${activity.dayLabel} - ${activity.time}",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                                Text(activity.title, fontWeight = FontWeight.Bold)
                                Text(activity.location, fontSize = 13.sp)
                            }
                            Text("${activity.costEur} EUR", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            FloatingActionButton(
                onClick = { navController.navigate("discoverActivities/${trip.city}") },
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir actividad",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailScreenPreview() {
    DiscoverlibTheme { TripDetailScreen(rememberNavController(), "Roma") }
}
