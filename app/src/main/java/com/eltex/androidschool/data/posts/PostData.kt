package com.eltex.androidschool.data.posts

import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.data.common.InstantSerializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.Instant

/**
 * Класс, представляющий пост в приложении.
 * Этот класс используется для хранения данных о посте, включая его идентификатор, автора, дату публикации,
 * содержание и информацию о лайках.
 *
 * @property id Уникальный идентификатор поста. По умолчанию 0L.
 * @property author Имя автора поста. По умолчанию пустая строка.
 * @property authorId Уникальный идентификатор автора поста. По умолчанию 0L.
 * @property authorAvatar Ссылка на аватар автора поста. По умолчанию пустая строка.
 * @property published Дата и время публикации поста. По умолчанию текущее время.
 * @property content Текстовое содержание поста. По умолчанию пустая строка.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. По умолчанию false.
 * @property likeOwnerIds Множество идентификаторов пользователей, которые лайкнули этот пост. По умолчанию пустое множество.
 * @property attachment Вложение (например, изображение или видео), связанное с постом. По умолчанию null.
 *
 * @see Attachment
 * @see InstantSerializer
 */
@Serializable
data class PostData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorId")
    val authorId: Long = 0L,
    @SerialName("authorAvatar")
    val authorAvatar: String = "",
    @SerialName("published")
    @Serializable(with = InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("content")
    val content: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("attachment")
    val attachment: Attachment? = null,
)
