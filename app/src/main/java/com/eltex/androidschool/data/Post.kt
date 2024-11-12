package com.eltex.androidschool.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Класс, представляющий пост в приложении.
 *
 * @property id Уникальный идентификатор поста. По умолчанию 0L.
 * @property author Автор поста. По умолчанию пустая строка.
 * @property published Дата и время публикации поста. По умолчанию текущая дата и время.
 * @property content Содержание поста. По умолчанию пустая строка.
 * @property likeByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. По умолчанию false.
 */
data class Post(
    val id: Long = 0L,
    val author: String = "",
    val published: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    val likeByMe: Boolean = false,
) {
    /**
     * Возвращает отформатированную строку даты и времени публикации поста.
     *
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedPublished(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return published.format(formatter)
    }
}
