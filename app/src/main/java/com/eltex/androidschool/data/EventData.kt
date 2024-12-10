package com.eltex.androidschool.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import java.util.Locale

/**
 * Класс, представляющий событие в приложении.
 *
 * @property id Уникальный идентификатор события. По умолчанию 0L.
 * @property author Автор события. По умолчанию пустая строка.
 * @property published Дата и время публикации события. По умолчанию текущая дата и время.
 * @property lastModified Дата и время последнего изменения поста. По умолчанию null.
 * @property optionConducting Вариант проведения события. По умолчанию пустая строка.
 * @property dataEvent Дата события. По умолчанию пустая строка.
 * @property content Содержание события. По умолчанию пустая строка.
 * @property link Ссылка на событие. По умолчанию пустая строка.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие. По умолчанию false.
 * @property participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии. По умолчанию false.
 */
@Serializable
data class EventData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = "",
    @SerialName("lastModified")
    val lastModified: String? = null,
    @SerialName("type")
    val optionConducting: String = "",
    @SerialName("datetime")
    val dataEvent: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
) {
    private val formattedDataAndTime: String = "yyyy-MM-dd HH:mm:ss"

    /**
     * Возвращает отформатированную строку даты и времени публикации события в формате, зависящем от локали пользователя.
     *
     * @param locale Локаль пользователя.
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedPublished(locale: Locale): String {
        return published.let { textData: String ->
            val dateTime = ZonedDateTime.parse(textData, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            dateTime.withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(formattedDataAndTime, locale))
        }
    }

    /**
     * Возвращает отформатированную строку даты и времени проведения события в формате, зависящем от локали пользователя.
     *
     * @param locale Локаль пользователя.
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedDataAndTimeEvent(locale: Locale): String {
        return dataEvent.let { textData: String ->
            val dateTime = ZonedDateTime.parse(textData, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            dateTime.withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(formattedDataAndTime, locale))
        }
    }

    /**
     * Возвращает дату и время последнего изменения события в формате, зависящем от локали пользователя.
     *
     * @param locale Локаль пользователя.
     * @return Строка с датой и временем последнего изменения в формате "yyyy-MM-dd HH:mm:ss" или `null`, если дата отсутствует.
     */
    fun getFormattedLastModified(locale: Locale): String? {
        return lastModified?.let { textData: String ->
            val dateTime = ZonedDateTime.parse(textData, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            dateTime.withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(formattedDataAndTime, locale))
        }
    }
}
