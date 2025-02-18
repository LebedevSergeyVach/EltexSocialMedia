package com.eltex.androidschool.api.auth

import com.eltex.androidschool.data.auth.AuthData

import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("api/users/authentication")
    suspend fun login(
        @Query("login") login: String,
        @Query("pass") password: String,
    ): AuthData

    @POST("api/users/registration")
    suspend fun register(
        @Query("login") login: String,
        @Query("pass") password: String,
        @Query("name") name: String,
    ): AuthData
}
