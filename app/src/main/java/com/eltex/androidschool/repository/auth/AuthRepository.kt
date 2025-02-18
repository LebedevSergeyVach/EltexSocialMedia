package com.eltex.androidschool.repository.auth

import android.content.ContentResolver

import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.viewmodel.common.FileModel

interface AuthRepository {
    suspend fun login(login: String, password: String): AuthData
    suspend fun register(
        login: String,
        username: String,
        password: String,
        fileModel: FileModel?,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit,
    ): AuthData
}
