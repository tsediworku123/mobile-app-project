package com.example.collagealert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoticeAdapter(
    private var notices: List<RealtimeAlert>,
    private var currentUserId: String = "",
    private var isAdmin: Boolean = false,
    private val onDeleteClick: (RealtimeAlert) -> Unit,
    private val onLikeClick: ((RealtimeAlert) -> Unit)? = null,
    private val onDislikeClick: ((RealtimeAlert) -> Unit)? = null,
    private val onCommentClick: ((RealtimeAlert) -> Unit)? = null,
    private val onShareClick: ((RealtimeAlert) -> Unit)? = null
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notica, parent, false)
        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(notices[position])
    }

    override fun getItemCount() = notices.size

    fun updateData(newNotices: List<RealtimeAlert>, userId: String = currentUserId, admin: Boolean = isAdmin) {
        notices = newNotices
        currentUserId = userId
        isAdmin = admin
        notifyDataSetChanged()
    }

    inner class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        private val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        private val deleteButton: MaterialButton = itemView.findViewById(R.id.deleteButton)
        private val typeEmojiTextView: TextView = itemView.findViewById(R.id.typeEmojiTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val priorityBar: View = itemView.findViewById(R.id.priorityBar)
        private val likeButton: MaterialButton = itemView.findViewById(R.id.likeButton)
        private val dislikeButton: MaterialButton = itemView.findViewById(R.id.dislikeButton)
        private val commentButton: MaterialButton = itemView.findViewById(R.id.commentButton)
        private val shareButton: MaterialButton = itemView.findViewById(R.id.shareButton)
        private val interactionLayout: View = itemView.findViewById(R.id.interactionLayout)

        fun bind(notice: RealtimeAlert) {
            titleTextView.text = notice.title
            messageTextView.text = notice.message
            typeTextView.text = notice.type
            priorityTextView.text = notice.priority

            typeEmojiTextView.text = when (notice.type) {
                "EXAM" -> "📝"
                "SEMINAR" -> "🎯"
                "HOLIDAY" -> "🏖️"
                "NOTICE" -> "📢"
                "URGENT" -> "🚨"
                else -> "📌"
            }

            val date = Date(notice.timestamp)
            val format = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            timeTextView.text = format.format(date)

            val (barColor, bgColor) = when (notice.priority) {
                "HIGH" -> Pair(R.color.priority_high, R.color.priority_high_bg)
                "MEDIUM" -> Pair(R.color.priority_medium, R.color.priority_medium_bg)
                "LOW" -> Pair(R.color.priority_low, R.color.priority_low_bg)
                else -> Pair(R.color.primary, R.color.priority_normal_bg)
            }

            priorityBar.setBackgroundColor(ContextCompat.getColor(itemView.context, barColor))
            cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, bgColor))
            priorityTextView.setTextColor(ContextCompat.getColor(itemView.context, barColor))

            // Hide category-related views if it's News
            if (notice.type == "NEWS") {
                itemView.findViewById<View>(R.id.labelsLayout).visibility = View.GONE
                priorityBar.visibility = View.GONE
                typeEmojiTextView.visibility = View.GONE
            } else {
                itemView.findViewById<View>(R.id.labelsLayout).visibility = View.VISIBLE
                priorityBar.visibility = View.VISIBLE
                typeEmojiTextView.visibility = View.VISIBLE
            }

            authorTextView.text = if (notice.createdByName.isNotEmpty()) "By: ${notice.createdByName}" else "By: Anonymous"

            deleteButton.visibility = if (isAdmin || notice.createdBy == currentUserId) View.VISIBLE else View.GONE

            deleteButton.setOnClickListener {
                onDeleteClick(notice)
            }

            // Interaction Bar Logic
            if (notice.type == "NEWS") {
                interactionLayout.visibility = View.VISIBLE

                val likeCount = notice.likes.size
                likeButton.text = likeCount.toString()
                val isLiked = notice.likes.containsKey(currentUserId)
                if (isLiked) {
                    likeButton.setIconResource(R.drawable.ic_like)
                    likeButton.setIconTintResource(R.color.priority_high)
                    likeButton.setTextColor(ContextCompat.getColor(itemView.context, R.color.priority_high))
                } else {
                    likeButton.setIconResource(R.drawable.ic_like)
                    likeButton.setIconTintResource(R.color.text_hint)
                    likeButton.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_hint))
                }

                val dislikeCount = notice.dislikes.size
                dislikeButton.text = dislikeCount.toString()
                val isDisliked = notice.dislikes.containsKey(currentUserId)
                if (isDisliked) {
                    dislikeButton.setIconResource(R.drawable.ic_dislike)
                    dislikeButton.setIconTintResource(R.color.secondary)
                    dislikeButton.setTextColor(ContextCompat.getColor(itemView.context, R.color.secondary))
                } else {
                    dislikeButton.setIconResource(R.drawable.ic_dislike)
                    dislikeButton.setIconTintResource(R.color.text_hint)
                    dislikeButton.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_hint))
                }

                val commentCount = notice.comments.size
                commentButton.text = if (commentCount > 0) "$commentCount" else "Comment"

                likeButton.setOnClickListener { onLikeClick?.invoke(notice) }
                dislikeButton.setOnClickListener { onDislikeClick?.invoke(notice) }
                commentButton.setOnClickListener { onCommentClick?.invoke(notice) }
                shareButton.setOnClickListener { onShareClick?.invoke(notice) }
            } else {
                interactionLayout.visibility = View.GONE
            }
        }
    }
}
