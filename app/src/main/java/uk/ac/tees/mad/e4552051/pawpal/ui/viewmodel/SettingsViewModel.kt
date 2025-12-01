package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.repository.SettingsRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SettingsRepository(application)

    val darkMode = repo.darkMode
    val notifications = repo.notifications

    val cloudSync = repo.cloudSync

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repo.setDarkMode(enabled)
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repo.setNotifications(enabled)
        }
    }
    fun setCloudSync(enabled: Boolean) {
        viewModelScope.launch {
            repo.setCloudSync(enabled)
        }
    }
}