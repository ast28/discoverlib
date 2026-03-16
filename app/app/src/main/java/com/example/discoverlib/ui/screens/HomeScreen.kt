package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.navegation.Routes
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    val trips by viewModel.trips.collectAsState()

    val featuredTrip = remember(trips) {
        val today = LocalDate.now()
        trips
            .filter { !it.endDate.isBefore(today) }
            .minByOrNull { it.startDate }
    }

    var selectedDayIndex by rememberSaveable { mutableIntStateOf(0) }

    val dayLabels = listOf("lun 23", "mar 24", "mié 25", "jue 26", "vie 27")

    HomeScreenContent(
        navController = navController,
        featuredTrip = featuredTrip,
        dayLabels = dayLabels,
        selectedDayIndex = selectedDayIndex,
        onPrevDay = { if (selectedDayIndex > 0) selectedDayIndex-- },
        onNextDay = { if (selectedDayIndex < dayLabels.lastIndex) selectedDayIndex++ },
        onTripClick = {
            featuredTrip?.let {
                navController.navigate("tripDetail/${it.id}")
            } ?: navController.navigate(Routes.Trips)
        },
        onActivityClick = { activityId ->
            featuredTrip?.let {
                navController.navigate("activity/${it.id}/$activityId")
            }
        }
    )
}

@Composable
fun HomeScreenContent(
    navController: NavController,
    featuredTrip: Trip?,
    dayLabels: List<String>,
    selectedDayIndex: Int,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit,
    onTripClick: () -> Unit,
    onActivityClick: (String) -> Unit
) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.HOME) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(stringResource(id = R.string.home_title), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (featuredTrip != null) {
                NextTripSummaryCard(
                    trip = featuredTrip,
                    onClick = onTripClick
                )

                Spacer(modifier = Modifier.height(18.dp))

                DailyCalendarCard(
                    trip = featuredTrip,
                    selectedDay = dayLabels[selectedDayIndex],
                    canGoPrev = selectedDayIndex > 0,
                    canGoNext = selectedDayIndex < dayLabels.lastIndex,
                    onPrevDay = onPrevDay,
                    onNextDay = onNextDay,
                    onActivityClick = onActivityClick
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(id = R.string.home_no_trips), color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun NextTripSummaryCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(stringResource(id = R.string.home_next_trip), color = colorResource(id = R.color.logo), fontWeight = FontWeight.Bold)
                Text(trip.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("${trip.startDate} - ${trip.endDate}", fontSize = 13.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                val nights = java.time.temporal.ChronoUnit.DAYS.between(trip.startDate, trip.endDate)
                Text(stringResource(id = R.string.home_nights, nights), fontWeight = FontWeight.Bold)
                Text("${trip.budgetEur} EUR", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DailyCalendarCard(
    trip: Trip,
    selectedDay: String,
    canGoPrev: Boolean,
    canGoNext: Boolean,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit,
    onActivityClick: (String) -> Unit
) {
    val hours = listOf("08:00", "10:00", "12:00", "14:00", "16:00", "18:00")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val dayFormatter = DateTimeFormatter.ofPattern("E dd", Locale("es", "ES"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrevDay, enabled = canGoPrev) {
                    Text("<", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(id = R.string.home_daily_schedule), fontWeight = FontWeight.Bold, color = colorResource(id = R.color.logo))
                    Text("${trip.title} - $selectedDay", fontSize = 13.sp)
                }
                IconButton(onClick = onNextDay, enabled = canGoNext) {
                    Text(">", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            hours.forEach { hour ->
                val activity = trip.activities.firstOrNull {
                    val timeString = it.time.format(timeFormatter)
                    val dateString = it.date.format(dayFormatter).replace(".", "")
                    dateString.equals(selectedDay, ignoreCase = true) && timeString == hour
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hour,
                        modifier = Modifier.width(52.dp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(1.dp, Color(0x22FF3D00), RoundedCornerShape(8.dp))
                            .clickable(
                                enabled = activity != null,
                                onClick = {
                                    if (activity != null) onActivityClick(activity.id)
                                }
                            )
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (activity == null) {
                            Text(stringResource(id = R.string.home_no_activities), color = Color(0xFFB0B0B0), fontSize = 12.sp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = iconForCategory(activity.category)),
                                    contentDescription = activity.title,
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${activity.title} - ${activity.costEur} EUR", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun iconForCategory(category: ActivityCategory): Int {
    return when (category) {
        ActivityCategory.CULTURE -> R.drawable.monument
        ActivityCategory.FOOD -> R.drawable.restaurant
        ActivityCategory.NATURE -> R.drawable.forest
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DiscoverlibTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            featuredTrip = Trip(
                id = "1",
                title = "Roma",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(4),
                description = "Viaje a Roma",
                budgetEur = 500,
                activities = mutableListOf()
            ),
            dayLabels = listOf("lun 23", "mar 24", "mié 25", "jue 26", "vie 27"),
            selectedDayIndex = 0,
            onPrevDay = {},
            onNextDay = {},
            onTripClick = {},
            onActivityClick = {}
        )
    }
}