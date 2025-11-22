package uk.ac.tees.mad.e4552051.pawpal.data.repository

import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.ReminderDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

class ReminderRepository(private val dao: ReminderDao) {

    fun getReminders() = dao.getAllReminders()

    suspend fun addReminder(rem: ReminderEntity) = dao.addReminder(rem)

    suspend fun updateReminder(rem: ReminderEntity) = dao.updateReminder(rem)

    suspend fun deleteReminder(rem: ReminderEntity) = dao.deleteReminder(rem)
}