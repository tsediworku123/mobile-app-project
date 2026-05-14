package com.mobileapp.utils

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
            Level.DEBUG -> println("DEBUG: $logMessage")
            Level.INFO -> println("INFO: $logMessage")
            Level.WARNING -> println("WARN: $logMessage")
            Level.ERROR -> {
                println("ERROR: $logMessage")
                throwable?.printStackTrace()
            }
        }
    }
    
    fun debug(message: String) = log(Level.DEBUG, message)
    fun info(message: String) = log(Level.INFO, message)
    fun warning(message: String) = log(Level.WARNING, message)
    fun error(message: String, throwable: Throwable? = null) = log(Level.ERROR, message, throwable)
}
