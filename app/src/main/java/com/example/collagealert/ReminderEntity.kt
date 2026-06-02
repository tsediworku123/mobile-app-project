package com.example.collagealert

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val dateTime: Long, // Timestamp for when the reminder should trigger
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
