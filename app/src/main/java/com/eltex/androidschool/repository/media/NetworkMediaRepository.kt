package com.eltex.androidschool.repository.media

import android.content.ContentResolver

import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.data.media.MediaDto
import com.eltex.androidschool.viewmodel.common.FileModel

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import okio.Buffer
import okio.BufferedSink
import okio.IOException

/**
 * Загружает медиафайл на сервер с отслеживанием прогресса.
 *
 * Репозиторий для работы с медиафайлами через сеть.
 * Этот файл содержит функцию для загрузки медиафайлов на сервер с отслеживанием прогресса.
 *
 * @param fileModel Модель файла, содержащая URI файла и другую необходимую информацию.
 * @param contentResolver [ContentResolver] для доступа к содержимому файла по URI.
 * @param onProgress Колбэк, вызываемый для обновления прогресса загрузки. Принимает целое число от 0 до 100, представляющее процент выполнения.
 * @param mediaApi API для загрузки медиафайлов на сервер.
 *
 * @return [MediaDto] Ответ сервера после успешной загрузки файла.
 *
 * @throws IOException Если файл не найден или произошла ошибка при чтении файла.
 *
 * @see MediaApi
 * @see MediaDto
 * @see FileModel
 */
suspend fun uploadMedia(
    fileModel: FileModel,
    contentResolver: ContentResolver,
    onProgress: (Int) -> Unit,
    mediaApi: MediaApi,
): MediaDto {
    val fileBody = contentResolver.openInputStream(fileModel.uri)?.use { inputStream ->
        val fileBytes = inputStream.readBytes()
        fileBytes.toRequestBody()
    } ?: throw IOException("File not found")

    val requestBody = object : RequestBody() {
        override fun contentType(): MediaType? = fileBody.contentType()

        override fun writeTo(sink: BufferedSink) {
            val totalBytes = fileBody.contentLength()
            val buffer = Buffer()
            var bytesWritten = 0L

            fileBody.writeTo(buffer)

            while (!buffer.exhausted()) {
                val bytesRead = buffer.read(sink.buffer, 16364)
                bytesWritten += bytesRead
                val progress = ((bytesWritten.toFloat() / totalBytes) * 100).toInt()

                onProgress(progress)
            }
        }

        override fun contentLength(): Long = fileBody.contentLength()
    }

    val part = MultipartBody.Part.createFormData("file", "file", requestBody)

    return mediaApi.uploadMedia(part)
}
