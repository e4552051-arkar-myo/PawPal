package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity
import uk.ac.tees.mad.e4552051.pawpal.data.repository.ReminderRepository

class ReminderViewModel(
    private val repository: ReminderRepository
) : ViewModel() {

    // All reminders live flow
    val reminders: Flow<List<ReminderEntity>> = repository.getReminders()

    // Add new reminder
    fun addReminder(
        petName: String,
        reminderType: String,
        note: String? = null,
        date: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            val reminder = ReminderEntity(
                petName = petName,
                reminderType = reminderType,
                date = date,
                note = note
            )
            repository.addReminder(reminder)
        }
    }

    // Update existing reminder
    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
        }
    }

    // Delete reminder
    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    // Load reminder by ID (for Edit screen)
    suspend fun getReminderById(id: Int): ReminderEntity? {
        return reminders.first().find { it.id == id }
    }
}