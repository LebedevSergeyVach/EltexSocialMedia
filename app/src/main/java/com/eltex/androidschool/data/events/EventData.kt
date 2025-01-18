package com.eltex.androidschool.data.events

import com.eltex.androidschool.data.common.InstantSerializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.Instant

/**
 * Класс, представляющий событие в приложении.
 *
 * Этот класс используется для хранения данных о событии, включая его идентификатор, автора, дату публикации,
 * содержание, ссылку на событие и информацию о лайках и участниках.
 *
 * @property id Уникальный идентификатор события. По умолчанию 0L.
 * @property author Автор события. По умолчанию пустая строка.
 * @property authorId ID автора события. По умолчанию 0L.
 * @property published Дата и время публикации события. По умолчанию текущее время.
 * @property optionConducting Вариант проведения события. По умолчанию пустая строка.
 * @property dataEvent Дата и время события в формате ISO. По умолчанию пустая строка.
 * @property content Содержание события. По умолчанию пустая строка.
 * @property link Ссылка на событие. По умолчанию пустая строка.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие. По умолчанию false.
 * @property participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии. По умолчанию false.
 * @property likeOwnerIds Множество идентификаторов пользователей, которые лайкнули это событие. По умолчанию пустое множество.
 * @property participantsIds Множество идентификаторов пользователей, которые участвуют в этом событии. По умолчанию пустое множество.
 */
@Serializable
data class EventData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorId")
    val authorId: Long = 0L,
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
)
