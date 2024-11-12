package com.eltex.androidschool.data

import java.time.Instant

/**
 * Класс, представляющий пост в приложении.
 *
 * @property id Уникальный идентификатор поста. По умолчанию 0L.
 * @property author Автор поста. По умолчанию пустая строка.
 * @property published Дата и время публикации поста. По умолчанию пустая строка.
 * @property content Содержание поста. По умолчанию пустая строка.
 * @property likeByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. По умолчанию false.
 */
data class Post(
    val id: Long = 0L,
    val author: String = "",
    val published: Instant = Instant.now(),
    val content: String = "",
    val likeByMe: Boolean = false,
)
