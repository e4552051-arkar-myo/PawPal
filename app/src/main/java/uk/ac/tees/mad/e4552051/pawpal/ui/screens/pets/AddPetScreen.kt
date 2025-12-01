package uk.ac.tees.mad.e4552051.pawpal.ui.screens.pets

import android.content.Intent
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

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddPetScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }


    // Gallery picker launcher
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            imageUri = uri.toString()
        }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            imageUri = cameraImageUri.toString()
        }
    }

    fun createImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val imageFileName = "JPEG_${timeStamp}_.jpg"
        val storageDir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(storageDir, imageFileName)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

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

            // IMAGE PLACEHOLDER / PREVIEW
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    }
                ) {
                    Text("Gallery")
                }

                OutlinedButton(
                    onClick = {
                        val uri = createImageUri(context)
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                    }
                ) {
                    Text("Camera")
                }
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

            // SAVE PET BUTTON
            Button(
                onClick = {
                    if (petName.isNotBlank() && petType.isNotBlank() && petAge.isNotBlank()) {
                        viewModel.addPet(
                            name = petName,
                            type = petType,
                            age = petAge.toIntOrNull() ?: 0,
                            imageUri = imageUri
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Pet")
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