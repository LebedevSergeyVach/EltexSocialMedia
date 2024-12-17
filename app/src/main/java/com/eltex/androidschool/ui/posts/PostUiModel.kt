package com.eltex.androidschool.ui.posts

/**
 * Data-класс, представляющий UI-модель поста.
 *
 * Этот класс используется для отображения информации о посте в пользовательском интерфейсе.
 *
 * @property id Уникальный идентификатор поста.
 * @property author Автор поста.
 * @property published Дата и время публикации поста.
 * @property content Содержимое поста.
 * @property likedByMe Указывает, лайкнул ли текущий пользователь этот пост.
 * @property likes Количество лайков, полученных постом.
 */
data class PostUiModel(
    val id: Long = 0L,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)
