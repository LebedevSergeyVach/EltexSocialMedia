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
