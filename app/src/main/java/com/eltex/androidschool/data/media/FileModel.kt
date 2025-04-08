package com.eltex.androidschool.data.media

import android.net.Uri
import com.eltex.androidschool.data.common.AttachmentTypeFile

/**
 * Модель данных для представления файла.
 *
 * @property uri URI файла.
 * @property type Тип файла (например, изображение, документ и т.д.).
 */
data class FileModel(
    val uri: Uri,
    val type: AttachmentTypeFile,
)