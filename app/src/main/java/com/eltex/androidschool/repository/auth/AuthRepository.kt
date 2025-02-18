package com.eltex.androidschool.repository.auth

import com.eltex.androidschool.data.auth.AuthData

interface AuthRepository {
    suspend fun login(login: String, password: String): AuthData
    suspend fun register(login: String, name: String, password: String): AuthData
}
