package uk.ac.tees.mad.e4552051.pawpal.data.repository

import uk.ac.tees.mad.e4552051.pawpal.data.local.dao.PetDao
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity

class PetRepository(
    private val petDao: PetDao
) {

        fun getPets() = petDao.getAllPets()

        fun getPetById(id: Int) = petDao.getPetById(id)

        suspend fun addPet(pet: PetEntity, cloudSyncEnabled: Boolean) {
            val generatedId = petDao.insertPet(pet).toInt()

            if (cloudSyncEnabled) {
                CloudSyncRepository.syncPet(
                    pet.copy(id = generatedId)
                )
            }
        }

        suspend fun updatePet(pet: PetEntity, cloudSyncEnabled: Boolean) {
            petDao.updatePet(pet)

            if (cloudSyncEnabled) {
                CloudSyncRepository.updatePet(pet)
            }
        }

        suspend fun deletePet(pet: PetEntity, cloudSyncEnabled: Boolean) {
            petDao.deletePet(pet)
            if (cloudSyncEnabled) {
                CloudSyncRepository.deletePet(pet.id)
            }
        }

        suspend fun syncFromCloud() {
            val cloudPets = CloudSyncRepository.pullPets()
            petDao.upsertAll(cloudPets)
        }
    }