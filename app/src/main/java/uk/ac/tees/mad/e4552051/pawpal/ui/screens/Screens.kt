package uk.ac.tees.mad.e4552051.pawpal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
//
@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "PawPal", modifier = Modifier.padding(8.dp))
            Text(text = "Loading...", modifier = Modifier.padding(bottom = 16.dp))
        }
    }

    // Auto navigate after 1500ms
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToHome()
    }
}
//@Composable
//fun SplashScreen(onNavigateToHome: () -> Unit) {
//    // Very simple splash with a button for manual navigation during dev.
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(text = "PawPal", modifier = Modifier.padding(8.dp))
//            Text(text = "Loading...", modifier = Modifier.padding(bottom = 16.dp))
//            Button(onClick = onNavigateToHome) {
//                Text("Go to Home (dev)")
//            }
//        }
//    }
//}

@Composable
fun HomeScreen(onNavigateToAddPet: () -> Unit, onNavigateToReminders: () -> Unit, onNavigateToSettings: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Home — Pet list & upcoming reminders", modifier = Modifier.padding(bottom = 12.dp))
        Button(onClick = onNavigateToAddPet, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
            Text("Add Pet")
        }
        Button(onClick = onNavigateToReminders, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
            Text("View Reminders")
        }
        Button(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
            Text("Settings")
        }
    }
}

@Composable
fun AddPetScreen(onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add/Edit Pet", modifier = Modifier.padding(bottom = 12.dp))
        // placeholder content: replace with TextFields later
        Button(onClick = onNavigateBack) {
            Text("Save & Back")
        }
    }
}

@Composable
fun ReminderScreen(onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reminders", modifier = Modifier.padding(bottom = 12.dp))
        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", modifier = Modifier.padding(bottom = 12.dp))
        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}