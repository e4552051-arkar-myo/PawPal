package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.components.ReminderForm
import uk.ac.tees.mad.e4552051.pawpal.ui.components.TimePickerDialog
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDetailScreen(
    reminderId: Int,
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit
) {
    var reminder by remember { mutableStateOf<ReminderEntity?>(null) }

    LaunchedEffect(reminderId) {
        reminder = viewModel.getReminderById(reminderId)
    }

    if (reminder == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Extract fields
    var petName by remember { mutableStateOf(reminder!!.petName) }
    var type by remember { mutableStateOf(reminder!!.reminderType) }
    var note by remember { mutableStateOf(reminder!!.note ?: "") }

    val initialCalendar = Calendar.getInstance().apply { timeInMillis = reminder!!.date }

    var selectedDate: Long? by remember { mutableStateOf(reminder!!.date) }
    var selectedTime: Pair<Int, Int>? by remember {
        mutableStateOf(initialCalendar.get(Calendar.HOUR_OF_DAY) to initialCalendar.get(Calendar.MINUTE))
    }
    // Picker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { AppTopBar("Edit Reminder") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ReminderForm(
                petName = petName,
                onPetNameChange = { petName = it },
                type = type,
                onTypeChange = { type = it },
                note = note,
                onNoteChange = { note = it },
                dateText = selectedDate?.let { sdf.format(Date(it)) } ?: "Select Date",
                onDateClick = { showDatePicker = true },
                onClearDate = { selectedDate = null },

                timeText = selectedTime?.let { (h,m)->"%02d:%02d".format(h,m)} ?: "Select Time",
                onTimeClick = { showTimePicker = true },
                onClearTime = { selectedTime = null }
            )

            // Save button
            Button(
                onClick = {
                    if (petName.isBlank() || type.isBlank() || selectedDate == null || selectedTime == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please fill all fields")
                        }
                        return@Button
                    }
                    val updatedCalendar = Calendar.getInstance().apply {

                        // DATE part
                        timeInMillis = selectedDate ?: System.currentTimeMillis()

                        // TIME part (safe)
                        val (hour, minute) = selectedTime ?: (0 to 0)

                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    viewModel.updateReminder(
                        reminder!!.copy(
                            petName = petName,
                            reminderType = type,
                            note = note.ifBlank { null },
                            date = updatedCalendar.timeInMillis
                        )
                    )

                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Reminder")
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }

    // DATE PICKER
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TIME PICKER
    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                selectedTime = hour to minute
                showTimePicker = false
            }
        )
    }

    // DELETE CONFIRMATION
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Reminder") },
            text = { Text("Are you sure you want to delete this reminder?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteReminder(reminder!!)
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