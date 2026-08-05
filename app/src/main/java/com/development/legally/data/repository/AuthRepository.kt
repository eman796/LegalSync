package com.development.legally.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Iniciar sesión
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Error al iniciar sesión"))

            // Buscamos al usuario por su campo email (ya que el ID del documento es personalizado)
            val searchByEmail = firestore.collection("users")
                .whereEqualTo("email", firebaseUser.email)
                .get()
                .await()
            
            val user = if (!searchByEmail.isEmpty) {
                searchByEmail.documents[0].toObject(User::class.java)!!
            } else {
                User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    role = "lawyer"
                )
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Solicitar registro — crea el usuario en Auth y guarda en Firestore con ID legible
    suspend fun requestRegistration(fullName: String, email: String, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Error al crear usuario"))

            val userData = hashMapOf(
                "id" to uid,
                "name" to fullName,
                "email" to email,
                "role" to "secretary",
                "isApproved" to false,
                "createdAt" to System.currentTimeMillis()
            )

            // ID del documento legible: "Nombre Apellido - email@ejemplo.com"
            val customDocId = "$fullName - $email"
            
            firestore.collection("users").document(customDocId).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener rol del usuario actual
    suspend fun getUserRole(): String {
        return try {
            val currentUser = auth.currentUser ?: return "secretary"
            val searchByEmail = firestore.collection("users")
                .whereEqualTo("email", currentUser.email)
                .get()
                .await()
            
            if (!searchByEmail.isEmpty) {
                searchByEmail.documents[0].getString("role") ?: "secretary"
            } else {
                "secretary"
            }
        } catch (e: Exception) {
            "secretary"
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser

    fun isLoggedIn() = auth.currentUser != null
}