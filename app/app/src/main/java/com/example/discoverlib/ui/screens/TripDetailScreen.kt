package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TripDetailScreen(
    navController: NavController,
    tripId: String?,
    viewModel: TripViewModel = hiltViewModel()
) {
    val trip = viewModel.getSavedOneTrip(tripId?: "")

    if (trip != null) {
        TripDetailContent(
            navController = navController,
            trip = trip,
            onBackClick = { navController.popBackStack() },
            onAddActivityClick = { navController.navigate("discoverActivities/${trip.id}") },
            onActivityClick = { activityId -> navController.navigate("activity/$tripId/$activityId") }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(id = R.string.trip_detail_not_found))
        }
    }
}

@Composable
fun TripDetailContent(
    navController: NavController,
    trip: Trip,
    onBackClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    onActivityClick: (String) -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("E dd", Locale("es", "ES"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    DiscoverScaffold(navController = navController, selectedSection = MainSection.TRIPS) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.trip_detail_back),
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(trip.title, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Text("${trip.startDate} - ${trip.endDate}")
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val nights = java.time.temporal.ChronoUnit.DAYS.between(trip.startDate, trip.endDate)
                    StatCard("$nights", stringResource(id = R.string.trip_detail_nights), Modifier.weight(1f))
                    StatCard("${trip.budgetEur} EUR", stringResource(id = R.string.trip_detail_price), Modifier.weight(1f))
                    StatCard("${trip.activities.size}", stringResource(id = R.string.trip_detail_activities), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(id = R.string.trip_detail_itinerary),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF3D00)
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))

                trip.activities.forEach { activity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onActivityClick(activity.id) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                val dateLabel = activity.date.format(dayFormatter).replace(".", "")
                                val timeLabel = activity.time.format(timeFormatter)
                                Text("$dateLabel - $timeLabel", color = Color.Gray, fontSize = 13.sp)
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
                onClick = onAddActivityClick,
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.trip_detail_add_btn),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailScreenPreview() {
    DiscoverlibTheme {
        TripDetailContent(
            navController = rememberNavController(),
            trip = Trip(
                id = "1",
                title = "Roma",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(4),
                description = "Trip to Rome",
                budgetEur = 600,
                activities = mutableListOf()
            ),
            onBackClick = {},
            onAddActivityClick = {},
            onActivityClick = {}
        )
    }
}