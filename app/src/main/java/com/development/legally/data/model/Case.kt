package com.development.legally.data.model

import com.google.firebase.Timestamp

data class Case(
    val id: String = "",
    val caseNumber: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val processType: String = "",
    val status: String = "",     // "En proceso", "Pendiente", "Finalizado", "Archivado"
    val priority: String = "",   // "Baja", "Media", "Alta", "Urgente"
    val description: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)