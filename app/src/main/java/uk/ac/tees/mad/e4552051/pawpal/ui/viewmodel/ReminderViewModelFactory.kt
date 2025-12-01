package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.e4552051.pawpal.data.repository.ReminderRepository

class ReminderViewModelFactory(
    private val application: Application,
    private val repository: ReminderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReminderViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}