package com.development.legally.data.model

data class Client(
    val id: String = "",
    val name: String = "",
    val lastName: String = "",
    val nationality: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = "",
    val personType: String = "", // "Física" o "Jurídica"
    val address: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)