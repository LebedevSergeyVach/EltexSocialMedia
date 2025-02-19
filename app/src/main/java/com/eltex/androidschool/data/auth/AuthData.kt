package com.eltex.androidschool.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("token")
    val token: String = "",
    @SerialName("avatar")
    val avatar: String = "",
)
