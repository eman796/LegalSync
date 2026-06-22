package com.development.legally.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.Case
import kotlinx.coroutines.tasks.await

class CaseRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val casesCollection = firestore.collection("cases")

    // Obtener todos los expedientes
    suspend fun getCases(): Result<List<Case>> {
        return try {
            val snapshot = casesCollection
                .orderBy("createdAt")
                .get()
                .await()
            val cases = snapshot.toObjects(Case::class.java)
            Result.success(cases)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener expedientes por cliente
    suspend fun getCasesByClient(clientId: String): Result<List<Case>> {
        return try {
            val snapshot = casesCollection
                .whereEqualTo("clientId", clientId)
                .get()
                .await()
            val cases = snapshot.toObjects(Case::class.java)
            Result.success(cases)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener un expediente por ID
    suspend fun getCaseById(caseId: String): Result<Case> {
        return try {
            val document = casesCollection.document(caseId).get().await()
            val case = document.toObject(Case::class.java)
                ?: return Result.failure(Exception("Expediente no encontrado"))
            Result.success(case)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear expediente
    suspend fun createCase(case: Case): Result<Unit> {
        return try {
            val docRef = casesCollection.document()
            val caseWithId = case.copy(id = docRef.id)
            docRef.set(caseWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Editar expediente
    suspend fun updateCase(case: Case): Result<Unit> {
        return try {
            casesCollection.document(case.id).set(case).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cambiar estado del expediente
    suspend fun updateCaseStatus(caseId: String, status: String): Result<Unit> {
        return try {
            casesCollection.document(caseId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar expediente
    suspend fun deleteCase(caseId: String): Result<Unit> {
        return try {
            casesCollection.document(caseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}