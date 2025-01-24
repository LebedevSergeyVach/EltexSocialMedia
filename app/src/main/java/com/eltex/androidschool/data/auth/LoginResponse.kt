package com.eltex.androidschool.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("token")
    val token: String,
)