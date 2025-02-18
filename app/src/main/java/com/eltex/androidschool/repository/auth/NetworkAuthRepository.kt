package com.eltex.androidschool.repository.auth

import com.eltex.androidschool.api.auth.AuthApi
import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.store.UserPreferences

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
                userId = response.id
            )
        }

        return response
    }

    override suspend fun register(login: String, name: String, password: String): AuthData {
        val response: AuthData = authApi.register(
            login = login,
            name = name,
            password = password,
        )

        if (response.token.isNotEmpty()) {
            userPreferences.saveUserCredentials(
                authToken = response.token,
                userId = response.id
            )
        }

        return response
    }
}
