package uk.ac.tees.mad.e4552051.pawpal.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar

@Composable
fun HomeScreen(
    onNavigateToAddPet: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    Scaffold(
        topBar = { AppTopBar("PawPal") },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddPet) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Your Pets", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(2) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Sample Pet $it",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons for Reminders and Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onNavigateToReminders) {
                    Text("Reminders")
                }
                Button(onClick = onNavigateToSettings) {
                    Text("Settings")
                }
            }
        }
    }
}