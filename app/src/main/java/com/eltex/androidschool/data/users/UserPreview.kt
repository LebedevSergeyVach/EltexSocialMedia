package com.eltex.androidschool.data.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPreview(
    @SerialName("name")
    val name: String = "",
    @SerialName("avatar")
    val avatar: String? = null,
)
