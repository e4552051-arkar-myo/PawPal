package uk.ac.tees.mad.e4552051.pawpal.data.repository

import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.ReminderDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

class ReminderRepository(private val dao: ReminderDao) {

    fun getReminders() = dao.getAllReminders()

    suspend fun addReminder(reminder: ReminderEntity, syncEnabled: Boolean) {
        val generatedId = dao.addReminder(reminder).toInt()

        if (syncEnabled) {
            CloudSyncRepository.syncReminder(reminder.copy(id = generatedId))
        }
    }

    suspend fun updateReminder(rem: ReminderEntity, cloudSyncEnabled: Boolean){
        dao.updateReminder(rem)
        if (cloudSyncEnabled) {
            CloudSyncRepository.updateReminder(rem)
        }
    }

    suspend fun deleteReminder(rem: ReminderEntity, cloudSyncEnabled: Boolean) {
        dao.deleteReminder(rem)
        if (cloudSyncEnabled) {
            CloudSyncRepository.deleteReminder(rem.id)
        }

    }
    suspend fun syncFromCloud() {
        val cloudReminders = CloudSyncRepository.pullReminders()
        dao.upsertAll(cloudReminders)
    }
}