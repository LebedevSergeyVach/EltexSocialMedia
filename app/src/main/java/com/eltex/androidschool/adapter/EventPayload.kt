package com.eltex.androidschool.adapter

data class EventPayload(
    val likeByMe: Boolean? = null,
    val participateByMe: Boolean? = null
) {
    fun isNotEmpty(): Boolean = likeByMe != null || participateByMe != null
}
