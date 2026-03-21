package com.example.discoverlib.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.discoverlib.domain.Trip
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel

private const val TAG = "GalleryScreen"

@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    val trips by viewModel.trips.collectAsState()

    LaunchedEffect(Unit) {
        Log.d(TAG, "GalleryScreen initialized")
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedTripId by remember(trips) {
        mutableStateOf(trips.firstOrNull()?.id ?: "")
    }

    val selectedTrip = trips.find { it.id == selectedTripId }

    val photos = when {
        selectedTrip?.title?.equals("Rome", ignoreCase = true) == true ||
                selectedTrip?.title?.equals("Roma", ignoreCase = true) == true -> {
            List(30) { R.drawable.photo }
        }
        selectedTrip?.title?.equals("London", ignoreCase = true) == true ||
                selectedTrip?.title?.equals("Paris", ignoreCase = true) == true -> {
            emptyList()
        }
        else -> {
            selectedTrip?.activities?.mapNotNull { it.photo } ?: emptyList()
        }
    }

    val sizeMb = (photos.size * 2.5).toString()
    val gallerySummary = stringResource(id = R.string.gallery_summary, photos.size, sizeMb)

    GalleryScreenContent(
        navController = navController,
        expanded = expanded,
        selectedTripName = selectedTrip?.title ?: stringResource(id = R.string.gallery_no_trips),
        photos = photos,
        gallerySummary = gallerySummary,
        trips = trips,
        onExpandedChange = { 
            Log.d(TAG, "Trip selector expanded: $it")
            expanded = it 
        },
        onTripSelected = {
            Log.d(TAG, "Trip selected for gallery: ${it.id} (${it.title})")
            selectedTripId = it.id
            expanded = false
        },
        onAddPhotosClick = {
            Log.d(TAG, "Add photos button clicked")
        },
        onDeletePhotoClick = {
            Log.d(TAG, "Delete photo clicked")
        }
    )
}

@Composable
fun GalleryScreenContent(
    navController: NavController,
    expanded: Boolean,
    selectedTripName: String,
    photos: List<Int>,
    gallerySummary: String,
    trips: List<Trip>,
    onExpandedChange: (Boolean) -> Unit,
    onTripSelected: (Trip) -> Unit,
    onAddPhotosClick: () -> Unit,
    onDeletePhotoClick: () -> Unit
) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.GALLERY) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(stringResource(id = R.string.gallery_title), fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text(gallerySummary, fontSize = 14.sp, color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Row(
                        modifier = Modifier.clickable { onExpandedChange(true) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTripName,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                        trips.forEach { trip ->
                            DropdownMenuItem(
                                text = { Text(trip.title) },
                                onClick = { onTripSelected(trip) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (photos.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val chunks = photos.chunked(3)
                    items(chunks) { rowPhotos ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPhotos.forEach { photoRes ->
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = photoRes),
                                        contentDescription = "Photo",
                                        modifier = Modifier
                                            .size(90.dp)
                                    )
                                    IconButton(onClick = onDeletePhotoClick) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.delete),
                                            contentDescription = stringResource(id = R.string.gallery_delete_desc),
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                            repeat(3 - rowPhotos.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(id = R.string.gallery_no_photos), color = Color.Gray)
                }
            }

            Button(
                onClick = onAddPhotosClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) { Text(stringResource(id = R.string.gallery_add_btn), color = Color.White) }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryPreview() {
    DiscoverlibTheme {
        GalleryScreenContent(
            navController = rememberNavController(),
            expanded = false,
            selectedTripName = "Roma",
            photos = emptyList(),
            gallerySummary = "0 photos - 0 MB",
            trips = emptyList(),
            onExpandedChange = {},
            onTripSelected = {},
            onAddPhotosClick = {},
            onDeletePhotoClick = {}
        )
    }
}
