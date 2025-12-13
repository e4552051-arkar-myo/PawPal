package uk.ac.tees.mad.e4552051.pawpal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onNavigateToReminders: (() -> Unit)? = null,
    onNavigateToSettings: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },

        // Back arrow (only if provided)
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },

        // Action icons
        actions = {
            if (onNavigateToReminders != null) {
                IconButton(onClick = onNavigateToReminders) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Reminders"
                    )
                }
            }

            if (onNavigateToSettings != null) {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
    )
}