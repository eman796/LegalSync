package com.development.legally.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",  // "lawyer" o "secretary"
    val password: String = "",
    val isApproved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)