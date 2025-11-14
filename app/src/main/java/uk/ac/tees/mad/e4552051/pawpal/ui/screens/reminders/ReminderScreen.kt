package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar

@Composable
fun ReminderScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = { AppTopBar("Reminders") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Upcoming Reminders", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            // Back Button
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}