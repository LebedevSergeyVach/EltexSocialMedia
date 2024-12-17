package com.eltex.androidschool.ui.posts

data class PostUiModel(
    val id: Long = 0L,
    val author: String = "",
    val published: String = "",
    val lastModified: String? = null,
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)
