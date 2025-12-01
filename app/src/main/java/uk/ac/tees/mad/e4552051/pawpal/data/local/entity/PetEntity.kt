package uk.ac.tees.mad.e4552051.pawpal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val age: Int,
    val imageUri: String? = null
)