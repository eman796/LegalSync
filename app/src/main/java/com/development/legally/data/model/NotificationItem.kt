package com.development.legally.data.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val eventId: String,
    val timestamp: Long
)
