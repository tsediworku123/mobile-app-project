package com.example.collagealert

import androidx.lifecycle.LiveData

class ReminderRepository(private val reminderDao: ReminderDao) {

    val allReminders: LiveData<List<ReminderEntity>> = reminderDao.getAllReminders()

    fun getUpcomingReminders(currentTime: Long): LiveData<List<ReminderEntity>> {
        return reminderDao.getUpcomingReminders(currentTime)
    }

    suspend fun insert(reminder: ReminderEntity): Long {
        return reminderDao.insert(reminder)
    }

    suspend fun update(reminder: ReminderEntity) {
        reminderDao.update(reminder)
    }

    suspend fun delete(reminder: ReminderEntity) {
        reminderDao.delete(reminder)
    }

    suspend fun updateCompletionStatus(id: Long, completed: Boolean) {
        reminderDao.updateCompletionStatus(id, completed)
    }
}
