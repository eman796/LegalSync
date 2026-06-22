package com.development.legally.data.model

data class FollowUp(
    val id: String = "",
    val caseId: String = "",
    val date: String = "",
    val description: String = "",
    val responsibleUser: String = "",
    val createdAt: Long = System.currentTimeMillis()
)