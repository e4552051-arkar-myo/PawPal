package uk.ac.tees.mad.e4552051.pawpal.ui.screens.addpet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar

@Composable
fun AddPetScreen(onNavigateBack: () -> Unit) {

    // Local states for fields (Sprint 2 placeholders)
    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar("Add Pet") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // IMAGE PLACEHOLDER BLOCK
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Pet Image\n(placeholder)", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { /* TODO: Select Image */ }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // INPUT FIELDS
            OutlinedTextField(
                value = petName,
                onValueChange = { petName = it },
                label = { Text("Pet Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = petType,
                onValueChange = { petType = it },
                label = { Text("Pet Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = petAge,
                onValueChange = { petAge = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ADD PET BUTTON (Action Placeholder)
            Button(
                onClick = { /* TODO: Save to DB in Sprint 3 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Pet")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Back Button
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}