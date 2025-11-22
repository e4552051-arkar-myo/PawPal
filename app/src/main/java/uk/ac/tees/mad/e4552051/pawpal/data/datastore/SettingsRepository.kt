package uk.ac.tees.mad.e4552051.pawpal.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    val darkMode: Flow<Boolean> =
        context.settingsDataStore.data.map { it[SettingsKeys.DARK_MODE] ?: false }

    val notifications: Flow<Boolean> =
        context.settingsDataStore.data.map { it[SettingsKeys.NOTIFICATIONS] ?: true }

    suspend fun setDarkMode(value: Boolean) {
        context.settingsDataStore.edit { it[SettingsKeys.DARK_MODE] = value }
    }

    suspend fun setNotifications(value: Boolean) {
        context.settingsDataStore.edit { it[SettingsKeys.NOTIFICATIONS] = value }
    }
}