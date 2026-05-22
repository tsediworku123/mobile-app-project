package com.example.collagealert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collagealert.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()
        fetchNotifications(uid)
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(emptyList()) { notification ->
            markAsRead(notification)
        }
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRecyclerView.adapter = adapter
    }

    private fun fetchNotifications(uid: String) {
        database.child("users").child(uid).child("notifications")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<AppNotification>()
                    for (child in snapshot.children) {
                        child.getValue(AppNotification::class.java)?.let { list.add(it) }
                    }
                    val sortedList = list.sortedByDescending { it.timestamp }
                    adapter.updateData(sortedList)
                    binding.emptyStateLayout.visibility = if (sortedList.isEmpty()) View.VISIBLE else View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun markAsRead(notification: AppNotification) {
        val uid = auth.currentUser?.uid ?: return
        database.child("users").child(uid).child("notifications").child(notification.id).child("isRead").setValue(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
