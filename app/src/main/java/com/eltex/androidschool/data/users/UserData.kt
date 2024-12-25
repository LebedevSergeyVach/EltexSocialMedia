package com.eltex.androidschool.data.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Данные пользователя, полученные из API.
 *
 *  Этот класс используется для хранения данных о пользователе.
 *
 * @property id Уникальный идентификатор пользователя.
 * @property name Имя пользователя.
 * @property avatar URL аватара пользователя.
 * @property login Логин пользователя.
 */
@Serializable
data class UserData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("name")
    val name: String = "",
    @SerialName("avatar")
    val avatar: String = "",
    @SerialName("login")
    val login: String = "",
)
