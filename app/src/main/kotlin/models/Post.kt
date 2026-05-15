package com.mobileapp.models

/**
 * Data class representing a Post in the social feed
 * Contains post content and engagement metrics
 */
data class Post(
    val id: String,
    val authorId: String,
    val authorName: String,
    val content: String,
    val imageUrls: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val tags: List<String> = emptyList(),
    val isLiked: Boolean = false,
    val visibility: PostVisibility = PostVisibility.PUBLIC
) {
    /**
     * Check if post has media attachments
     */
    fun hasMedia(): Boolean = imageUrls.isNotEmpty()
    
    /**
     * Get engagement score
     */
    fun getEngagementScore(): Int = likesCount + (commentsCount * 2) + (sharesCount * 3)
    
    /**
     * Get formatted engagement count
     */
    fun getEngagementCount(): String = "${likesCount + commentsCount + sharesCount}"
}

enum class PostVisibility {
    PUBLIC, FRIENDS_ONLY, PRIVATE
}
