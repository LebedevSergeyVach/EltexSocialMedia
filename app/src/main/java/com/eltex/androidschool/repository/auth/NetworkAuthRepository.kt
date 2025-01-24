package com.eltex.androidschool.repository.auth

import com.eltex.androidschool.api.auth.AuthApi
import com.eltex.androidschool.data.auth.LoginResponse
import com.eltex.androidschool.data.auth.RegisterResponse

class NetworkAuthRepository : AuthRepository {
    override suspend fun login(login: String, password: String): LoginResponse =
        AuthApi.INSTANCE.login(
            login = login,
            password = password,
        )

    override suspend fun register(login: String, name: String, password: String): RegisterResponse =
        AuthApi.INSTANCE.register(
            login = login,
            password = password,
            name = name,
        )
}
