package uk.ac.tees.mad.e4552051.pawpal.ui.screens.home
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.PetViewModel

@Composable
fun HomeScreen(
    viewModel: PetViewModel,
    onNavigateToAddPet: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onPetClick: (PetEntity) -> Unit,
    onNavigateToVets: () -> Unit

) {
    val petList by viewModel.pets.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            AppTopBar(
                title = "PawPal",
                onNavigateToReminders = onNavigateToReminders,
                onNavigateToSettings = onNavigateToSettings
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddPet) {
                Icon(Icons.Default.Add, contentDescription = "Add Pet")
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
            Button(
                onClick = { onNavigateToVets() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Find Nearby Vets")
            }

            if (petList.isEmpty()) {
                Text("No pets yet. Tap + to add your first pet!")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(petList, key = { it.id }) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { onPetClick(pet) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PetCard(
    pet: PetEntity,
    onClick: (PetEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(pet) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (pet.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(pet.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pet.name.first().uppercase(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(pet.name, style = MaterialTheme.typography.titleLarge)
                Text("Type: ${pet.type}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Age: ${pet.age}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}