package uk.ac.tees.mad.e4552051.pawpal.ui.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PawPal",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Loading...",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    // Auto navigate after delay
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToHome()
    }
}