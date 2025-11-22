package uk.ac.tees.mad.e4552051.pawpal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.PetDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.ReminderDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

@Database(
    entities = [PetEntity::class, ReminderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PawPalDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun reminderDao(): ReminderDao
}