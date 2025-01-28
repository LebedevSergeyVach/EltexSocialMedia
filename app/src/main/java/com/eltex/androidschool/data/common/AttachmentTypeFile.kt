package com.eltex.androidschool.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Перечисление, представляющее тип вложения (attachment) в медиа-сети.
 * Используется для указания типа медиа-файла (изображение, видео, аудио).
 *
 * @property IMAGE Тип вложения - изображение.
 * @property VIDEO Тип вложения - видео.
 * @property AUDIO Тип вложения - аудио.
 * @see Attachment
 */
@Serializable
enum class AttachmentTypeFile {
    @SerialName("IMAGE")
    IMAGE,

    @SerialName("VIDEO")
    VIDEO,

    @SerialName("AUDIO")
    AUDIO,
}
