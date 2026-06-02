package com.example.collagealert

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val application: Application,
    private val alertRepository: AlertRepository,
    private val reminderRepository: ReminderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.example.collagealert.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application, alertRepository, reminderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}