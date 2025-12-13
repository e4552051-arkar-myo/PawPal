package uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.notification.ReminderNotifications
import uk.ac.tees.mad.e4552051.pawpal.ui.components.AppTopBar
import uk.ac.tees.mad.e4552051.pawpal.ui.components.ReminderForm
import uk.ac.tees.mad.e4552051.pawpal.ui.components.TimePickerDialog
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel
import java.util.Date
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddReminderScreen(
    viewModel: ReminderViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var petName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())

    Scaffold(
        topBar = { AppTopBar(
            title = "Add Reminders",
            onBack = onNavigateBack
        ) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
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
                timeText = selectedTime?.let { (h, m) -> String.format("%02d:%02d", h, m) }
                    ?: "Select Time",
                onTimeClick = { showTimePicker = true },
                onClearDate = { selectedDate = null },
                onClearTime = { selectedTime = null }
            )

            // SAVE BUTTON
            Button(
                onClick = {
                    Log.d(
                        "ReminderDebug",
                        "Save clicked: petName='$petName', type='$type', date=$selectedDate, time=$selectedTime"
                    )

                    // 1) Basic text validation
                    if (petName.isBlank() || type.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter pet name and reminder type")
                        }
                        return@Button
                    }

                    // 2) Date/time must be selected
                    if (selectedDate == null || selectedTime == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please select date and time")
                        }
                        return@Button
                    }

                    // 3) Safe destructuring
                    val (hour, minute) = selectedTime!!

                    val calendar = java.util.Calendar.getInstance().apply {
                        timeInMillis = selectedDate!!
                        set(java.util.Calendar.HOUR_OF_DAY, hour)
                        set(java.util.Calendar.MINUTE, minute)
                        set(java.util.Calendar.SECOND, 0)
                    }

                    val triggerAtMillis = calendar.timeInMillis
                    val notificationId = Random.nextInt()

                    // 4) Save in DB with real trigger time
                    viewModel.addReminder(
                        petName = petName,
                        reminderType = type,
                        note = note,
                        date = triggerAtMillis
                    )

                    // 5) Permission check for exact alarm
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        !alarmManager.canScheduleExactAlarms()
                    ) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please allow exact alarms in system settings")
                        }
                        return@Button
                    }

                    // 6) Schedule notification
                    Log.d("ReminderDebug", "Calling scheduleReminder at $triggerAtMillis")
                    ReminderNotifications.scheduleReminder(
                        context = context,
                        triggerAtMillis = triggerAtMillis,
                        title = "$petName – $type",
                        message = "Time for $type for $petName",
                        notificationId = notificationId
                    )

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Reminder scheduled!")
                    }

                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Reminder")
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
}