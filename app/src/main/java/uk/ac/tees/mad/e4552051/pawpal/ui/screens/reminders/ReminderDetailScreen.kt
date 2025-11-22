package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.PetViewModel

@Composable
fun PetDetailScreen(
    pet: PetEntity,
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf(pet.name) }
    var type by remember { mutableStateOf(pet.type) }
    var age by remember { mutableStateOf(pet.age.toString()) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppTopBar("Edit Pet") }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type") })
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.updatePet(
                        pet.copy(
                            name = name,
                            type = type,
                            age = age.toIntOrNull() ?: pet.age
                        )
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Pet")
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete Pet") },
                    text = { Text("Are you sure you want to delete this pet?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deletePet(pet)
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