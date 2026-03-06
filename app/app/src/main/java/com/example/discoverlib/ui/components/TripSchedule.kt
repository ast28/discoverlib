package com.example.discoverlib.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.discoverlib.R
import com.example.discoverlib.domain.ActivityCategory
import com.example.discoverlib.domain.TripActivity

@Composable
fun TripSchedule(
    activities: List<TripActivity>,
    onActivityClick: () -> Unit
) {
    val dayColumns = listOf("Lun 23", "Mar 24", "Mie 25", "Jue 26", "Vie 27")
    val hours = listOf("08:00", "10:00", "12:00", "14:00", "16:00", "18:00")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colorResource(id = R.color.logo))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF3D00).copy(alpha = 0.1f))
                .border(0.5.dp, colorResource(id = R.color.logo))
        ) {
            Spacer(modifier = Modifier.width(45.dp))
            dayColumns.forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, colorResource(id = R.color.logo))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }

        hours.forEach { hour ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = hour,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                dayColumns.forEach { day ->
                    val activity = activities.firstOrNull { it.dayLabel == day && it.time == hour }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .border(0.5.dp, colorResource(id = R.color.logo))
                            .padding(2.dp)
                            .clickable(enabled = activity != null, onClick = onActivityClick),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activity != null) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = iconForCategory(activity.category)),
                                    contentDescription = activity.title,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text("${activity.costEur} EUR", fontSize = 7.sp, fontWeight = FontWeight.Bold)
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