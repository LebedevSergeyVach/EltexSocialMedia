package com.eltex.androidschool.adapter.comments

data class CommentPayload(
    val likeByMe: Boolean? = null,
    val likes: Int? = null,
) {
    fun isNotEmpty(): Boolean = likeByMe != null || likes != null
}
