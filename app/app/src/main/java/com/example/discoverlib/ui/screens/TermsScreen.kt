package com.example.discoverlib.ui.screens

import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.discoverlib.navegation.Routes
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.TripViewModel

private const val TAG = "TermsScreen"

@Composable
fun TermsScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        Log.d(TAG, "TermsScreen initialized")
    }

    val terms = listOf(
        stringResource(id = R.string.terms_item_1_title) to stringResource(id = R.string.terms_item_1_desc),
        stringResource(id = R.string.terms_item_2_title) to stringResource(id = R.string.terms_item_2_desc),
        stringResource(id = R.string.terms_item_3_title) to stringResource(id = R.string.terms_item_3_desc),
        stringResource(id = R.string.terms_item_4_title) to stringResource(id = R.string.terms_item_4_desc)
    )

    TermsScreenContent(
        navController = navController,
        termsList = terms,
        onAccept = { 
            Log.d(TAG, "Terms accepted")
            navController.navigate(Routes.Home) 
        },
        onDecline = { 
            Log.d(TAG, "Terms declined")
            navController.navigate(Routes.Home) 
        }
    )
}

@Composable
fun TermsScreenContent(
    navController: NavController,
    termsList: List<Pair<String, String>>,
    onAccept: () -> Unit,
    onDecline: () -> Unit
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
            Text(stringResource(id = R.string.terms_title), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            termsList.forEachIndexed { index, item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("${index + 1}. ${item.first}", fontWeight = FontWeight.Bold)
                        Text(item.second)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onDecline,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) { Text(stringResource(id = R.string.terms_decline), color = MaterialTheme.colorScheme.onBackground) }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
                ) { Text(stringResource(id = R.string.terms_accept), color = Color.White) }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsScreenPreview() {
    DiscoverlibTheme {
        TermsScreenContent(
            navController = rememberNavController(),
            termsList = listOf(
                "Section 1" to "Example of terms and conditions text for preview.",
                "Section 2" to "Another section with more legal information."
            ),
            onAccept = {},
            onDecline = {}
        )
    }
}
