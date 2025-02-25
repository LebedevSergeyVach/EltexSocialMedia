package com.eltex.androidschool.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Модель данных, представляющая ответ на запрос аутентификации или регистрации.
 * Содержит идентификатор пользователя, токен аутентификации и URL аватара.
 *
 * @property id Идентификатор пользователя.
 * @property token Токен аутентификации, используемый для авторизации запросов.
 * @property avatar URL аватара пользователя.
 */
@Serializable
data class AuthData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("token")
    val token: String = "",
    @SerialName("avatar")
    val avatar: String = "",
)
