package com.development.legally.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.Client
import kotlinx.coroutines.tasks.await

class ClientRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val clientsCollection = firestore.collection("clients")

    // Obtener todos los clientes
    suspend fun getClients(): Result<List<Client>> {
        return try {
            val snapshot = clientsCollection
                .orderBy("createdAt")
                .get()
                .await()
            val clients = snapshot.toObjects(Client::class.java)
            Result.success(clients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener un cliente por ID
    suspend fun getClientById(clientId: String): Result<Client> {
        return try {
            val document = clientsCollection.document(clientId).get().await()
            val client = document.toObject(Client::class.java)
                ?: return Result.failure(Exception("Cliente no encontrado"))
            Result.success(client)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear cliente
    suspend fun createClient(client: Client): Result<Unit> {
        return try {
            val docRef = clientsCollection.document()
            val clientWithId = client.copy(id = docRef.id)
            docRef.set(clientWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Editar cliente
    suspend fun updateClient(client: Client): Result<Unit> {
        return try {
            clientsCollection.document(client.id).set(client).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar cliente
    suspend fun deleteClient(clientId: String): Result<Unit> {
        return try {
            clientsCollection.document(clientId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}