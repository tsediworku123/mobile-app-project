package com.example.collagealert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase

object NotificationHelper {

    private const val CHANNEL_ID = "college_activity_channel"

    fun sendNotification(toUid: String, type: String, title: String, message: String, relatedId: String = "") {
        val database = FirebaseDatabase.getInstance().reference
        val notifId = database.child("users").child(toUid).child("notifications").push().key ?: return
        
        val notification = AppNotification(
            id = notifId,
            type = type,
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            relatedId = relatedId
        )
        
        database.child("users").child(toUid).child("notifications").child(notifId).setValue(notification)
    }

    fun broadcastNotice(title: String, message: String, noticeId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val globalNotifId = database.child("global_notifications").push().key ?: return
        val notification = AppNotification(
            id = globalNotifId,
            type = "NOTICE",
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            relatedId = noticeId
        )
        database.child("global_notifications").child(globalNotifId).setValue(notification)
    }

    fun showLocalNotification(context: Context, title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "College Activity",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
