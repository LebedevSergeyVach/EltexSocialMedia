package com.eltex.androidschool.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Event (
    val id: Long = 0L,
    val author: String = "",
    val published: LocalDateTime = LocalDateTime.now(),
    val optionConducting: String = "",
    val dataEvent: String = "",
    val content: String = "",
    val link: String = "",
    val likeByMe: Boolean = false,
    val participateByMe: Boolean = false,
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
