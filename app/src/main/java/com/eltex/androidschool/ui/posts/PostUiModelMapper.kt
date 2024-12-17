package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.data.posts.PostData

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PostUiModelMapper {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH.mm")
    }

    fun map(post: PostData): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}
