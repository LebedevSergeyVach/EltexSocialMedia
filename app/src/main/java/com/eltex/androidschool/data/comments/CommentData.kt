package com.eltex.androidschool.data.comments

import com.eltex.androidschool.data.common.InstantSerializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.Instant

@Serializable
data class CommentData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("postId")
    val postId: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorId")
    val authorId: Long = 0L,
    @SerialName("authorAvatar")
    val authorAvatar: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("published")
    @Serializable(with = InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
)
