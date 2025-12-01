package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.e4552051.pawpal.data.repository.PetRepository

class PetViewModelFactory(
    private val application: Application,
    private val repository: PetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}