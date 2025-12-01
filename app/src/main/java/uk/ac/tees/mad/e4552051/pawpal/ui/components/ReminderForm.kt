package uk.ac.tees.mad.e4552051.pawpal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReminderForm(
    petName: String,
    onPetNameChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    dateText: String,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit,
    timeText: String,
    onTimeClick: () -> Unit,
    onClearTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = petName,
            onValueChange = onPetNameChange,
            label = { Text("Pet Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text("Reminder Type") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )

        ReminderField(
            text = dateText,
            onClick = onDateClick,
            onClear = onClearDate
        )

        ReminderField(
            text = timeText,
            onClick = onTimeClick,
            onClear = onClearTime
        )
    }
}

@Composable
fun ReminderField(
    text: String,
    onClick: () -> Unit,
    onClear: () -> Unit,
    showClearButton: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium,
        border = ButtonDefaults.outlinedButtonBorder,
        color = MaterialTheme.colorScheme.surface
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // THIS is the only clickable area for picker
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Clear button that DOES NOT propagate click
            if (showClearButton && text.isNotBlank() && !text.contains("Select", ignoreCase = true)) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onClear()
                        }
                )
            }
        }
    }
}