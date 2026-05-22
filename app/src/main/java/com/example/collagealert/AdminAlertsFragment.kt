package com.example.collagealert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collagealert.databinding.FragmentAdminAlertsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminAlertsFragment : Fragment() {

    private var _binding: FragmentAdminAlertsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var noticeAdapter: NoticeAdapter
    private val noticesList = mutableListOf<RealtimeAlert>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()
        setupClickListeners()
        loadNotices()
    }

    private fun setupRecyclerView() {
        noticeAdapter = NoticeAdapter(
            notices = noticesList,
            currentUserId = auth.currentUser?.uid ?: "",
            isAdmin = true,
            onDeleteClick = { notice -> showDeleteConfirmation(notice) }
        )
        binding.noticesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.noticesRecyclerView.adapter = noticeAdapter
    }

    private fun loadNotices() {
        database.child("notices").orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noticesList.clear()
                    for (noticeSnapshot in snapshot.children) {
                        val notice = noticeSnapshot.getValue(RealtimeAlert::class.java)
                        notice?.let { noticesList.add(0, it) }
                    }
                    if (_binding != null) {
                        noticeAdapter.updateData(noticesList, auth.currentUser?.uid ?: "", true)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showDeleteConfirmation(notice: RealtimeAlert) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Notice")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete") { _, _ -> deleteNotice(notice) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNotice(notice: RealtimeAlert) {
        database.child("notices").child(notice.id).removeValue()
            .addOnSuccessListener { Toast.makeText(requireContext(), "Notice deleted", Toast.LENGTH_SHORT).show() }
    }

    private fun setupClickListeners() {
        binding.logoutButton.setOnClickListener { showLogoutConfirmation() }
        binding.sendAlertButton.setOnClickListener { sendAlert() }
    }



    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                auth.signOut()
                (activity as? AdminActivity)?.navigateToLogin()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendAlert() {
        val title = binding.titleEditText.text.toString().trim()
        val message = binding.messageEditText.text.toString().trim()

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val type = when (binding.typeChipGroup.checkedChipId) {
            R.id.examChip -> "EXAM"
            R.id.seminarChip -> "SEMINAR"
            R.id.holidayChip -> "HOLIDAY"
            R.id.noticeChip -> "NOTICE"
            R.id.urgentChip -> "URGENT"
            else -> "GENERAL"
        }

        val priority = when (binding.priorityRadioGroup.checkedRadioButtonId) {
            R.id.highPriority -> "HIGH"
            R.id.mediumPriority -> "MEDIUM"
            else -> "LOW"
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.sendAlertButton.isEnabled = false

        val alertId = database.child("notices").push().key ?: return
        val alert = RealtimeAlert(alertId, title, message, type, priority, System.currentTimeMillis(), auth.currentUser?.uid ?: "", "Admin")

        database.child("notices").child(alertId).setValue(alert)
            .addOnSuccessListener {
                NotificationHelper.broadcastNotice(title, message, alertId)
                Toast.makeText(requireContext(), "✅ Alert Sent!", Toast.LENGTH_SHORT).show()
                binding.titleEditText.text?.clear()
                binding.messageEditText.text?.clear()
                binding.progressBar.visibility = View.GONE
                binding.sendAlertButton.isEnabled = true
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                binding.sendAlertButton.isEnabled = true
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
