package com.example.discoverlib.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.data.MockData
import com.example.discoverlib.navegation.Routes
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme

@Composable
fun AboutScreen(navController: NavController) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.NONE) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_color),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(88.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("About", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Plan your adventures, your way.")
            Spacer(modifier = Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Team", fontWeight = FontWeight.Bold)
                    MockData.team.forEach { member ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = member.initials,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEEE8))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("  ${member.name} - ${member.role}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Technical info", fontWeight = FontWeight.Bold)
                    Text("Version 1.0.0")
                    Text("Sprint 01")
                    Text("Android min API 26")
                    Text("Kotlin 2.0.21")
                    Text("License: MIT")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate(Routes.Terms) },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo)),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Terms & Conditions", color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    DiscoverlibTheme { AboutScreen(rememberNavController()) }
}