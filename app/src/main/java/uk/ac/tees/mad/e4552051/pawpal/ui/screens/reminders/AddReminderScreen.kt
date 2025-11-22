package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel

@Composable
fun AddReminderScreen(
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit
) {
    var petName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar("Add Reminder") }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            OutlinedTextField(value = petName, onValueChange = { petName = it }, label = { Text("Pet Name") })
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Reminder Type") })
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") })
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.addReminder(
                        petName = petName,
                        reminderType = type,
                        note = note
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Reminder")
            }
        }
    }
}