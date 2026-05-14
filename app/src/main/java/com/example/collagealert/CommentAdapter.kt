package com.example.collagealert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(private var comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    fun updateData(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.commentUserName)
        private val timeTextView: TextView = itemView.findViewById(R.id.commentTime)
        private val textTextView: TextView = itemView.findViewById(R.id.commentText)

        fun bind(comment: Comment) {
            nameTextView.text = comment.userName
            textTextView.text = comment.text
            
            val date = Date(comment.timestamp)
            val format = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            timeTextView.text = format.format(date)
        }
    }
}
