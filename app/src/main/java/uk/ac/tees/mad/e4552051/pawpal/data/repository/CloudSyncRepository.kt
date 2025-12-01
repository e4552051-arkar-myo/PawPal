package uk.ac.tees.mad.e4552051.pawpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.PetEntity
import uk.ac.tees.mad.e4552051.pawpal.data.local.entity.ReminderEntity

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
}