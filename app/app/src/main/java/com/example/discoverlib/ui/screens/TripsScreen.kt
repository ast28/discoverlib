package com.example.discoverlib.ui.screens

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
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import java.time.LocalDate

@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var tripToEdit by remember { mutableStateOf<Trip?>(null) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    val tripsList by viewModel.trips.collectAsState()

    // --- NUEVO: Ordenamos los viajes por fecha de inicio ---
    val sortedTrips = tripsList.sortedBy { it.startDate }

    TripsScreenContent(
        navController = navController,
        tripsList = sortedTrips, // Le pasamos la lista ya ordenada
        showDialog = showDialog,
        tripToEdit = tripToEdit,
        onAddClick = {
            tripToEdit = null
            showDialog = true
        },
        onEditClick = { trip ->
            tripToEdit = trip
            showDialog = true
        },
        onDeleteClick = { trip -> tripToDelete = trip },
        onDismissDialog = {
            showDialog = false
            tripToEdit = null
        },
        onConfirmTrip = { title, start, end ->
            if (tripToEdit != null) {
                val updated = tripToEdit!!.copy(
                    title = title,
                    startDate = start,
                    endDate = end
                )
                viewModel.saveEditedTrip(updated)
            } else {
                val newTrip = Trip(
                    id = java.util.UUID.randomUUID().toString(),
                    title = title,
                    startDate = start,
                    endDate = end,
                    description = "Trip to $title",
                    budgetEur = 0,
                    activities = mutableListOf()
                )
                viewModel.saveNewTrip(newTrip)
            }
            showDialog = false
            tripToEdit = null
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
                        viewModel.saveDeletedTrip(tripToDelete!!.id)
                        tripToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) { Text(stringResource(id = R.string.dialog_delete_btn), color = Color.White) }
            },
            dismissButton = {
                TextButton(
                    onClick = { tripToDelete = null },
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
    onAddClick: () -> Unit,
    onEditClick: (Trip) -> Unit,
    onDeleteClick: (Trip) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmTrip: (String, LocalDate, LocalDate) -> Unit
) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.TRIPS) { paddingValues ->
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
                                    Text("${trip.startDate} - ${trip.endDate}", color = Color.Gray)

                                    val totalCost = trip.activities.sumOf { it.costEur }
                                    Text("${stringResource(id = R.string.trips_budget_prefix)} $totalCost EUR", fontWeight = FontWeight.Medium)
                                }
                                Box {
                                    IconButton(onClick = { showMenu = true }) {
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
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onDismissDialog = {},
            onConfirmTrip = { _, _, _ -> }
        )
    }
}