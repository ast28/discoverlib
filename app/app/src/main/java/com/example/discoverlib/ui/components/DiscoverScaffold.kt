package com.example.discoverlib.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.discoverlib.R
import com.example.discoverlib.navegation.Routes

private data class BottomItem(
    val label: String,
    val iconRes: Int,
    val route: String?,
    val section: MainSection
)

enum class MainSection {
    HOME,
    TRIPS,
    GALLERY,
    SETTINGS,
    NONE
}

@Composable
fun DiscoverScaffold(
    navController: NavController,
    selectedSection: MainSection,
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.logo))
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateSingleTop(Routes.Home) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Home",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Discoverlib",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigateSingleTop(Routes.Home) }
                        .padding(top = 4.dp)
                )
                IconButton(onClick = { navController.navigateSingleTop(Routes.About) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.about_us),
                        contentDescription = "About",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val items = listOf(
                        BottomItem("Home", R.drawable.ic_home, Routes.Home, MainSection.HOME),
                        BottomItem(
                            "Trips",
                            R.drawable.ic_trips,
                            Routes.Trips,
                            MainSection.TRIPS
                        ),
                        BottomItem(
                            "Gallery",
                            R.drawable.ic_gallery,
                            Routes.Gallery,
                            MainSection.GALLERY
                        ),
                        BottomItem("Settings", R.drawable.ic_settings, Routes.Preferences, MainSection.SETTINGS)
                    )

                    items.forEach { item ->
                        val isSelected = selectedSection == item.section

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            BottomCircleNavButton(
                                isSelected = isSelected,
                                onClick = { item.route?.let { navController.navigateSingleTop(it) } }
                            ) {
                                Icon(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.label,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = item.label,
                                color = if (isSelected) colorResource(id = R.color.logo) else MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        content = content
    )
}

@Composable
private fun BottomCircleNavButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonColor = when {
        isPressed -> Color(0xFF8A2200)
        isSelected -> Color(0xFFB22A00)
        else -> colorResource(id = R.color.logo)
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(buttonColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun NavController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}
