package uk.ac.tees.mad.e4552051.pawpal.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminder: ReminderEntity) : Long

    @Query("SELECT * FROM reminders ORDER BY date ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)
}