package uk.ac.tees.mad.e4552051.pawpal.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: PawPalDatabase? = null

    fun getDatabase(context: Context): PawPalDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                PawPalDatabase::class.java,
                "pawpal.db"
            )
                .fallbackToDestructiveMigration(false)
                .build()
            INSTANCE = instance
            instance
        }
    }
}