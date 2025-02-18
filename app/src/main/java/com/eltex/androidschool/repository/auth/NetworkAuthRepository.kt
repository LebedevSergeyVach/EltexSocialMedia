package com.eltex.androidschool.repository.auth

import android.content.ContentResolver
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.api.auth.AuthApi
import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.store.UserPreferences
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.viewmodel.common.FileModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.IOException

import javax.inject.Inject

class NetworkAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences,
) : AuthRepository {
    override suspend fun login(login: String, password: String): AuthData {
        val response: AuthData = authApi.login(
            login = login,
            password = password,
        )

        if (response.token.isNotEmpty()) {
            userPreferences.saveUserCredentials(
                authToken = response.token,
                userId = response.id.toString()
            )
        }

        return response
    }

    override suspend fun register(
        login: String,
        username: String,
        password: String,
        fileModel: FileModel?,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit,
    ): AuthData {
        val response: AuthData = if (fileModel != null) {
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

            authApi.register(
                login = login,
                password = password,
                name = username,
                file = part,
            )
        } else {
            authApi.register(
                login = login,
                password = password,
                name = username,
                file = MultipartBody.Part.createFormData("file", "file"),
            )
        }

        if (response.token.isNotEmpty()) {
            userPreferences.saveUserCredentials(
                authToken = response.token,
                userId = response.id.toString()
            )
        }

        return response
    }
}
