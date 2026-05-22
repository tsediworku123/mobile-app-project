package com.example.collagealert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private var notifications: List<AppNotification>,
    private val onClick: (AppNotification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size

    fun updateData(newNotifications: List<AppNotification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.notifTitle)
        private val messageTextView: TextView = itemView.findViewById(R.id.notifMessage)
        private val timeTextView: TextView = itemView.findViewById(R.id.notifTime)
        private val statusIndicator: View = itemView.findViewById(R.id.statusIndicator)
        private val notifIcon: ImageView = itemView.findViewById(R.id.notifIcon)
        private val iconBg: View = itemView.findViewById(R.id.iconBg)

        fun bind(notification: AppNotification) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
            
            val date = Date(notification.timestamp)
            val format = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            timeTextView.text = format.format(date)
            
            statusIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

            val (iconRes, iconTint, bgTint) = when (notification.type) {
                "LIKE" -> Triple(R.drawable.ic_like, R.color.priority_high, R.color.priority_high_bg)
                "DISLIKE" -> Triple(R.drawable.ic_dislike, R.color.secondary, R.color.secondary_light)
                "COMMENT" -> Triple(R.drawable.ic_comment, R.color.primary, R.color.primary_light)
                "NOTICE" -> Triple(R.drawable.ic_notification, R.color.warning, R.color.priority_medium_bg)
                else -> Triple(R.drawable.ic_notification, R.color.primary, R.color.primary_light)
            }

            notifIcon.setImageResource(iconRes)
            notifIcon.setColorFilter(ContextCompat.getColor(itemView.context, iconTint))
            iconBg.backgroundTintList = ContextCompat.getColorStateList(itemView.context, bgTint)
            
            itemView.setOnClickListener { onClick(notification) }
        }
    }
}
