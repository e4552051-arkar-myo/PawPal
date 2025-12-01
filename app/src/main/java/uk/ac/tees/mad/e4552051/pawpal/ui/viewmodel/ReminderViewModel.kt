package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.data.repository.ReminderRepository
import uk.ac.tees.mad.e4552051.pawpal.data.repository.SettingsRepository
import android.util.Log
class ReminderViewModel(
    application: Application,
    private val repository: ReminderRepository
) : ViewModel() {

    // All reminders live flow
    val reminders: Flow<List<ReminderEntity>> = repository.getReminders()
    private val settingsRepository = SettingsRepository(application)

    // Add new reminder
    fun addReminder(
        petName: String,
        reminderType: String,
        note: String? = null,
        date: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()
            val reminder = ReminderEntity(
                petName = petName,
                reminderType = reminderType,
                date = date,
                note = note
            )
            repository.addReminder(reminder, syncEnabled)
        }
    }

    // Update existing reminder
    fun updateReminder(reminder: ReminderEntity) {
        Log.i("check", "updateReminder called: $reminder")
        viewModelScope.launch {
            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()
            repository .updateReminder(reminder, syncEnabled)
        }
    }

    // Delete reminder
    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()
            repository.deleteReminder(reminder, syncEnabled)
        }
    }

    // Load reminder by ID (for Edit screen)
    suspend fun getReminderById(id: Int): ReminderEntity? {
        return reminders.first().find { it.id == id }
    }
}