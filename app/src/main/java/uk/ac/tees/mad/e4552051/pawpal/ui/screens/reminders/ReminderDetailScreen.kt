package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel

@Composable
fun ReminderDetailScreen(
    reminderId: Int,
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit
) {
    var reminder by remember { mutableStateOf<ReminderEntity?>(null) }

    LaunchedEffect(reminderId) {
        reminder = viewModel.getReminderById(reminderId)
    }

    if (reminder == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        var petName by remember { mutableStateOf(reminder!!.petName) }
        var type by remember { mutableStateOf(reminder!!.reminderType) }
        var note by remember { mutableStateOf(reminder!!.note ?: "") }
        var showDeleteDialog by remember { mutableStateOf(false) }

        Scaffold(
            topBar = { AppTopBar("Edit Reminder") }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(petName, { petName = it }, label = { Text("Pet name") })
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(type, { type = it }, label = { Text("Reminder type") })
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(note, { note = it }, label = { Text("Notes") })
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.updateReminder(
                            reminder!!.copy(
                                petName = petName,
                                reminderType = type,
                                note = note.ifBlank { null }
                            )
                        )
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Reminder")
                }

                // Back Button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Delete Reminder") },
                        text = { Text("Are you sure you want to delete this reminder?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteReminder(reminder!!)
                                showDeleteDialog = false
                                onNavigateBack()
                            }) { Text("Delete") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}