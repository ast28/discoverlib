package com.example.discoverlib.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
    tripId: String?,
    activityId: String?,
    viewModel: TripViewModel = hiltViewModel()
) {
    val activity = if (tripId != null && activityId != null) {
        viewModel.getSavedActivity(tripId, activityId)
    } else {
        null
    }

    if (activity != null) {
        ActivityScreenContent(
            navController = navController,
            activity = activity,
            onBackClick = { navController.popBackStack() }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(id = R.string.activity_not_found), color = Color.Gray, fontSize = 18.sp)
                Text(stringResource(id = R.string.activity_error_subtitle), color = Color.Gray, fontSize = 14.sp)
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
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = activity.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 36.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            val priceLabel = stringResource(id = R.string.activity_price)
            val locationLabel = stringResource(id = R.string.activity_location)

            Text(
                text = "${activity.description}\n\n$priceLabel ${activity.costEur} EUR\n$locationLabel ${activity.location}",
                textAlign = TextAlign.Justify,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            ActivityPhotosSection(activity)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ActivityPhotosSection(activity: TripActivity) {
    val photoPlaceholderId = R.drawable.logo_color

    val hasSpecificPhoto = activity.photo != photoPlaceholderId
    val hasSpecificMap = activity.photo_maps != photoPlaceholderId

    Text(
        text = stringResource(id = R.string.activity_photos_title),
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFF3D00),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasSpecificPhoto) {
            Image(
                painter = painterResource(id = activity.photo),
                contentDescription = "Photo of ${activity.title}",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            ProfessionalPhotoPlaceholder(
                icon = Icons.Default.Image,
                text = stringResource(id = R.string.activity_photos_unavailable),
                modifier = Modifier.weight(1f)
            )
        }

        if (hasSpecificMap) {
            Image(
                painter = painterResource(id = activity.photo_maps),
                contentDescription = "Map of ${activity.location}",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            ProfessionalPhotoPlaceholder(
                icon = Icons.Default.Map,
                text = stringResource(id = R.string.activity_map_unavailable),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProfessionalPhotoPlaceholder(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    DiscoverlibTheme {
        ActivityScreenContent(
            navController = rememberNavController(),
            activity = TripActivity(
                id = "1",
                title = "Villa Borghese Bike Rental",
                description = "Relaxing bike ride through Rome's biggest and most beautiful public park.",
                location = "Rome, Italy",
                date = LocalDate.now(),
                time = LocalTime.of(10, 0),
                category = ActivityCategory.NATURE,
                costEur = 15,
                photo = R.drawable.photo_villa,
                photo_maps = R.drawable.villa_maps
            ),
            onBackClick = {}
        )
    }
}