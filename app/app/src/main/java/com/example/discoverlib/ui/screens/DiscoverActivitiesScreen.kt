package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.MockActivity
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Composable
fun DiscoverActivitiesScreen(
    navController: NavController,
    tripId: String,
    viewModel: TripViewModel = hiltViewModel()
) {
    val tripsList by viewModel.trips.collectAsState()
    val trip = remember(tripsList, tripId) { tripsList.find { it.id == tripId } }

    val allSuggestedActivities = remember { viewModel.getSuggestedActivities() }

    val categories = listOf("All", "Tours", "Museums", "Food", "Nature")
    var selectedCategory by remember { mutableStateOf("All") }

    var showDialog by remember { mutableStateOf(false) }
    var prefilledActivity by remember { mutableStateOf<MockActivity?>(null) }

    val filteredActivities = if (selectedCategory == "All") {
        allSuggestedActivities
    } else {
        allSuggestedActivities.filter { it.category == selectedCategory }
    }

    if (trip == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(id = R.string.discover_trip_not_found), color = Color.Gray)
        }
        return
    }

    DiscoverActivitiesContent(
        navController = navController,
        cityName = trip.title,
        categories = categories,
        selectedCategory = selectedCategory,
        filteredActivities = filteredActivities,
        onCategorySelected = { category -> selectedCategory = category },
        onBackClick = { navController.popBackStack() },
        onAddSuggestedClick = { activity ->
            prefilledActivity = activity
            showDialog = true
        },
        onAddCustomClick = {
            prefilledActivity = null
            showDialog = true
        }
    )

    if (showDialog) {
        ActivityFormDialog(
            prefilledData = prefilledActivity,
            tripStartDate = trip.startDate,
            tripEndDate = trip.endDate,
            onDismiss = {
                showDialog = false
                prefilledActivity = null
            },
            onConfirm = { newActivity ->
                viewModel.addActivity(tripId, newActivity)
                showDialog = false
                prefilledActivity = null
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun DiscoverActivitiesContent(
    navController: NavController,
    cityName: String,
    categories: List<String>,
    selectedCategory: String,
    filteredActivities: List<MockActivity>,
    onCategorySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onAddSuggestedClick: (MockActivity) -> Unit,
    onAddCustomClick: () -> Unit
) {
    DiscoverScaffold(
        navController = navController,
        selectedSection = MainSection.TRIPS
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.discover_title, cityName),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

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
                            modifier = Modifier.clickable { onCategorySelected(category) }
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredActivities) { activity ->
                        ActivityCard(activity = activity) {
                            onAddSuggestedClick(activity)
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = onAddCustomClick,
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Custom Activity", tint = Color.White)
            }
        }
    }
}

@Composable
fun ActivityCard(activity: MockActivity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("📷", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = activity.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = colorResource(id = R.color.logo), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${activity.rating}", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "• ${activity.category}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${activity.priceEur} EUR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(stringResource(id = R.string.discover_add_btn), fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ActivityFormDialog(
    prefilledData: MockActivity?,
    tripStartDate: LocalDate,
    tripEndDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (TripActivity) -> Unit
) {
    var title by remember { mutableStateOf(prefilledData?.title ?: "") }
    var location by remember { mutableStateOf(prefilledData?.location ?: "") }
    var description by remember { mutableStateOf(prefilledData?.description ?: "") }
    var cost by remember { mutableStateOf(prefilledData?.priceEur?.toString() ?: "") }

    var selectedCategoryStr by remember { mutableStateOf(prefilledData?.category ?: "Culture") }

    var dateText by remember { mutableStateOf(tripStartDate.toString()) }
    var timeText by remember { mutableStateOf("10:00") }

    val parsedDate = try { LocalDate.parse(dateText) } catch (e: Exception) { null }
    val parsedTime = try { LocalTime.parse(timeText) } catch (e: Exception) { null }

    val isTitleError = title.isBlank()
    val isCostError = cost.toIntOrNull() == null
    val isLocationError = location.isBlank()
    val isDateError = parsedDate == null || parsedDate.isBefore(tripStartDate) || parsedDate.isAfter(tripEndDate)
    val isTimeError = parsedTime == null

    val isFormValid = !isTitleError && !isCostError && !isLocationError && !isDateError && !isTimeError

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (prefilledData == null) stringResource(id = R.string.form_new_custom)
                else stringResource(id = R.string.form_schedule),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(id = R.string.form_title)) },
                    isError = isTitleError,
                    singleLine = true
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(stringResource(id = R.string.form_location)) },
                    isError = isLocationError,
                    singleLine = true
                )

                OutlinedTextField(
                    value = selectedCategoryStr,
                    onValueChange = { selectedCategoryStr = it },
                    label = { Text(stringResource(id = R.string.form_category)) },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(id = R.string.form_description)) },
                    singleLine = false,
                    maxLines = 3
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = { dateText = it },
                        label = { Text(stringResource(id = R.string.form_date)) },
                        isError = isDateError,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeText,
                        onValueChange = { timeText = it },
                        label = { Text(stringResource(id = R.string.form_time)) },
                        isError = isTimeError,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (isDateError && parsedDate != null) {
                    Text(
                        stringResource(id = R.string.form_date_error, tripStartDate.toString(), tripEndDate.toString()),
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text(stringResource(id = R.string.form_cost)) },
                    isError = isCostError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        val finalCategory = when (selectedCategoryStr.lowercase()) {
                            "food" -> ActivityCategory.FOOD
                            "nature" -> ActivityCategory.NATURE
                            else -> ActivityCategory.CULTURE
                        }

                        val newActivity = TripActivity(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            location = location,
                            date = parsedDate!!,
                            time = parsedTime!!,
                            category = finalCategory,
                            costEur = cost.toIntOrNull() ?: 0,
                            photo = R.drawable.logo_color,
                            photo_maps = R.drawable.logo_color
                        )
                        onConfirm(newActivity)
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.form_save), color = Color.White) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.form_cancel), fontWeight = FontWeight.Bold) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DiscoverActivitiesPreview() {
    DiscoverlibTheme {
        DiscoverActivitiesContent(
            navController = rememberNavController(),
            cityName = "Roma",
            categories = listOf("All", "Tours", "Museums", "Food", "Nature"),
            selectedCategory = "All",
            filteredActivities = emptyList(),
            onCategorySelected = {},
            onBackClick = {},
            onAddSuggestedClick = {},
            onAddCustomClick = {}
        )
    }
}