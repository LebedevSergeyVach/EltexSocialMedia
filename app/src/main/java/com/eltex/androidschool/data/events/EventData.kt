package com.eltex.androidschool.data.events

import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.data.common.InstantSerializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.Instant

/**
 * Класс, представляющий событие в приложении.
 * Этот класс используется для хранения данных о событии, включая его идентификатор, автора, дату публикации,
 * содержание, ссылку на событие и информацию о лайках и участниках.
 *
 * @property id Уникальный идентификатор события. По умолчанию 0L.
 * @property author Имя автора события. По умолчанию пустая строка.
 * @property authorId Уникальный идентификатор автора события. По умолчанию 0L.
 * @property authorAvatar Ссылка на аватар автора события. По умолчанию пустая строка.
 * @property published Дата и время публикации события. По умолчанию текущее время.
 * @property optionConducting Вариант проведения события (например, онлайн или оффлайн). По умолчанию пустая строка.
 * @property dataEvent Дата и время проведения события. По умолчанию текущее время.
 * @property content Текстовое содержание события. По умолчанию пустая строка.
 * @property link Ссылка на событие (например, ссылка на внешний ресурс). По умолчанию пустая строка.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие. По умолчанию false.
 * @property participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии. По умолчанию false.
 * @property likeOwnerIds Множество идентификаторов пользователей, которые лайкнули это событие. По умолчанию пустое множество.
 * @property participantsIds Множество идентификаторов пользователей, которые участвуют в этом событии. По умолчанию пустое множество.
 * @property attachment Вложение (например, изображение или видео), связанное с событием. По умолчанию null.
 *
 * @see Attachment
 * @see InstantSerializer
 */
@Serializable
data class EventData(
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
    @SerialName("type")
    val optionConducting: String = "",
    @SerialName("datetime")
    @Serializable(with = InstantSerializer::class)
    val dataEvent: Instant? = Instant.now(),
    @SerialName("content")
    val content: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("participantsIds")
    val participantsIds: Set<Long> = emptySet(),
    @SerialName("attachment")
    val attachment: Attachment? = null,
)
