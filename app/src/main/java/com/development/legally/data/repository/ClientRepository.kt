package com.development.legally.data.repository

import android.util.Log
import com.development.legally.data.model.Client
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClientRepository {

    private val firestore by lazy { Firebase.firestore }
    private val clientsCollection by lazy { firestore.collection("clients") }

    suspend fun getClients(): Result<List<Client>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = clientsCollection
                .orderBy("createdAt")
                .get()
                .await()
            val clients = snapshot.toObjects(Client::class.java)
            Result.success(clients)
        } catch (e: Exception) {
            Log.e("ClientRepo", "Error al obtener clientes", e)
            Result.failure(e)
        }
    }

    suspend fun getClientById(clientId: String): Result<Client> = withContext(Dispatchers.IO) {
        try {
            val document = clientsCollection.document(clientId).get().await()
            val client = document.toObject(Client::class.java)
                ?: return@withContext Result.failure(Exception("Cliente no encontrado"))
            Result.success(client)
        } catch (e: Exception) {
            Log.e("ClientRepo", "Error al obtener cliente por ID", e)
            Result.failure(e)
        }
    }

    suspend fun createClient(client: Client): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("ClientRepo", "Iniciando creación de cliente...")
            val docRef = clientsCollection.document()
            val clientWithId = client.copy(id = docRef.id)
            docRef.set(clientWithId).await()
            Log.d("ClientRepo", "Cliente creado exitosamente en Firestore")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ClientRepo", "Error al crear cliente", e)
            Result.failure(e)
        }
    }

    suspend fun updateClient(client: Client): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("ClientRepo", "Intentando actualizar cliente: ${client.id}")
            if (client.id.isBlank()) return@withContext Result.failure(Exception("ID de cliente no válido"))
            
            clientsCollection.document(client.id).set(client).await()
            Log.d("ClientRepo", "Actualización exitosa en Firestore")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ClientRepo", "Error al actualizar cliente", e)
            Result.failure(e)
        }
    }

    suspend fun deleteClient(clientId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            clientsCollection.document(clientId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ClientRepo", "Error al eliminar cliente", e)
            Result.failure(e)
        }
    }

    // Métodos de filtrado simplificados para brevedad
    suspend fun getClientsOrderedBy(field: String, ascending: Boolean = true): Result<List<Client>> = withContext(Dispatchers.IO) {
        try {
            val query = if (ascending) clientsCollection.orderBy(field) else clientsCollection.orderBy(field)
            val snapshot = query.get().await()
            Result.success(snapshot.toObjects(Client::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getClientsFilteredBy(field: String, value: String): Result<List<Client>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = clientsCollection.whereEqualTo(field, value).get().await()
            Result.success(snapshot.toObjects(Client::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getClientsFilteredAndOrdered(filterField: String?, filterValue: String?, orderField: String?): Result<List<Client>> = withContext(Dispatchers.IO) {
        try {
            var query: com.google.firebase.firestore.Query = clientsCollection
            if (!filterField.isNullOrBlank() && !filterValue.isNullOrBlank()) {
                query = query.whereEqualTo(filterField, filterValue)
            }
            if (!orderField.isNullOrBlank()) {
                query = query.orderBy(orderField)
            } else {
                query = query.orderBy("createdAt")
            }
            val snapshot = query.get().await()
            Result.success(snapshot.toObjects(Client::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }
}
