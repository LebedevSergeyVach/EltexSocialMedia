package com.eltex.androidschool.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Класс, представляющий событие в приложении.
 *
 * @property id Уникальный идентификатор события. По умолчанию 0L.
 * @property author Автор события. По умолчанию пустая строка.
 * @property published Дата и время публикации события. По умолчанию текущая дата и время.
 * @property optionConducting Вариант проведения события. По умолчанию пустая строка.
 * @property dataEvent Дата события. По умолчанию пустая строка.
 * @property content Содержание события. По умолчанию пустая строка.
 * @property link Ссылка на событие. По умолчанию пустая строка.
 * @property likeByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие. По умолчанию false.
 * @property participateByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии. По умолчанию false.
 */
data class Event(
    val id: Long = 0L,
    val author: String = "",
    val published: LocalDateTime = LocalDateTime.now(),
    val lastModified: LocalDateTime? = null,
    val optionConducting: String = "",
    val dataEvent: String = "",
    val content: String = "",
    val link: String = "",
    val likeByMe: Boolean = false,
    val participateByMe: Boolean = false,
) {
    /**
     * Возвращает отформатированную строку даты и времени публикации события формате "yyyy-MM-dd HH:mm:ss".
     *
     * @return String Отформатированная строка даты и времени.
     */
    fun getFormattedPublished(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return published.format(formatter)
    }

    /**
     * Возвращает дату и время последнего изменения события в формате "yyyy-MM-dd HH:mm:ss".
     *
     * @return Строка с датой и временем последнего изменения в формате "yyyy-MM-dd HH:mm:ss" или `null`, если дата отсутствует.
     */
    fun getFormattedLastModified(): String? {
        return lastModified?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
