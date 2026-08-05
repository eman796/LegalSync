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

    // Obtener clientes ordenados por un campo (alfabéticamente)
    suspend fun getClientsOrderedBy(field: String, ascending: Boolean = true): Result<List<Client>> {
        return try {
            val query = if (ascending) clientsCollection.orderBy(field) else clientsCollection.orderBy(field)
            val snapshot = query.get().await()
            val clients = snapshot.toObjects(Client::class.java)
            Result.success(clients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener clientes filtrados por un campo igual a un valor
    suspend fun getClientsFilteredBy(field: String, value: String): Result<List<Client>> {
        return try {
            val snapshot = clientsCollection
                .whereEqualTo(field, value)
                .get()
                .await()
            val clients = snapshot.toObjects(Client::class.java)
            Result.success(clients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Combinación: filtro y orden
    suspend fun getClientsFilteredAndOrdered(filterField: String?, filterValue: String?, orderField: String?): Result<List<Client>> {
        return try {
            var query: com.google.firebase.firestore.Query = clientsCollection

            // Handle special filter cases
            if (!filterField.isNullOrBlank() && !filterValue.isNullOrBlank()) {
                when (filterField) {
                    "clientName" -> {
                        // filterValue expected as "first||last"
                        val parts = filterValue.split("||")
                        val first = parts.getOrNull(0) ?: ""
                        val last = parts.getOrNull(1) ?: ""
                        if (first.isNotBlank() && last.isNotBlank()) {
                            query = query.whereEqualTo("name", first).whereEqualTo("lastName", last)
                        } else if (first.isNotBlank()) {
                            query = query.whereEqualTo("name", first)
                        }
                    }
                    "createdAtRange" -> {
                        val now = System.currentTimeMillis()
                        val threshold = when (filterValue) {
                            "last_week" -> now - 7L * 24 * 60 * 60 * 1000
                            "last_month" -> now - 30L * 24 * 60 * 60 * 1000
                            "last_year" -> now - 365L * 24 * 60 * 60 * 1000
                            else -> null
                        }
                        if (threshold != null) {
                            query = query.whereGreaterThanOrEqualTo("createdAt", threshold)
                        }
                    }
                    else -> {
                        // default equality filter
                        query = query.whereEqualTo(filterField, filterValue)
                    }
                }
            }

            if (!orderField.isNullOrBlank()) {
                query = query.orderBy(orderField)
            } else if (query == clientsCollection) {
                query = query.orderBy("createdAt")
            } else {
                // if there was a filter but no order specified, default to createdAt
                query = query.orderBy("createdAt")
            }

            val snapshot = query.get().await()
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