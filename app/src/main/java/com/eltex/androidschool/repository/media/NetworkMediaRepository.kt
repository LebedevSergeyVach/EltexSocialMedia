package com.eltex.androidschool.repository.media

import android.content.ContentResolver
import android.graphics.Bitmap

import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory

import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.data.media.MediaDto
import com.eltex.androidschool.data.media.FileModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import okio.BufferedSink
import okio.IOException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

const val DEFAULT_BUFFER_SIZE = 16384

/**
 * Преобразует изображение в формате URI в JPG и загружает его на сервер с отслеживанием прогресса.
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
 * @throws Exception Если произошла ошибка при преобразовании изображения.
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
    val inputStream: InputStream = contentResolver.openInputStream(fileModel.uri)
        ?: throw IOException("File not found")

    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
    withContext(Dispatchers.IO) {
        inputStream.close()
    }

    if (bitmap == null) {
        throw IOException("Failed to decode bitmap from input stream")
    }

    val byteArrayOutputStream = ByteArrayOutputStream()
    if (!bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream)) {
        throw IOException("Failed to compress bitmap to JPEG")
    }

    val fileBytes: ByteArray = byteArrayOutputStream.toByteArray()
    if (fileBytes.isEmpty()) {
        throw IOException("Failed to convert bitmap to byte array")
    }

    val fileBody: RequestBody = fileBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

    val requestBody = object : RequestBody() {
        override fun contentType(): MediaType? = fileBody.contentType()

        override fun writeTo(sink: BufferedSink) {
            val totalBytes: Long = fileBytes.size.toLong()
            var bytesWritten = 0L
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

            val source: ByteArrayInputStream = fileBytes.inputStream()
            source.use { input: ByteArrayInputStream ->
                while (true) {
                    val bytesRead: Int = input.read(buffer)
                    if (bytesRead == -1) break

                    sink.write(buffer, 0, bytesRead)
                    bytesWritten += bytesRead

                    val progress: Int = ((bytesWritten.toFloat() / totalBytes) * 100).toInt()
                    onProgress(progress)
                }
            }
        }

        override fun contentLength(): Long = fileBytes.size.toLong()
    }

    val part = MultipartBody.Part.createFormData("file", "file.jpg", requestBody)

    return try {
        mediaApi.uploadMedia(part)
    } catch (e: IOException) {
        throw IOException("Failed to upload file: ${e.message}")
    } catch (e: Exception) {
        throw IOException("Unexpected error: ${e.message}")
    }
}
