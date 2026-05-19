package com.mobileapp.utils

import android.util.Log

/**
 * Simple logging utility for the mobile application
 * Provides structured logging capabilities for debugging and monitoring
 */
object Logger {
    private const val TAG = "MobileApp"
    
    enum class Level {
        DEBUG, INFO, WARNING, ERROR
    }
    
    fun log(level: Level, message: String, throwable: Throwable? = null) {
        val logMessage = "[$level] $message"
        when (level) {
            Level.DEBUG -> Log.d(TAG, logMessage)
            Level.INFO -> Log.i(TAG, logMessage)
            Level.WARNING -> Log.w(TAG, logMessage)
            Level.ERROR -> {
                if (throwable != null) {
                    Log.e(TAG, logMessage, throwable)
                } else {
                    Log.e(TAG, logMessage)
                }
            }
        }
    }
    
    fun debug(message: String) = log(Level.DEBUG, message)
    fun info(message: String) = log(Level.INFO, message)
    fun warning(message: String) = log(Level.WARNING, message)
    fun error(message: String, throwable: Throwable? = null) = log(Level.ERROR, message, throwable)
}
