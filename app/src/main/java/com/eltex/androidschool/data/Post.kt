package com.eltex.androidschool.data

/**
 * Класс [Post] представляет данные о Посте
 *
 * @property id = 0L
 * @property author автор = ""
 * @property published дата и время публиции = ""
 * @property content содержание = ""
 * @property likeByMe лайк true & false = false
 */
data class Post(
    val id: Long = 0L,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likeByMe: Boolean = false,
)
