package com.example.collagealert

data class AppNotification(
    val id: String = "",
    val type: String = "NOTICE",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val relatedId: String = ""
)
