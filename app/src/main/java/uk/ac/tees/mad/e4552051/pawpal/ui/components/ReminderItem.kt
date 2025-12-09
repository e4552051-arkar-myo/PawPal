package uk.ac.tees.mad.e4552051.pawpal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

@Composable
fun ReminderItem(
    reminder: ReminderEntity,
    onClick: () -> Unit,
    onMarkDone: (Boolean) -> Unit,
    dateString: String
) {
    val isDone = reminder.isDone

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    reminder.petName,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    reminder.reminderType,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                )

                if (!reminder.note.isNullOrBlank()) {
                    Text(
                        reminder.note!!,
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                Text(
                    dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

            }

            IconButton(
                onClick = { onMarkDone(!isDone) }
            ) {
                Icon(
                    imageVector = if (isDone) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    contentDescription = "Mark as done",
                    tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}