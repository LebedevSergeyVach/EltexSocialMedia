package com.eltex.androidschool.repository.media

import android.content.Context

import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.api.media.MediaDto
import com.eltex.androidschool.viewmodel.common.FileModel

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream


suspend fun uploadMedia(fileModel: FileModel, context: Context): MediaDto =
    MediaApi.INSTANCE.uploadMedia(
        MultipartBody.Part.createFormData(
            name = "file",
            filename = "file",
            body = requireNotNull(
                context.contentResolver.openInputStream(fileModel.uri)
            ).use { inputStream: InputStream ->
                inputStream.readBytes()
            }
                .toRequestBody()
        )
    )

