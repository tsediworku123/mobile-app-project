package com.example.collagealert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainViewModel(
    application: Application,
    private val alertRepository: AlertRepository,
    private val reminderRepository: ReminderRepository
) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    val allAlerts: LiveData<List<AlertEntity>> = alertRepository.allAlerts
    val unreadCount: LiveData<Int> = alertRepository.unreadCount
    val totalCount: LiveData<Int> = alertRepository.totalCount

    val upcomingReminders: LiveData<List<ReminderEntity>> = reminderRepository.getUpcomingReminders(System.currentTimeMillis())

    private val _alerts = MutableLiveData<List<AlertData>>()
    val alerts: LiveData<List<AlertData>> = _alerts

    private val _typeCounts = MutableLiveData<Map<AlertType, Int>>()
    val typeCounts: LiveData<Map<AlertType, Int>> = _typeCounts

    init {
        allAlerts.observeForever { entities ->
            val alertDataList = entities.map { it.toAlertData() }
            _alerts.postValue(alertDataList)
            updateTypeCounts(alertDataList)
        }
    }

    private fun updateTypeCounts(alertList: List<AlertData>) {
        val counts = mutableMapOf<AlertType, Int>()
        AlertType.entries.forEach { type ->
            counts[type] = alertList.count { it.type == type }
        }
        _typeCounts.postValue(counts)
    }

    // Alert Operations
    fun addAlert(
        type: AlertType, 
        title: String, 
        message: String, 
        priority: Priority, 
        createdBy: String = "Student", 
        timestamp: Long = System.currentTimeMillis(),
        id: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val alertId = id ?: UUID.randomUUID().toString()
            val existing = alertRepository.getAlertById(alertId)
            if (existing != null) return@launch

            val newAlert = AlertData(
                id = alertId,
                title = title,
                message = message,
                type = type,
                priority = priority,
                timestamp = timestamp,
                isRead = false,
                createdBy = createdBy
            )
            alertRepository.insert(newAlert)
        }
    }

    fun markAlertAsRead(alert: AlertData) {
        viewModelScope.launch(Dispatchers.IO) {
            alertRepository.update(alert.copy(isRead = true))
        }
    }

    fun markLatestAsRead() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentAlerts = _alerts.value
            if (!currentAlerts.isNullOrEmpty()) {
                val latest = currentAlerts.first()
                alertRepository.update(latest.copy(isRead = true))
            }
        }
    }

    fun deleteAlert(alert: AlertData) {
        viewModelScope.launch(Dispatchers.IO) {
            alertRepository.delete(alert)
        }
    }

    fun clearAllAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            alertRepository.deleteAll()
        }
    }

    // Reminder Operations
    fun addReminder(title: String, description: String, dateTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val newReminder = ReminderEntity(
                title = title,
                description = description,
                dateTime = dateTime
            )
            val id = reminderRepository.insert(newReminder)
            scheduleReminderNotification(id, title, description, dateTime)
        }
    }

    private fun scheduleReminderNotification(id: Long, title: String, description: String, dateTime: Long) {
        val delay = dateTime - System.currentTimeMillis()
        if (delay > 0) {
            val data = Data.Builder()
                .putLong("reminder_id", id)
                .putString("title", title)
                .putString("message", description)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("reminder_$id")
                .build()

            workManager.enqueue(workRequest)
        }
    }

    fun updateReminderCompletion(id: Long, completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.updateCompletionStatus(id, completed)
            if (completed) {
                workManager.cancelAllWorkByTag("reminder_$id")
            }
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.delete(reminder)
            workManager.cancelAllWorkByTag("reminder_${reminder.id}")
        }
    }
}