package com.development.legally.ui.cases

data class LegalCase(
    val id: String,
    val caseNumber: String,
    val status: String,
    val description: String,
    val updatedDate: String
)