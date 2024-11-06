package com.eltex.androidschool.data

data class Post(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val likeByMe: Boolean = false,
)
