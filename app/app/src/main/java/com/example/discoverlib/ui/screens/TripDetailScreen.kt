package com.example.discoverlib.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.ValidationResult
import com.example.discoverlib.ui.components.ActivityFormDialog
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.toDisplayDate
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun TripDetailScreen(
    navController: NavController,
    tripId: String?,
    viewModel: TripViewModel = hiltViewModel()
) {
    val tripsList by viewModel.trips.collectAsState()

    val trip = tripsList.find { it.id == tripId }

    if (trip != null) {
        TripDetailContent(
            navController = navController,
            trip = trip,
            onBackClick = { navController.popBackStack() },
            onActivityClick = { activityId -> navController.navigate("activity/$tripId/$activityId") },
            onSaveActivity = { newActivity, onResult ->
                // Añadimos el bloque del callback { result -> }
                viewModel.addActivity(tripId = trip.id, activity = newActivity) { result ->
                    if (result.isSuccessful) {
                        Log.i("TripDetailScreen", "Activity added successfully to trip ${trip.id}")
                    } else {
                        Log.e("TripDetailScreen", "Error adding activity: ${result.message}")
                    }
                    onResult(result)
                    // Aquí iría el scope.launch { snackbarHostState... } si lo necesitas
                }
            },
            onDeleteActivity = { activityId, onResult ->
                viewModel.deleteOneActivity(tripId = trip.id, activityId = activityId) { result ->
                    if (result.isSuccessful) {
                        Log.i("TripDetailScreen", "Activity $activityId deleted successfully")
                    } else {
                        Log.e("TripDetailScreen", "Error deleting activity: ${result.message}")
                    }
                    onResult(result)
                }
            }
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
    onActivityClick: (String) -> Unit,
    onSaveActivity: (TripActivity, (ValidationResult) -> Unit) -> Unit,
    onDeleteActivity: (String, (ValidationResult) -> Unit) -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val activityAddedMessage = stringResource(id = R.string.snackbar_activity_added)
    val activityDeletedMessage = stringResource(id = R.string.snackbar_activity_deleted)

    val catAll = stringResource(id = R.string.cat_all)
    val catTransport = stringResource(id = R.string.cat_transport)
    val catTours = stringResource(id = R.string.cat_tours)
    val catCulture = stringResource(id = R.string.cat_culture)
    val catFood = stringResource(id = R.string.cat_food)
    val catNature = stringResource(id = R.string.cat_nature)

    val categories = listOf(catAll, catTransport, catTours, catCulture, catFood, catNature)
    var selectedCategory by remember { mutableStateOf(catAll) }

    var showAddDialog by remember { mutableStateOf(false) }

    var activityToDelete by remember { mutableStateOf<TripActivity?>(null) }

    val filteredActivities = (if (selectedCategory == catAll) {
        trip.activities
    } else {
        trip.activities.filter { activity ->
            when (selectedCategory) {
                catTransport -> activity.category == ActivityCategory.TRANSPORT
                catTours -> activity.category == ActivityCategory.TOURS
                catCulture -> activity.category == ActivityCategory.CULTURE
                catFood -> activity.category == ActivityCategory.FOOD
                catNature -> activity.category == ActivityCategory.NATURE
                else -> false
            }
        }
    }).sortedWith(compareBy({ it.date }, { it.time }))

    DiscoverScaffold(
        navController = navController,
        selectedSection = MainSection.TRIPS,
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(30.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.trip_detail_back),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = trip.title,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "${trip.startDate.toDisplayDate()} - ${trip.endDate.toDisplayDate()}",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val nights = java.time.temporal.ChronoUnit.DAYS.between(trip.startDate, trip.endDate)
                    val totalCost = trip.activities.sumOf { it.costEur }
                    StatCard("$nights", stringResource(id = R.string.trip_detail_nights), Modifier.weight(1f))
                    StatCard("$totalCost EUR", stringResource(id = R.string.trip_detail_price), Modifier.weight(1f))
                    StatCard("${trip.activities.size}", stringResource(id = R.string.trip_detail_activities), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.trip_detail_itinerary),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF3D00),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) colorResource(id = R.color.logo) else Color.LightGray.copy(alpha = 0.3f),
                            modifier = Modifier.clickable { selectedCategory = category }
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredActivities.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(id = R.string.trip_detail_no_activities), color = Color.Gray)
                    }
                } else {
                    filteredActivities.forEach { activity ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable { onActivityClick(activity.id) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    val dateLabel = activity.date.toDisplayDate()
                                    val timeLabel = activity.time.format(timeFormatter)
                                    Text("$dateLabel - $timeLabel", color = Color.Gray, fontSize = 13.sp)
                                    Text(activity.title, fontWeight = FontWeight.Bold)
                                    Text(activity.location, fontSize = 13.sp)
                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("${activity.costEur} EUR", fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.width(4.dp))

                                    IconButton(
                                        onClick = { activityToDelete = activity },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Activity",
                                            tint = Color.Red.copy(alpha = 0.7f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
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
                    contentDescription = stringResource(id = R.string.trip_detail_add_btn),
                    tint = Color.White
                )
            }
        }

        if (showAddDialog) {
            ActivityFormDialog(
                initialActivity = null,
                tripStartDate = trip.startDate,
                tripEndDate = trip.endDate,
                onDismiss = { showAddDialog = false },
                existingActivities = trip.activities,
                onConfirm = { title, location, description, date, time, costEur, category ->
                    val newActivity = TripActivity(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        location = location,
                        date = date,
                        time = time,
                        category = category,
                        costEur = costEur,
                        photo = R.drawable.logo_color,
                        photo_maps = R.drawable.logo_color
                    )

                    onSaveActivity(newActivity) { result ->
                        if (result.isSuccessful) {
                            coroutineScope.launch { snackbarHostState.showSnackbar(activityAddedMessage) }
                            showAddDialog = false
                            selectedCategory = catAll
                        }
                    }
                }
            )
        }

        activityToDelete?.let { activity ->
            AlertDialog(
                onDismissRequest = { activityToDelete = null },
                title = {
                    Text(text = "Delete Activity", fontWeight = FontWeight.Bold)
                },
                text = {
                    Text("Are you sure you want to delete '${activity.title}'?\nThis action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteActivity(activity.id) { result ->
                                if (result.isSuccessful) {
                                    coroutineScope.launch { snackbarHostState.showSnackbar(activityDeletedMessage) }
                                }
                                activityToDelete = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A4A4A))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activityToDelete = null }) {
                        Text("Cancel", color = colorResource(id = R.color.logo), fontWeight = FontWeight.Bold)
                    }
                }
            )
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
