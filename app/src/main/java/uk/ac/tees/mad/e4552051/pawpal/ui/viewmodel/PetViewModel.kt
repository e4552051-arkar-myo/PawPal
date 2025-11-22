package uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.repository.PetRepository

class PetViewModel(
    private val repository: PetRepository
) : ViewModel() {

    val pets: Flow<List<PetEntity>> = repository.getPets()

    fun addPet(name: String, type: String, age: Int, imageUri: String? = null) {
        viewModelScope.launch {
            repository.addPet(
                PetEntity(
                    name = name,
                    type = type,
                    age = age,
                    imageUri = imageUri
                )
            )
        }
    }

    fun getPet(id: Int): Flow<PetEntity?> {
        return repository.getPetById(id)
    }

    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.updatePet(pet)
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }
}