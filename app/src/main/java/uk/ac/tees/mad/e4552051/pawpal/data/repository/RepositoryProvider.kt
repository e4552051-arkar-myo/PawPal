package uk.ac.tees.mad.e4552051.pawpal.data.repository

import android.content.Context
import uk.ac.tees.mad.e4552051.pawpal.data.local.database.DatabaseProvider

object RepositoryProvider {

    fun providePetRepository(context: Context): PetRepository {
        val db = DatabaseProvider.getDatabase(context)
        return PetRepository(db.petDao())
    }

    fun provideReminderRepository(context: Context): ReminderRepository {
        val db = DatabaseProvider.getDatabase(context)
        return ReminderRepository(db.reminderDao())
    }
}