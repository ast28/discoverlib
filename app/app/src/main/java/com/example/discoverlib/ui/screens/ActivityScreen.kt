package com.example.discoverlib.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TripActivity
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ActivityScreen(
    navController: NavController,
    activityId: String?,
    viewModel: TripViewModel = hiltViewModel()
) {
    val activity = viewModel.getSavedTrips()
        .flatMap { it.activities }
        .find { it.id == activityId }

    if (activity != null) {
        ActivityScreenContent(
            navController = navController,
            activity = activity,
            onBackClick = { navController.popBackStack() }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Activity not found")
                Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Go back")
                }
            }
        }
    }
}

@Composable
fun ActivityScreenContent(
    navController: NavController,
    activity: TripActivity,
    onBackClick: () -> Unit
) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.NONE) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
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
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver atrás",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = activity.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${activity.description}\n\nDuration: 2h\nPrice: ${activity.costEur} EUR\nLocation: ${activity.location}",
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = activity.photo),
                    contentDescription = "Photo",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = activity.photo_maps),
                    contentDescription = "Map",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    DiscoverlibTheme {
        ActivityScreenContent(
            navController = rememberNavController(),
            activity = TripActivity(
                id = "1",
                title = "Louvre Museum",
                description = "Visit the world's largest art museum and a historic monument in Paris.",
                location = "Paris, France",
                date = LocalDate.now(),
                time = LocalTime.of(10, 0),
                category = ActivityCategory.CULTURE,
                costEur = 25,
                photo = R.drawable.logo_color,
                photo_maps = R.drawable.logo_color
            ),
            onBackClick = {}
        )
    }
}