package com.eltex.androidschool.data

/**
 * Класс [Event] представляет данные о Событии
 *
 * @property id = 0L
 * @property author автор = ""
 * @property published дата и время публиции = ""
 * @property optionConducting online & offline = "
 * @property dataEvent дата и время события = ""
 * @property content содержание = ""
 * @property link ссылка = ""
 * @property likeByMe лайк true & false = false
 * @property participateByMe участие true & false = false
 */
data class Event (
    val id: Long = 0L,
    val author: String = "",
    val published: String = "",
    val optionConducting: String = "",
    val dataEvent: String = "",
    val content: String = "",
    val link: String = "",
    val likeByMe: Boolean = false,
    val participateByMe: Boolean = false,
)
