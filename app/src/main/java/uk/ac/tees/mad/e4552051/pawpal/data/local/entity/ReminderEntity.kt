package uk.ac.tees.mad.e4552051.pawpal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val reminderType: String,
    val date: Long,      // store timestamp
    val note: String? = null,
    val isDone: Boolean = false
)