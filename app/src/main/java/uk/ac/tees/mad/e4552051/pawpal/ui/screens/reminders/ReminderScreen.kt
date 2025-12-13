package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.components.ReminderItem
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit,
    onAddReminder: () -> Unit,
    onReminderClick: (ReminderEntity) -> Unit
) {
    val reminders by viewModel.reminders.collectAsState(initial = emptyList())
    val sdf = SimpleDateFormat("dd MMM yyyy — HH:mm", Locale.getDefault())

    Scaffold(
        topBar = { AppTopBar(
            title = "Reminders",
            onBack = onNavigateBack
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddReminder) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                "Upcoming Reminders",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(reminders, key = { it.id }) { reminder ->

                    ReminderItem(
                        reminder = reminder,
                        onClick = { onReminderClick(reminder) },
                        onMarkDone = { done ->
                            viewModel.updateReminder(
                                reminder.copy(isDone = done)
                            )
                        },
                        dateString = sdf.format(Date(reminder.date))
                    )
                }
            }

        }
    }
}