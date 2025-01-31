package com.eltex.androidschool.data.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data-класс, представляющий ответ сервера после успешной загрузки медиа-файла.
 * Содержит URL загруженного файла.
 *
 * @property url URL загруженного медиа-файла.
 * @see MediaApi
 */
@Serializable
data class MediaDto(
    @SerialName("url")
    val url: String,
)
