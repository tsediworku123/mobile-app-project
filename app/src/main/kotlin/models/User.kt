package com.mobileapp.models

/**
 * Data class representing a User in the mobile application
 * Contains user profile information and authentication details
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isVerified: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0
) {
    /**
     * Get the user's full name
     */
    fun getFullName(): String = "$firstName $lastName"
    
    /**
     * Check if user's email is verified
     */
    fun isEmailVerified(): Boolean = isVerified
    
    /**
     * Get user's display name (preferred name)
     */
    fun getDisplayName(): String = firstName.ifEmpty { username }
}
