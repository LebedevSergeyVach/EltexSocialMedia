package com.eltex.androidschool.ui.comments

import com.eltex.androidschool.data.comments.CommentData
import com.eltex.androidschool.ui.common.DateTimeUiFormatter

import javax.inject.Inject

class CommentUiModelMapper @Inject constructor(
    private val dateTimeUiFormatter: DateTimeUiFormatter
) {

    fun map(comment: CommentData): CommentUiModel = with(comment) {
        CommentUiModel(
            id = id,
            postId = postId,
            author = author,
            authorId = authorId,
            authorAvatar = authorAvatar,
            content = content,
            published = dateTimeUiFormatter.format(instant = published),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}
