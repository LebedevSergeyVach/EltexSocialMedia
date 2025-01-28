package com.eltex.androidschool.ui.events

import com.eltex.androidschool.data.common.Attachment

/**
 * Data-класс, представляющий модель UI для события.
 *
 * Этот класс используется для отображения информации о событии в пользовательском интерфейсе.
 *
 * @property id Уникальный идентификатор события.
 * @property author Автор события.
 * @property authorId ID автора события.
 * @property authorAvatar URL аватара автора поста. Может быть null, если аватар не задан. Используется для отображения изображения автора.
 * @property published Дата и время публикации события.
 * @property optionConducting Вариант проведения события.
 * @property dataEvent Дата и время события.
 * @property content Содержание события.
 * @property link Ссылка на событие.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
 * @property participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
 * @property likes Количество лайков, полученных событием.
 * @property participates Количество участников события.
 *
 * @see Attachment Класс, представляющий вложение в посте.
 */
data class EventUiModel(
    val id: Long = 0L,
    val author: String = "",
    val authorId: Long = 0L,
    val authorAvatar: String? = null,
    val published: String = "",
    val optionConducting: String = "",
    val dataEvent: String = "",
    val content: String = "",
    val link: String = "",
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val likes: Int = 0,
    val participates: Int = 0,
    val attachment: Attachment? = null,
)
