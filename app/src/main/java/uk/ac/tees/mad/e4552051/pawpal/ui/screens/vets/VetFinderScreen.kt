package uk.ac.tees.mad.e4552051.pawpal.ui.screens.vets

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val context = LocalContext.current

    // Permission
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // User location
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fusedClient.lastLocation.addOnSuccessListener {
                userLocation = it
            }
        }
    }

    // Nearby vet results
    var vetMarkers by remember { mutableStateOf<List<Pair<String, LatLng>>>(emptyList()) }

    // Fetch real vet data
    LaunchedEffect(userLocation) {
        userLocation?.let { loc ->
            val response = PlacesApi.service.getNearbyVets(
                location = "${loc.latitude},${loc.longitude}",
                apiKey = Secrets.PLACES_API_KEY
            )

            vetMarkers = response.results.map {
                it.name to LatLng(it.geometry.location.lat, it.geometry.location.lng)
            }
        }
    }

    // Camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation?.let { LatLng(it.latitude, it.longitude) }
                ?: LatLng(54.5742, -1.2350),
            13f
        )
    }

    Scaffold(
        topBar = { AppTopBar("Nearby Vets") }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
        ) {

            // Map Top Half
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = true
                )
            ) {

                // Show user location marker manually (optional)
                userLocation?.let {
                    Marker(
                        state = rememberMarkerState(
                            position = LatLng(it.latitude, it.longitude)
                        ),
                        title = "You are here"
                    )
                }

                // Vet markers
                vetMarkers.forEach { (name, latLng) ->
                    Marker(
                        state = rememberMarkerState(position = latLng),
                        title = name
                    )
                }
            }

            // Vet List Bottom Half
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // bottom 50%
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

                    items(vetMarkers) { (name, position) ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Open Google Maps for navigation
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("google.navigation:q=${position.latitude},${position.longitude}")
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