package com.development.legally.data.repository

import com.development.legally.data.model.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val firestore by lazy { Firebase.firestore }
    private val agendaCollection by lazy { firestore.collection("Agenda") }

    suspend fun createEvent(event: Event): Result<Unit> {
        return try {
            val docId = if (event.id.isNotEmpty()) event.id else "${event.titulo} - ${event.fechaHora}"
            val eventWithId = event.copy(id = docId)
            agendaCollection.document(docId).set(eventWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEvents(): Result<List<Event>> {
        return try {
            val snapshot = agendaCollection.get().await()
            val events = snapshot.toObjects(Event::class.java)
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventById(eventId: String): Result<Event?> {
        return try {
            val document = agendaCollection.document(eventId).get().await()
            val event = document.toObject(Event::class.java)
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateEvent(event: Event): Result<Unit> {
        return try {
            agendaCollection.document(event.id).set(event).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            agendaCollection.document(eventId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
