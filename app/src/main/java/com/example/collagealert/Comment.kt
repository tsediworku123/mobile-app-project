package com.example.collagealert

data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
