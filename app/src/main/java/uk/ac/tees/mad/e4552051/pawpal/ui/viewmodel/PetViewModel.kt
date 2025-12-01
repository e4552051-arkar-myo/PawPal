package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.repository.PetRepository
import uk.ac.tees.mad.e4552051.pawpal.data.repository.SettingsRepository

class PetViewModel(
    application: Application,
    private val repository: PetRepository
) : AndroidViewModel(application) {

    val pets: Flow<List<PetEntity>> = repository.getPets()
    private val settingsRepository = SettingsRepository(application)

    fun addPet(name: String, type: String, age: Int, imageUri: String? = null) {
        viewModelScope.launch {

            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()

            repository.addPet(
                PetEntity(
                    name = name,
                    type = type,
                    age = age,
                    imageUri = imageUri
                ),
                cloudSyncEnabled = syncEnabled
            )
        }
    }

    fun getPet(id: Int): Flow<PetEntity?> {
        return repository.getPetById(id)
    }

    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()
            repository.updatePet(pet, syncEnabled)
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            // Get cloud sync flag safely inside coroutine
            val syncEnabled = settingsRepository.cloudSync.first()
            repository.deletePet(pet, syncEnabled)
        }
    }
}