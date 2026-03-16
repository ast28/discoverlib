package com.example.discoverlib.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.discoverlib.R
import com.example.discoverlib.ui.components.DiscoverScaffold
import com.example.discoverlib.ui.components.MainSection
import com.example.discoverlib.ui.theme.DiscoverlibTheme
import com.example.discoverlib.ui.viewmodels.UserViewModel
import java.time.LocalDate

@Composable
fun PreferencesScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val languagesDisplay = listOf("English", "Español", "Català")
    val languageCodes = listOf("en", "es", "ca")

    val currentLangCode = remember { userViewModel.getLanguage() }

    var langIndex by remember {
        val index = languageCodes.indexOf(currentLangCode)
        mutableIntStateOf(if (index >= 0) index else 0)
    }

    var reminders by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf(userViewModel.getSavedUsername()) }
    var dob by remember { mutableStateOf(userViewModel.getSavedDateOfBirth()) }

    var showProfileDialog by remember { mutableStateOf(false) }

    PreferencesScreenContent(
        navController = navController,
        isDarkTheme = isDarkTheme,
        onThemeChange = { newDarkTheme ->
            userViewModel.saveDarkMode(newDarkTheme)
            onThemeChange(newDarkTheme)
        },
        languages = languagesDisplay,
        langIndex = langIndex,
        onLangChange = { newIndex ->
            langIndex = newIndex
            val selectedCode = languageCodes[newIndex]
            userViewModel.saveLanguage(selectedCode)
        },
        reminders = reminders,
        onRemindersChange = { reminders = it },
        username = username,
        dob = dob,
        onEditProfileClick = { showProfileDialog = true }
    )

    if (showProfileDialog) {
        EditProfileDialog(
            initialName = username,
            initialDob = dob,
            onDismiss = { showProfileDialog = false },
            onConfirm = { newName, newDob ->
                if (newName.isNotBlank()) {
                    userViewModel.saveNewUsername(newName)
                    username = newName
                }
                try {
                    val parsedDate = LocalDate.parse(newDob)
                    if (userViewModel.saveNewDateOfBirth(parsedDate)) {
                        dob = newDob
                    }
                } catch (e: Exception) { }
                showProfileDialog = false
            }
        )
    }
}

@Composable
fun PreferencesScreenContent(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    languages: List<String>,
    langIndex: Int,
    onLangChange: (Int) -> Unit,
    reminders: Boolean,
    onRemindersChange: (Boolean) -> Unit,
    username: String,
    dob: String,
    onEditProfileClick: () -> Unit
) {
    DiscoverScaffold(navController = navController, selectedSection = MainSection.SETTINGS) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.settings_title),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileHeaderCard(
                username = username,
                dob = dob,
                onEditClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.settings_app_title),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.logo),
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    PreferenceDropdown(
                        title = stringResource(id = R.string.language),
                        subtitle = stringResource(id = R.string.language_subtitle),
                        options = languages,
                        selectedIndex = langIndex,
                        onOptionSelected = onLangChange
                    )

                    PreferenceToggle(
                        title = stringResource(id = R.string.dark_mode),
                        subtitle = stringResource(id = R.string.dark_mode_subtitle),
                        checked = isDarkTheme,
                        onCheckedChange = onThemeChange
                    )

                    PreferenceToggle(
                        title = stringResource(id = R.string.notifications_title),
                        subtitle = stringResource(id = R.string.notifications_subtitle),
                        checked = reminders,
                        onCheckedChange = onRemindersChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileHeaderCard(
    username: String,
    dob: String,
    onEditClick: () -> Unit
) {
    val displayUsername = if (username.contains("Not defined") || username.isBlank()) {
        stringResource(id = R.string.profile_default_name)
    } else username

    val displayDob = if (dob.contains("Not defined") || dob.isBlank()) {
        "--"
    } else dob

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayUsername,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.logo)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = displayDob,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(colorResource(id = R.color.logo).copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.profile_edit_title),
                    tint = colorResource(id = R.color.logo)
                )
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    initialName: String,
    initialDob: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var nameText by remember { mutableStateOf(if (initialName.contains("Not defined")) "" else initialName) }
    var dobText by remember { mutableStateOf(if (initialDob.contains("Not defined")) "" else initialDob) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.profile_edit_title), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text(stringResource(id = R.string.settings_username)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dobText,
                    onValueChange = { dobText = it },
                    label = { Text(stringResource(id = R.string.settings_dob)) },
                    placeholder = { Text(stringResource(id = R.string.dialog_dob_format)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nameText, dobText) },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.logo))
            ) { Text("Save", color = Color.White) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = R.color.logo))
            ) { Text("Cancel", fontWeight = FontWeight.Bold) }
        }
    )
}

@Composable
fun PreferenceDropdown(
    title: String,
    subtitle: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x22FF3D00), RoundedCornerShape(10.dp))
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
        }

        Box(contentAlignment = Alignment.TopEnd) {
            Text(
                text = options[selectedIndex],
                color = colorResource(id = R.color.logo),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                color = if (index == selectedIndex) colorResource(id = R.color.logo) else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onOptionSelected(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PreferenceToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x22FF3D00), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(id = R.color.logo),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    DiscoverlibTheme {
        PreferencesScreenContent(
            navController = rememberNavController(),
            isDarkTheme = false,
            onThemeChange = {},
            languages = listOf("English", "Español", "Català"),
            langIndex = 0,
            onLangChange = {},
            reminders = false,
            onRemindersChange = {},
            username = "Alba Senar",
            dob = "2005-03-24",
            onEditProfileClick = {}
        )
    }
}