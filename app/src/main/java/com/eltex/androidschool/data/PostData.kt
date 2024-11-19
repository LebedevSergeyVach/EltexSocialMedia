package com.eltex.androidschool.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Класс, представляющий пост в приложении.
 *
 * @property id Уникальный идентификатор поста. По умолчанию 0L.
 * @property author Автор поста. По умолчанию пустая строка.
 * @property published Дата и время публикации поста. По умолчанию текущая дата и время.
 * @property lastModified Дата и время последнего изменения поста. По умолчанию null.
 * @property content Содержание поста. По умолчанию пустая строка.
 * @property likeByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. По умолчанию false.
 */
@Serializable
data class PostData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    @SerialName("last_modified")
    val lastModified: String? = null,
    @SerialName("content")
    val content: String = "",
    @SerialName("like_by_me")
    val likeByMe: Boolean = false,
) {
    /**
     * Возвращает отформатированную строку даты и времени публикации поста в формате "yyyy-MM-dd HH:mm:ss".
     *
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedPublished(): String {
        return published.let { textData: String ->
            val dateTime = LocalDateTime.parse(textData, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }

    /**
     * Возвращает дату и время последнего изменения поста в формате "yyyy-MM-dd HH:mm:ss".
     *
     * @return Строка с датой и временем последнего изменения в формате "yyyy-MM-dd HH:mm:ss" или `null`, если дата отсутствует.
     */
    fun getFormattedLastModified(): String? {
        return lastModified?.let { textData: String ->
            val dateTime = LocalDateTime.parse(textData, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }
}