package com.eltex.androidschool.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data-класс, представляющий вложение (attachment) в медиа-сети.
 * Вложение может быть изображением, видео или аудио.
 *
 * @property url URL медиа-файла.
 * @property type Тип вложения, представленный перечислением [AttachmentTypeFile].
 * @see AttachmentTypeFile
 */
@Serializable
data class Attachment(
    @SerialName("url")
    val url: String,
    @SerialName("type")
    val type: AttachmentTypeFile,
)
