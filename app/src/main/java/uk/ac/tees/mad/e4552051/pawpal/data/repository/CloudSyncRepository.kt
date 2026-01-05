package uk.ac.tees.mad.e4552051.pawpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

import kotlinx.coroutines.tasks.await
object CloudSyncRepository {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // -----------------------------
    // PET SYNC
    // -----------------------------
    fun syncPet(pet: PetEntity) {
        val map = hashMapOf(
            "id" to pet.id,
            "name" to pet.name,
            "type" to pet.type,
            "age" to pet.age,
            "imageUri" to pet.imageUri
        )

        db.collection("pets")
            .document(pet.id.toString())
            .set(map, SetOptions.merge())   // Add or update
    }

    fun updatePet(pet: PetEntity) {
        val map = hashMapOf(
            "name" to pet.name,
            "type" to pet.type,
            "age" to pet.age,
            "imageUri" to pet.imageUri
        )

        db.collection("pets")
            .document(pet.id.toString())
            .set(map, SetOptions.merge())   // Only update changed fields
    }

    fun deletePet(petId: Int) {
        db.collection("pets")
            .document(petId.toString())
            .delete()
    }

    suspend fun pullPets(): List<PetEntity> {
        val snap = db.collection("pets").get().await()
        return snap.documents.mapNotNull { doc ->
            val id = doc.id.toIntOrNull() ?: return@mapNotNull null
            val name = doc.getString("name") ?: return@mapNotNull null
            val type = doc.getString("type") ?: ""
            val breed = doc.getString("breed") ?: ""
            val age = (doc.getLong("age") ?: 0L).toInt()
            val imageUri = doc.getString("imageUri")
            PetEntity(id = id, name = name, type = type, age = age, imageUri = imageUri)
        }
    }


    // -----------------------------
    // REMINDER SYNC
    // -----------------------------
    fun syncReminder(reminder: ReminderEntity) {
        val map = hashMapOf(
            "id" to reminder.id,
            "petName" to reminder.petName,
            "reminderType" to reminder.reminderType,
            "date" to reminder.date,
            "note" to reminder.note
        )

        db.collection("reminders")
            .document(reminder.id.toString())
            .set(map, SetOptions.merge())
    }

    fun updateReminder(reminder: ReminderEntity) {
        val map = hashMapOf(
            "petName" to reminder.petName,
            "reminderType" to reminder.reminderType,
            "date" to reminder.date,
            "note" to reminder.note
        )

        db.collection("reminders")
            .document(reminder.id.toString())
            .set(map, SetOptions.merge())
    }

    fun deleteReminder(reminderId: Int) {
        db.collection("reminders")
            .document(reminderId.toString())
            .delete()
    }

    suspend fun pullReminders(): List<ReminderEntity> {
        val snap = db.collection("reminders").get().await()
        return snap.documents.mapNotNull { doc ->
            val id = doc.id.toIntOrNull() ?: return@mapNotNull null
            val petName = doc.getString("petName") ?: return@mapNotNull null
            val reminderType = doc.getString("reminderType") ?: ""
            val note = doc.getString("note")
            val date = doc.getLong("date") ?: System.currentTimeMillis()
            ReminderEntity(id = id, petName = petName, reminderType = reminderType, note = note, date = date)
        }
    }
}