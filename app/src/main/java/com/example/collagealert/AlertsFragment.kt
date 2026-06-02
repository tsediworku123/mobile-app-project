package com.example.collagealert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collagealert.databinding.FragmentAlertsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: MainViewModel
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupDatabase()
        setupRecyclerViews()
        setupClickListeners()
        setupObservers()
        updateUIInfo()
        listenForCloudAlerts()
    }

    private fun setupDatabase() {
        val application = requireActivity().application
        val db = AppDatabase.getDatabase(requireContext())
        val alertRepository = AlertRepository(db.alertDao())
        val reminderRepository = ReminderRepository(db.reminderDao())
        val factory = MainViewModelFactory(application, alertRepository, reminderRepository)
        viewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        // Alerts RecyclerView
        alertAdapter = AlertAdapter(emptyList(), { showAlertDetails(it) }, { showAlertOptions(it) })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = alertAdapter

        // Reminders RecyclerView
        reminderAdapter = ReminderAdapter(
            onCheckedChange = { reminder, isChecked ->
                viewModel.updateReminderCompletion(reminder.id, isChecked)
            },
            onLongClick = { reminder ->
                showReminderOptionsDialog(reminder)
            }
        )
        binding.remindersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.remindersRecyclerView.adapter = reminderAdapter
    }

    private fun setupClickListeners() {
        binding.themeToggle.setOnClickListener { toggleTheme() }

        binding.addReminderButton.setOnClickListener { showReminderDialog(null) }

        binding.examCard.setOnClickListener {
            viewModel.addAlert(AlertType.EXAM, "Mid-term Exams", "Schedule published", Priority.HIGH, "Student")
        }

        binding.seminarCard.setOnClickListener {
            viewModel.addAlert(AlertType.SEMINAR, "AI Guest Lecture", "Mr. Solomon from AAU", Priority.MEDIUM, "Student")
        }

        binding.holidayCard.setOnClickListener {
            viewModel.addAlert(AlertType.HOLIDAY, "Easter Break", "Campus closed April 15-22", Priority.LOW, "Student")
        }

        binding.noticeCard.setOnClickListener {
            viewModel.addAlert(AlertType.NOTICE, "Library Hours", "Extended hours during exams", Priority.NORMAL, "Student")
        }

        binding.urgentCard.setOnClickListener {
            viewModel.addAlert(AlertType.URGENT, "Campus Emergency", "Evacuation drill", Priority.HIGH, "Student")
        }

        binding.markReadCard.setOnClickListener {
            viewModel.markLatestAsRead()
            Toast.makeText(requireContext(), "Latest alert marked as read", Toast.LENGTH_SHORT).show()
        }

        binding.clearButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear All")
                .setMessage("Delete all alerts?")
                .setPositiveButton("Yes") { _, _ -> viewModel.clearAllAlerts() }
                .setNegativeButton("No", null)
                .show()
        }

        binding.showStatsButton.setOnClickListener { showStatistics() }
    }

    private fun setupObservers() {
        viewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            alertAdapter.updateData(alerts)
        }
        
        viewModel.unreadCount.observe(viewLifecycleOwner) { count -> 
            binding.statsNumber.text = count.toString() 
        }

        viewModel.upcomingReminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
            binding.noRemindersText.visibility = if (reminders.isEmpty()) View.VISIBLE else View.GONE
            binding.remindersScroll.visibility = if (reminders.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun showReminderOptionsDialog(reminder: ReminderEntity) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(requireContext())
            .setTitle("Reminder Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showReminderDialog(reminder)
                    1 -> viewModel.deleteReminder(reminder)
                }
            }
            .show()
    }

    private fun showReminderDialog(reminder: ReminderEntity?) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(if (reminder == null) "Add Personal Reminder" else "Edit Reminder")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 10)

        val titleInput = EditText(context)
        titleInput.hint = "Title (e.g., Submit Assignment)"
        if (reminder != null) titleInput.setText(reminder.title)
        layout.addView(titleInput)

        val descInput = EditText(context)
        descInput.hint = "Description (Optional)"
        if (reminder != null) descInput.setText(reminder.description)
        layout.addView(descInput)

        builder.setView(layout)

        builder.setPositiveButton("Next") { _, _ ->
            val title = titleInput.text.toString()
            if (title.isBlank()) {
                Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            showDateTimePicker(reminder, title, descInput.text.toString())
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showDateTimePicker(reminder: ReminderEntity?, title: String, description: String) {
        val currentCalendar = Calendar.getInstance()
        if (reminder != null) currentCalendar.timeInMillis = reminder.dateTime

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute)
                
                if (reminder == null) {
                    viewModel.addReminder(title, description, calendar.timeInMillis)
                    Toast.makeText(requireContext(), "Reminder added!", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedReminder = reminder.copy(
                        title = title,
                        description = description,
                        dateTime = calendar.timeInMillis
                    )
                    viewModel.updateReminder(updatedReminder)
                    Toast.makeText(requireContext(), "Reminder updated!", Toast.LENGTH_SHORT).show()
                }
            }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), false).show()
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun toggleTheme() {
        val mode = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun listenForCloudAlerts() {
        database.child("notices").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(RealtimeAlert::class.java)?.let {
                    val data = it.toAlertData()
                    viewModel.addAlert(data.type, data.title, data.message, data.priority, data.createdBy, it.timestamp, it.id)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateUIInfo() {
        val sdf = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
        binding.dateText.text = sdf.format(Date())
        val email = auth.currentUser?.email ?: "Student"
        binding.userNameText.text = email.substringBefore("@").replaceFirstChar { it.uppercase() }
    }

    private fun showAlertDetails(alert: AlertData) {
        AlertDialog.Builder(requireContext())
            .setTitle("${alert.typeEmoji} ${alert.title}")
            .setMessage(alert.message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAlertOptions(alert: AlertData) {
        val options = if (alert.isRead) {
            arrayOf("Mark as Unread", "Delete")
        } else {
            arrayOf("Mark as Read", "Delete")
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Options")
            .setItems(options) { _, which ->
                if (which == 0) {
                    if (alert.isRead) {
                        viewModel.markAlertAsUnread(alert)
                        Toast.makeText(requireContext(), "Alert marked as unread", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.markAlertAsRead(alert)
                        Toast.makeText(requireContext(), "Alert marked as read", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.deleteAlert(alert)
                }
            }.show()
    }

    private fun showStatistics() {
        val total = viewModel.totalCount.value ?: 0
        val unread = viewModel.unreadCount.value ?: 0
        AlertDialog.Builder(requireContext())
            .setTitle("Statistics")
            .setMessage("Total Alerts: $total\nUnread Alerts: $unread")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
