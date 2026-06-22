package com.development.legally.data.model

data class Reminder(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val alertBefore: String = "", // "24 horas", "48 horas", "3 días", "1 semana"
    val caseId: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)