package uk.ac.tees.mad.e4552051.pawpal.data.repository

import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.PetDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity

class PetRepository(
    private val petDao: PetDao
) {

    fun getPets() = petDao.getAllPets()

    fun getPetById(id: Int) = petDao.getPetById(id)

    suspend fun addPet(pet: PetEntity) = petDao.insertPet(pet)

    suspend fun updatePet(pet: PetEntity) = petDao.updatePet(pet)

    suspend fun deletePet(pet: PetEntity) = petDao.deletePet(pet)
}