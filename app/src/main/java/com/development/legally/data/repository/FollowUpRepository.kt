package com.development.legally.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.FollowUp
import kotlinx.coroutines.tasks.await

class FollowUpRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val followUpsCollection = firestore.collection("followups")

    // Obtener seguimientos por expediente
    suspend fun getFollowUpsByCase(caseId: String): Result<List<FollowUp>> {
        return try {
            val snapshot = followUpsCollection
                .whereEqualTo("caseId", caseId)
                .orderBy("createdAt")
                .get()
                .await()
            val followUps = snapshot.toObjects(FollowUp::class.java)
            Result.success(followUps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear seguimiento
    suspend fun createFollowUp(followUp: FollowUp): Result<Unit> {
        return try {
            val docRef = followUpsCollection.document()
            val followUpWithId = followUp.copy(id = docRef.id)
            docRef.set(followUpWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Editar seguimiento
    suspend fun updateFollowUp(followUp: FollowUp): Result<Unit> {
        return try {
            followUpsCollection.document(followUp.id).set(followUp).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar seguimiento
    suspend fun deleteFollowUp(followUpId: String): Result<Unit> {
        return try {
            followUpsCollection.document(followUpId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}