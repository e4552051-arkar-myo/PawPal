package uk.ac.tees.mad.e4552051.pawpal.ui.screens.vets

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import uk.ac.tees.mad.e4552051.pawpal.data.Secrets
import uk.ac.tees.mad.e4552051.pawpal.data.places.PlacesApi
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar

@SuppressLint("MissingPermission")
@Composable
fun VetFinderScreen(onNavigateBack: () -> Unit) {

    val TAG = "VetFinder"
    val context = LocalContext.current

    // Permission state
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Request permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasLocationPermission = it
    }

    // Request permission at start
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // User Location
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fusedClient.lastLocation.addOnSuccessListener {
                userLocation = it
                Log.d(TAG, "Got user location = $it")
            }
        }
    }

    // Vet results
    var vetMarkers by remember { mutableStateOf<List<Pair<String, LatLng>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // API call for nearby vets
    LaunchedEffect(userLocation) {
        if (userLocation == null) return@LaunchedEffect

        isLoading = true
        errorMessage = null

        try {
            val loc = "${userLocation!!.latitude},${userLocation!!.longitude}"

            Log.d(TAG, "Calling Places API with loc=$loc")

            val response = PlacesApi.service.getNearbyVets(
                location = loc,
                radius = 3500,
                type = "veterinary_care",
                apiKey = Secrets.PLACES_API_KEY
            )

//            if (response.status != "OK") {
//                errorMessage = "Places API error: ${response.status}"
//                Log.e(TAG, "API error: ${response.status}")
//                isLoading = false
//                return@LaunchedEffect
//            }

            vetMarkers = response.results.mapNotNull { r ->
                try {
                    val lat = r.geometry?.location?.lat
                    val lng = r.geometry?.location?.lng
                    if (lat != null && lng != null) {
                        r.name to LatLng(lat, lng)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Places API crashed", e)
            errorMessage = "Failed to load vet locations."
        }

        isLoading = false
    }

    // Camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation?.let { LatLng(it.latitude, it.longitude) }
                ?: LatLng(54.5742, -1.2350),
            13f
        )
    }

    // UI
    Scaffold(
        topBar = { AppTopBar("Nearby Vets") }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            // MAP 50%
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = hasLocationPermission
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true,
                        zoomControlsEnabled = true
                    )
                ) {
                    // User marker
                    userLocation?.let {
                        Marker(
                            state = rememberMarkerState(
                                position = LatLng(it.latitude, it.longitude)
                            ),
                            title = "You are here"
                        )
                    }

                    // Vet markers
                    vetMarkers.forEach { (name, pos) ->
                        Marker(
                            state = rememberMarkerState(pos.toString()),
                            title = name
                        )
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }

            // LIST 50%
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Nearby Veterinary Clinics",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vetMarkers) { (name, pos) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("google.navigation:q=${pos.latitude},${pos.longitude}")
                                    )
                                    intent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(intent)
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(name, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Tap to navigate",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Back")
            }
        }
    }
}