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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.data.MockData
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.navegation.Routes
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun HomeScreen(navController: NavController) {
    val dayLabels = listOf("Mon 23", "Tue 24", "Wed 25", "Thu 26", "Fri 27")
    var selectedDayIndex by rememberSaveable { mutableIntStateOf(0) }

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
            Text("Home", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            NextTripSummaryCard(
                trip = MockData.featuredTrip,
                onClick = { navController.navigate(Routes.TripDetail) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            DailyCalendarCard(
                trip = MockData.featuredTrip,
                selectedDay = dayLabels[selectedDayIndex],
                canGoPrev = selectedDayIndex > 0,
                canGoNext = selectedDayIndex < dayLabels.lastIndex,
                onPrevDay = { selectedDayIndex-- },
                onNextDay = { selectedDayIndex++ },
                onActivityClick = { activityId ->
                    navController.navigate("activity/$activityId/false")
                }
            )

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
            .fillMaxWidth(),
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
                Text("Next trip", color = colorResource(id = R.color.logo), fontWeight = FontWeight.Bold)
                Text(trip.city, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(trip.period, fontSize = 13.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${trip.nights} nights", fontWeight = FontWeight.Bold)
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
                    Text("Daily Schedule", fontWeight = FontWeight.Bold, color = colorResource(id = R.color.logo))
                    Text("${trip.city} - $selectedDay", fontSize = 13.sp)
                }
                IconButton(onClick = onNextDay, enabled = canGoNext) {
                    Text(">", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            hours.forEach { hour ->
                val activity = trip.activities.firstOrNull { it.dayLabel == selectedDay && it.time == hour }
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
                            Text("No activities", color = Color(0xFFB0B0B0), fontSize = 12.sp)
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
    DiscoverlibTheme { HomeScreen(rememberNavController()) }
}

