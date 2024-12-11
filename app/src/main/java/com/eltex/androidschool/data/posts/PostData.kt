package com.eltex.androidschool.data.posts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import java.util.Locale

/**
 * Класс, представляющий пост в приложении.
 *
 * @property id Уникальный идентификатор поста. По умолчанию 0L.
 * @property author Автор поста. По умолчанию пустая строка.
 * @property published Дата и время публикации поста. По умолчанию пустая строка.
 * @property lastModified Дата и время последнего изменения поста. По умолчанию null.
 * @property content Содержание поста. По умолчанию пустая строка.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. По умолчанию false.
 */
@Serializable
data class PostData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = "",
    @SerialName("last_modified")
    val lastModified: String? = null,
    @SerialName("content")
    val content: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
) {
    /**
     * Возвращает отформатированную строку даты и времени публикации поста в формате, зависящем от локали пользователя.
     *
     * @param locale Локаль пользователя.
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedPublished(locale: Locale): String {
        return published.let { textData: String ->
            val dateTime = ZonedDateTime.parse(textData, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            dateTime.withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale))
        }
    }

    /**
     * Возвращает дату и время последнего изменения поста в формате, зависящем от локали пользователя.
     *
     * @param locale Локаль пользователя.
     * @return Строка с датой и временем последнего изменения в формате "yyyy-MM-dd HH:mm:ss" или `null`, если дата отсутствует.
     */
    fun getFormattedLastModified(locale: Locale): String? {
        return lastModified?.let { textData: String ->
            val dateTime = ZonedDateTime.parse(textData, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            dateTime.withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale))
        }
    }
}
