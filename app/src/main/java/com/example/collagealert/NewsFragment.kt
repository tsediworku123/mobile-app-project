package com.example.collagealert

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collagealert.databinding.DialogAddNewsBinding
import com.example.collagealert.databinding.DialogCommentsBinding
import com.example.collagealert.databinding.FragmentNewsBinding
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: NoticeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("news")
        
        setupRecyclerView()
        setupFab()
        fetchNews()
    }

    private fun setupRecyclerView() {
        adapter = NoticeAdapter(
            notices = emptyList(),
            currentUserId = auth.currentUser?.uid ?: "",
            isAdmin = false,
            onDeleteClick = { notice -> showDeleteConfirmation(notice) },
            onLikeClick = { notice -> handleLike(notice) },
            onDislikeClick = { notice -> handleDislike(notice) },
            onCommentClick = { notice -> showCommentsDialog(notice) },
            onShareClick = { notice -> handleShare(notice) }
        )
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.newsRecyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.addNewsFab.setOnClickListener {
            showAddNewsDialog()
        }
    }

    private fun showAddNewsDialog() {
        val dialogBinding = DialogAddNewsBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Post", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val title = dialogBinding.titleEditText.text.toString().trim()
                val message = dialogBinding.messageEditText.text.toString().trim()
                
                if (title.isEmpty() || message.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                postNews(title, message, "NEWS")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun postNews(title: String, message: String, type: String) {
        val user = auth.currentUser ?: return
        val newsId = database.push().key ?: return
        
        // Fetch user name from database
        FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("name").get()
            .addOnSuccessListener { snapshot ->
                val userName = snapshot.getValue(String::class.java) ?: user.email?.substringBefore("@") ?: "Student"
                
                val news = RealtimeAlert(
                    id = newsId,
                    title = title,
                    message = message,
                    type = type,
                    priority = "NORMAL",
                    timestamp = System.currentTimeMillis(),
                    createdBy = user.uid,
                    createdByName = userName
                )

                database.child(newsId).setValue(news)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "News posted successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to post news", Toast.LENGTH_SHORT).show()
                    }
            }
    }

    private fun fetchNews() {
        val currentUid = auth.currentUser?.uid ?: ""
        // Check if admin to set delete permissions correctly in adapter
        FirebaseDatabase.getInstance().reference.child("users").child(currentUid).child("role").get()
            .addOnSuccessListener { snapshot ->
                val isAdmin = snapshot.getValue(String::class.java) == "admin"
                
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<RealtimeAlert>()
                        for (child in snapshot.children) {
                            child.getValue(RealtimeAlert::class.java)?.let { list.add(it) }
                        }
                        adapter.updateData(list.reversed(), currentUid, isAdmin)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
    }

    private fun showDeleteConfirmation(notice: RealtimeAlert) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete News")
            .setMessage("Are you sure you want to delete this news post?")
            .setPositiveButton("Delete") { _, _ ->
                database.child(notice.id).removeValue()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun handleLike(notice: RealtimeAlert) {
        val uid = auth.currentUser?.uid ?: return
        val likeRef = database.child(notice.id).child("likes").child(uid)
        
        if (notice.likes.containsKey(uid)) {
            likeRef.removeValue()
        } else {
            likeRef.setValue(true)
            // Remove dislike if exists
            if (notice.dislikes.containsKey(uid)) {
                database.child(notice.id).child("dislikes").child(uid).removeValue()
            }
            // Notify author
            if (notice.createdBy != uid) {
                NotificationHelper.sendNotification(
                    notice.createdBy,
                    "LIKE",
                    "New Like! ❤️",
                    "Someone liked your news: ${notice.title}",
                    notice.id
                )
            }
        }
    }

    private fun handleDislike(notice: RealtimeAlert) {
        val uid = auth.currentUser?.uid ?: return
        val dislikeRef = database.child(notice.id).child("dislikes").child(uid)
        
        if (notice.dislikes.containsKey(uid)) {
            dislikeRef.removeValue()
        } else {
            dislikeRef.setValue(true)
            // Remove like if exists
            if (notice.likes.containsKey(uid)) {
                database.child(notice.id).child("likes").child(uid).removeValue()
            }
            // Notify author
            if (notice.createdBy != uid) {
                NotificationHelper.sendNotification(
                    notice.createdBy,
                    "DISLIKE",
                    "New Dislike! 👎",
                    "Someone disliked your news: ${notice.title}",
                    notice.id
                )
            }
        }
    }

    private fun showCommentsDialog(notice: RealtimeAlert) {
        val dialogBinding = DialogCommentsBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        val commentAdapter = CommentAdapter(notice.comments.values.toList().sortedByDescending { it.timestamp })
        dialogBinding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.commentsRecyclerView.adapter = commentAdapter

        dialogBinding.sendCommentButton.setOnClickListener {
            val text = dialogBinding.commentEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                postComment(notice, text)
                dialogBinding.commentEditText.text?.clear()
            }
        }

        // Listen for comment changes
        val commentsRef = database.child(notice.id).child("comments")
        val listener = commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Comment>()
                for (child in snapshot.children) {
                    child.getValue(Comment::class.java)?.let { list.add(it) }
                }
                commentAdapter.updateData(list.sortedByDescending { it.timestamp })
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        dialog.setOnDismissListener { commentsRef.removeEventListener(listener) }
        dialog.show()
    }

    private fun postComment(notice: RealtimeAlert, text: String) {
        val user = auth.currentUser ?: return
        val commentId = database.child(notice.id).child("comments").push().key ?: return
        
        FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("name").get()
            .addOnSuccessListener { snapshot ->
                val userName = snapshot.getValue(String::class.java) ?: "Student"
                val comment = Comment(commentId, user.uid, userName, text, System.currentTimeMillis())
                database.child(notice.id).child("comments").child(commentId).setValue(comment)
                
                // Notify author
                if (notice.createdBy != user.uid) {
                    NotificationHelper.sendNotification(
                        notice.createdBy,
                        "COMMENT",
                        "New Comment! 💬",
                        "$userName commented on your news: ${notice.title}",
                        notice.id
                    )
                }
            }
    }

    private fun handleShare(notice: RealtimeAlert) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, notice.title)
            putExtra(Intent.EXTRA_TEXT, "${notice.title}\n\n${notice.message}\n\nPosted by: ${notice.createdByName}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share News via"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
