package com.eltex.androidschool.repository.auth

import com.eltex.androidschool.data.auth.LoginResponse
import com.eltex.androidschool.data.auth.RegisterResponse

interface AuthRepository {
    suspend fun login(login: String, password: String): LoginResponse
    suspend fun register(login: String, name: String, password: String): RegisterResponse
}
