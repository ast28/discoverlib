package com.example.discoverlib.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.domain.ValidationResult
import com.example.discoverlib.ui.components.ActivityFormDialog
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.viewmodels.TripViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

private const val TAG = "ActivityScreen"

@Composable
fun ActivityScreen(
    navController: NavController,
    tripId: String?,
    activityId: String?,
    viewModel: TripViewModel = hiltViewModel()
) {
    val trips by viewModel.trips.collectAsState()

    val trip = trips.find { it.id == tripId }
    val activity = trip?.activities?.find { it.id == activityId }

    when {
        trip != null && activity != null -> {
            ActivityScreenContent(
                navController = navController,
                activity = activity,
                tripStartDate = trip.startDate,
                tripEndDate = trip.endDate,
                onBackClick = { navController.popBackStack() },
                onSaveEdit = { editedActivity, onValidationResult ->
                    viewModel.updateActivity(tripId = trip.id, activity = editedActivity) { result ->
                        onValidationResult(result)
                    }
                }
            )
        }

        trips.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(id = R.color.logo))
            }
        }

        else -> {
            Log.e("ActivityScreen", "Error: Activity or Trip not found after data load. tripId=$tripId, activityId=$activityId")

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(id = R.string.activity_not_found), color = Color.Gray, fontSize = 18.sp)
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(top = 24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
                    ) {
                        Text(stringResource(id = R.string.activity_go_back), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityScreenContent(
    navController: NavController,
    activity: TripActivity,
    tripStartDate: LocalDate,
    tripEndDate: LocalDate,
    onBackClick: () -> Unit,
    onSaveEdit: (TripActivity, (ValidationResult) -> Unit) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val activityUpdatedMessage = stringResource(id = R.string.snackbar_activity_updated)

    DiscoverScaffold(
        navController = navController,
        selectedSection = MainSection.TRIPS,
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                IconButton(onClick = onBackClick, modifier = Modifier.size(30.dp)) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = activity.title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Información con labels y datos en negrita usando tus Strings
                val priceLabel = stringResource(id = R.string.activity_price)
                val locationLabel = stringResource(id = R.string.activity_location)

                Text(
                    text = buildAnnotatedString {
                        append(activity.description)
                        append("\n\n")
                        append("$priceLabel ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${activity.costEur} EUR")
                        }
                        append("\n")
                        append("$locationLabel ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(activity.location)
                        }
                    },
                    textAlign = TextAlign.Justify,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(40.dp))

                ActivityPhotosSection(activity)

                Spacer(modifier = Modifier.height(100.dp))
            }

            FloatingActionButton(
                onClick = { showEditDialog = true },
                containerColor = colorResource(id = R.color.logo),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }

        if (showEditDialog) {
            ActivityFormDialog(
                initialActivity = activity,
                tripStartDate = tripStartDate,
                tripEndDate = tripEndDate,
                onDismiss = { showEditDialog = false },
                existingActivities = emptyList(),
                onConfirm = { title, location, description, date, time, costEur, category ->
                    val edited = activity.copy(
                        title = title, location = location, description = description,
                        date = date, time = time, costEur = costEur, category = category
                    )
                    onSaveEdit(edited) { result ->
                        if (result.isSuccessful) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(activityUpdatedMessage)
                            }
                            showEditDialog = false
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(result.message)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ActivityPhotosSection(activity: TripActivity) {
    Text(
        text = stringResource(id = R.string.activity_photos_title),
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFF3D00),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val photoMod = Modifier.weight(1f).fillMaxSize().clip(RoundedCornerShape(12.dp))

        if (activity.photo != R.drawable.logo_color) {
            Image(painter = painterResource(id = activity.photo), contentDescription = null, modifier = photoMod, contentScale = ContentScale.Crop)
        } else {
            PhotoPlaceholder(Icons.Default.Image, stringResource(id = R.string.activity_photos_unavailable), Modifier.weight(1f))
        }

        if (activity.photo_maps != R.drawable.logo_color) {
            Image(painter = painterResource(id = activity.photo_maps), contentDescription = null, modifier = photoMod, contentScale = ContentScale.Crop)
        } else {
            PhotoPlaceholder(Icons.Default.Map, stringResource(id = R.string.activity_map_unavailable), Modifier.weight(1f))
        }
    }
}

@Composable
private fun PhotoPlaceholder(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
            Text(text = text, color = Color.Gray, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}
