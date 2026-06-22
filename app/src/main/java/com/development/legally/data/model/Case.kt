package com.development.legally.data.model

data class Case(
    val id: String = "",
    val caseNumber: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val processType: String = "",
    val status: String = "",     // "En proceso", "Pendiente", "Finalizado", "Archivado"
    val priority: String = "",   // "Baja", "Media", "Alta", "Urgente"
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)