package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit,
    onAddReminder: () -> Unit,
    onReminderClick: (ReminderEntity) -> Unit
) {
    val reminders by viewModel.reminders.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { AppTopBar("Reminders") },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddReminder) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Upcoming Reminders", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(reminders) { reminder ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onReminderClick(reminder) }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(reminder.petName, style = MaterialTheme.typography.titleMedium)
                            Text(reminder.reminderType, style = MaterialTheme.typography.bodyMedium)
                            reminder.note?.let {
                                Text(it, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}