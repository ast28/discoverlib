package com.example.discoverlib.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.domain.TeamMember
import com.example.discoverlib.navegation.Routes
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel

private const val TAG = "AboutScreen"

@Composable
fun AboutScreen(navController: NavController, viewModel: TripViewModel = hiltViewModel()) {
    val teamMembers = viewModel.getTeam()

    LaunchedEffect(Unit) {
        Log.d(TAG, "AboutScreen initialized")
    }

    AboutScreenContent(
        navController = navController,
        teamList = teamMembers,
        onTermsClick = { 
            Log.d(TAG, "Terms and Conditions button clicked")
            navController.navigate(Routes.Terms) 
        }
    )
}

@Composable
fun AboutScreenContent(
    navController: NavController,
    teamList: List<TeamMember>,
    onTermsClick: () -> Unit
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
            Image(
                painter = painterResource(id = R.drawable.logo_color),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(88.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(stringResource(id = R.string.about_title), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(stringResource(id = R.string.about_subtitle))

            Spacer(modifier = Modifier.height(15.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(id = R.string.about_team), fontWeight = FontWeight.Bold)

                    teamList.forEach { member ->
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
                    Text(stringResource(id = R.string.about_tech_info), fontWeight = FontWeight.Bold)
                    Text(stringResource(id = R.string.about_version))
                    Text(stringResource(id = R.string.about_api))
                    Text(stringResource(id = R.string.about_kotlin))
                    Text(stringResource(id = R.string.about_license))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onTermsClick,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo)),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(stringResource(id = R.string.about_terms), color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    DiscoverlibTheme {
        AboutScreenContent(
            navController = rememberNavController(),
            teamList = listOf(
                TeamMember("AS", "Alba Senar Tejedor", "Developer")
            ),
            onTermsClick = {}
        )
    }
}
