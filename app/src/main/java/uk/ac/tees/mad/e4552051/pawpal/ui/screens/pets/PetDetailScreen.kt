package uk.ac.tees.mad.e4552051.pawpal.ui.screens.pets

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.PetViewModel
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PetDetailScreen(
    pet: PetEntity,
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var petName by remember { mutableStateOf(pet.name) }
    var petType by remember { mutableStateOf(pet.type) }
    var petAge by remember { mutableStateOf(pet.age.toString()) }
    var imageUri by remember { mutableStateOf(pet.imageUri) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it.toString()
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            imageUri = cameraImageUri.toString()
        }
    }

    fun createImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())
        val fileName = "PET_EDIT_${timeStamp}.jpg"
        val dir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(dir, fileName)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    Scaffold(
        topBar = { AppTopBar("Edit Pet") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- IMAGE PREVIEW ---
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .border(1.dp, Color.Gray, CircleShape)
                        .background(Color(0xFFEFEFEF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Pet Image\n(placeholder)",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- GALLERY & CAMERA BUTTONS ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Gallery")
                }

                OutlinedButton(onClick = {
                    val uri = createImageUri(context)
                    cameraImageUri = uri
                    cameraLauncher.launch(uri)
                }) {
                    Text("Camera")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT FIELDS ---
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

            // --- SAVE CHANGES ---
            Button(
                onClick = {
                    viewModel.updatePet(
                        pet.copy(
                            name = petName,
                            type = petType,
                            age = petAge.toIntOrNull() ?: pet.age,
                            imageUri = imageUri
                        )
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- DELETE BUTTON ---
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Pet")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- BACK BUTTON ---
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }

            // --- DELETE CONFIRMATION DIALOG ---
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
                        }) {
                            Text("Delete")
                        }
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