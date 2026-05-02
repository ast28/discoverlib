package com.example.discoverlib.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.components.TripFormDialog
import com.example.discoverlib.ui.toDisplayDate
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "TripsScreen"

@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var tripToEdit by remember { mutableStateOf<Trip?>(null) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val tripsList by viewModel.trips.collectAsState()
    val tripAddedMessage = stringResource(id = R.string.snackbar_trip_added)
    val tripUpdatedMessage = stringResource(id = R.string.snackbar_trip_updated)
    val tripDeletedMessage = stringResource(id = R.string.snackbar_trip_deleted)

    LaunchedEffect(Unit) {
        Log.d(TAG, "TripsScreen initialized")
    }

    val sortedTrips = tripsList.sortedBy { it.startDate }

    TripsScreenContent(
        navController = navController,
        tripsList = sortedTrips,
        showDialog = showDialog,
        tripToEdit = tripToEdit,
        snackbarHostState = snackbarHostState,
        onAddClick = {
            Log.d(TAG, "Add trip button clicked")
            tripToEdit = null
            showDialog = true
        },
        onEditClick = { trip ->
            Log.d(TAG, "Edit trip clicked for: ${trip.id}")
            tripToEdit = trip
            showDialog = true
        },
        onDeleteClick = { trip ->
            Log.d(TAG, "Delete trip clicked for: ${trip.id}")
            tripToDelete = trip
        },
        onDismissDialog = {
            Log.d(TAG, "Trip form dialog dismissed")
            showDialog = false
            tripToEdit = null
        },
        onConfirmTrip = { title, start, end ->
            if (tripToEdit != null) {
                Log.d(TAG, "Confirming trip edit for: ${tripToEdit!!.id}")
                val updated = tripToEdit!!.copy(
                    title = title,
                    startDate = start,
                    endDate = end
                )

                viewModel.updateTrip(updated) { result ->
                    if (result.isSuccessful) {
                        Log.i(TAG, "Trip edited successfully")
                        coroutineScope.launch { snackbarHostState.showSnackbar(tripUpdatedMessage) }
                        showDialog = false
                        tripToEdit = null
                    } else {
                        Log.e(TAG, "Error editing trip: ${result.message}")
                    }
                }
            } else {
                Log.d(TAG, "Confirming new trip creation: $title")
                val newTrip = Trip(
                    id = java.util.UUID.randomUUID().toString(),
                    userId = "",
                    title = title,
                    startDate = start,
                    endDate = end,
                    description = "Trip to $title",
                    budgetEur = 0,
                    activities = mutableListOf()
                )

                viewModel.addTrip(newTrip) { result ->
                    if (result.isSuccessful) {
                        Log.i(TAG, "Trip created successfully")
                        coroutineScope.launch { snackbarHostState.showSnackbar(tripAddedMessage) }
                        showDialog = false
                        tripToEdit = null
                    } else {
                        Log.e(TAG, "Error creating trip: ${result.message}")
                    }
                }
            }
        }
    )

    if (tripToDelete != null) {
        AlertDialog(
            onDismissRequest = { tripToDelete = null },
            title = { Text(stringResource(id = R.string.dialog_delete_trip_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(id = R.string.dialog_delete_trip_desc, tripToDelete?.title ?: "")) },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d(TAG, "Confirming deletion of trip: ${tripToDelete!!.id}")

                        viewModel.deleteTrip(tripToDelete!!.id) { success ->
                            if (success) {
                                Log.i(TAG, "Trip deleted successfully")
                                coroutineScope.launch { snackbarHostState.showSnackbar(tripDeletedMessage) }
                            } else {
                                Log.e(TAG, "Error: Trip not found during deletion")
                            }
                            tripToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) { Text(stringResource(id = R.string.dialog_delete_btn), color = Color.White) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        Log.d(TAG, "Deletion canceled")
                        tripToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.logo))
                ) { Text(stringResource(id = R.string.dialog_cancel_btn), fontWeight = FontWeight.Bold) }
            }
        )
    }
}

@Composable
fun TripsScreenContent(
    navController: NavController,
    tripsList: List<Trip>,
    showDialog: Boolean,
    tripToEdit: Trip?,
    snackbarHostState: SnackbarHostState,
    onAddClick: () -> Unit,
    onEditClick: (Trip) -> Unit,
    onDeleteClick: (Trip) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmTrip: (String, LocalDate, LocalDate) -> Unit
) {
    DiscoverScaffold(
        navController = navController,
        selectedSection = MainSection.TRIPS,
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(stringResource(id = R.string.trips_title), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tripsList) { trip ->
                        var showMenu by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                Log.d(TAG, "Navigating to TripDetail: ${trip.id}")
                                navController.navigate("tripDetail/${trip.id}")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(trip.title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                    Text("${trip.startDate.toDisplayDate()} - ${trip.endDate.toDisplayDate()}", color = Color.Gray)

                                    val totalCost = trip.activities.sumOf { it.costEur }
                                    Text("${stringResource(id = R.string.trips_budget_prefix)} $totalCost EUR", fontWeight = FontWeight.Medium)
                                }
                                Box {
                                    IconButton(onClick = {
                                        Log.d(TAG, "Options menu opened for trip: ${trip.id}")
                                        showMenu = true
                                    }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.trips_options_desc))
                                    }
                                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(id = R.string.trips_edit_action)) },
                                            leadingIcon = { Icon(Icons.Default.Edit, null) },
                                            onClick = { showMenu = false; onEditClick(trip) }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(id = R.string.trips_delete_action), color = Color.Red) },
                                            leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
                                            onClick = { showMenu = false; onDeleteClick(trip) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = onAddClick,
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) { Icon(Icons.Default.Add, null, tint = Color.White) }

            if (showDialog) {
                TripFormDialog(
                    initialTrip = tripToEdit,
                    onDismiss = onDismissDialog,
                    existingTrips = tripsList,
                    onConfirm = onConfirmTrip
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    DiscoverlibTheme {
        TripsScreenContent(
            navController = rememberNavController(),
            tripsList = emptyList(),
            showDialog = false,
            tripToEdit = null,
            snackbarHostState = SnackbarHostState(),
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onDismissDialog = {},
            onConfirmTrip = { _, _, _ -> }
        )
    }
}