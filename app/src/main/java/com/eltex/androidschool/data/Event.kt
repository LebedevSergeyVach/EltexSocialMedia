package com.eltex.androidschool.data


data class Event (
    val id: Long = 0L,
    val author: String = "",
    val published: String = "",
    val optionConducting: String = "",
    val dataEvent: String = "",
    val content: String = "",
    val link: String = "",
    val likeByMe: Boolean = false,
    val participateByMe: Boolean = false,
)
