package com.eltex.androidschool.data

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
 * @property likeByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие. По умолчанию false.
 * @property participateByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии. По умолчанию false.
 */
@Serializable
data class EventData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    @SerialName("lastModified")
    val lastModified: String? = null,
    @SerialName("optionConducting")
    val optionConducting: String = "",
    @SerialName("dataEvent")
    val dataEvent: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("likeByMe")
    val likeByMe: Boolean = false,
    @SerialName("participateByMe")
    val participateByMe: Boolean = false,
    // ТУТ Я ДОБАВИЛ НОВОЕ СВОЙСТВО :)
    @SerialName("newProperty")
    val newProperty: String = ""
) {
    /**
     * Возвращает отформатированную строку даты и времени публикации события формате "yyyy-MM-dd HH:mm:ss".
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
     * Возвращает дату и время последнего изменения события в формате "yyyy-MM-dd HH:mm:ss".
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
