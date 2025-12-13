package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.data.local.notification.ReminderNotifications
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.components.ReminderForm
import uk.ac.tees.mad.e4552051.pawpal.ui.components.TimePickerDialog
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel
import java.util.*
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDetailScreen(
    reminderId: Int,
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var reminder by remember { mutableStateOf<ReminderEntity?>(null) }

    LaunchedEffect(reminderId) {
        reminder = viewModel.getReminderById(reminderId)
        Log.d("ReminderDebug", "Loaded reminder: $reminder")
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
        mutableStateOf(
            initialCalendar.get(Calendar.HOUR_OF_DAY) to
                    initialCalendar.get(Calendar.MINUTE)
        )
    }

    // Picker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar(
            title = "Add Reminder",
            onBack = onNavigateBack
        ) },
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
                timeText = selectedTime?.let { (h, m) -> String.format("%02d:%02d", h, m) }
                    ?: "Select Time",
                onTimeClick = { showTimePicker = true },
                onClearTime = { selectedTime = null }
            )

            // SAVE CHANGES
            Button(
                onClick = {
                    Log.d(
                        "ReminderDebug",
                        "Detail Save clicked: petName='$petName', type='$type', date=$selectedDate, time=$selectedTime"
                    )

                    // 1) Validate text
                    if (petName.isBlank() || type.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter pet name and reminder type")
                        }
                        return@Button
                    }

                    // 2) Validate date & time
                    if (selectedDate == null || selectedTime == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please select date and time")
                        }
                        return@Button
                    }

                    // 3) Safe destructuring
                    val (hour, minute) = selectedTime!!

                    val updatedCalendar = Calendar.getInstance().apply {
                        // DATE
                        timeInMillis = selectedDate!!

                        // TIME
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    val triggerAtMillis = updatedCalendar.timeInMillis

                    Log.d("ReminderDebug", "Updating reminder to trigger at $triggerAtMillis")

                    // 4) Update DB
                    viewModel.updateReminder(
                        reminder!!.copy(
                            petName = petName,
                            reminderType = type,
                            note = note.ifBlank { null },
                            date = triggerAtMillis
                        )
                    )

                    // 5) Re-schedule alarm for edited time
                    val alarmManager =
                        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                        alarmManager.canScheduleExactAlarms()
                    ) {
                        // Use reminder id as notification id so it overwrites previous one
                        val notificationId = reminder!!.id

                        ReminderNotifications.scheduleReminder(
                            context = context,
                            triggerAtMillis = triggerAtMillis,
                            title = "$petName – $type",
                            message = "Time for $type for $petName",
                            notificationId = notificationId
                        )

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Reminder updated")
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Reminder saved, but exact alarms are disabled in system settings"
                            )
                        }
                    }

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
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

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