package com.development.legally.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.Case
import com.development.legally.data.model.Client
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class CaseRepository {

    private val firestore by lazy { Firebase.firestore }
    private val casesCollection by lazy { firestore.collection("Expedientes") }

    suspend fun getCases(): Result<List<Case>> {
        return try {
            val snapshot = casesCollection
                .get()
                .await()
            val casos = snapshot.toObjects(Case::class.java)
            Result.success(casos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    suspend fun createCase(case: Case): Result<String> {
        return try {
            val docRef = casesCollection.document()
            // Asignamos el ID generado tanto al campo interno como al objeto
            val caseWithId = case.copy(id = docRef.id, firestoreDocId = docRef.id)
            docRef.set(caseWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCase(case: Case): Result<Unit> {
        return try {
            // Usamos firestoreDocId si está disponible, o el campo id como fallback
            val docId = case.firestoreDocId.ifBlank { case.id }
            if (docId.isBlank()) return Result.failure(Exception("ID de documento no válido"))
            
            casesCollection.document(docId).set(case).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    suspend fun deleteCase(caseId: String): Result<Unit> {
        return try {
            casesCollection.document(caseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
