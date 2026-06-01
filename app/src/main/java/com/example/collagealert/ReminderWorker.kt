package com.example.collagealert

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val reminderId = inputData.getLong("reminder_id", -1)
        val title = inputData.getString("title") ?: "Reminder"
        val message = inputData.getString("message") ?: "You have a reminder!"

        if (reminderId == -1L) return Result.failure()

        val db = AppDatabase.getDatabase(applicationContext)
        val reminder = db.reminderDao().getReminderById(reminderId)

        // Only show notification if the reminder is not completed and still exists
        if (reminder != null && !reminder.isCompleted) {
            NotificationHelper.showLocalNotification(
                applicationContext,
                "⏰ Reminder: $title",
                message
            )
        }

        return Result.success()
    }
}
