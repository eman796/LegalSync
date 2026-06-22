package com.development.legally.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.Reminder
import kotlinx.coroutines.tasks.await

class ReminderRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val remindersCollection = firestore.collection("reminders")

    // Obtener todos los recordatorios
    suspend fun getReminders(): Result<List<Reminder>> {
        return try {
            val snapshot = remindersCollection
                .orderBy("createdAt")
                .get()
                .await()
            val reminders = snapshot.toObjects(Reminder::class.java)
            Result.success(reminders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener recordatorios por expediente
    suspend fun getRemindersByCase(caseId: String): Result<List<Reminder>> {
        return try {
            val snapshot = remindersCollection
                .whereEqualTo("caseId", caseId)
                .get()
                .await()
            val reminders = snapshot.toObjects(Reminder::class.java)
            Result.success(reminders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear recordatorio
    suspend fun createReminder(reminder: Reminder): Result<Unit> {
        return try {
            val docRef = remindersCollection.document()
            val reminderWithId = reminder.copy(id = docRef.id)
            docRef.set(reminderWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Editar recordatorio
    suspend fun updateReminder(reminder: Reminder): Result<Unit> {
        return try {
            remindersCollection.document(reminder.id).set(reminder).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar recordatorio
    suspend fun deleteReminder(reminderId: String): Result<Unit> {
        return try {
            remindersCollection.document(reminderId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}