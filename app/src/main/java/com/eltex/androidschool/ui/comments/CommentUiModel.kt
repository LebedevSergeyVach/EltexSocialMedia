package com.eltex.androidschool.ui.comments

data class CommentUiModel(
    val id: Long = 0L,
    val postId: Long = 0L,
    val author: String = "",
    val authorId: Long = 0L,
    val authorAvatar: String? = null,
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)
