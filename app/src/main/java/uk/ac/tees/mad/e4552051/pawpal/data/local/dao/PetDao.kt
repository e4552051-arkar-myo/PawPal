package uk.ac.tees.mad.e4552051.pawpal.data.local.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity) : Long

    @Query("SELECT * FROM pets ORDER BY id DESC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    fun getPetById(id: Int): Flow<PetEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(pets: List<PetEntity>)
}