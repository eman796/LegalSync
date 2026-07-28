package com.development.legally.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.development.legally.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Iniciar sesión con verificación de aprobación
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Error al iniciar sesión"))

            // VERIFICACIÓN DE SEGURIDAD: Consultar Firestore para ver si está aprobado
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            val isApproved = userDoc.getBoolean("isApproved") ?: false

            if (!isApproved) {
                auth.signOut() // Cerramos la sesión si no está aprobado
                return Result.failure(Exception("Tu cuenta está pendiente de aprobación por un administrador."))
            }

            val user = User(
                id = firebaseUser.uid,
                name = userDoc.getString("name") ?: "",
                email = firebaseUser.email ?: "",
                role = userDoc.getString("role") ?: "lawyer"
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Solicitar registro real: Crea cuenta en Auth y marca como NO aprobado en Firestore
    suspend fun requestRegistration(fullName: String, email: String, password: String): Result<Unit> {
        return try {
            // 1. Crear usuario en Firebase Auth
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("No se pudo crear el usuario"))

            // 2. Crear perfil en Firestore marcado como NO aprobado
            val userData = hashMapOf(
                "name" to fullName,
                "email" to email,
                "isApproved" to false, // Por defecto falso
                "role" to "lawyer",
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users").document(uid).set(userData).await()
            
            // Cerramos sesión inmediatamente después de registrarse para que espere aprobación
            auth.signOut()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn() = auth.currentUser != null
}
