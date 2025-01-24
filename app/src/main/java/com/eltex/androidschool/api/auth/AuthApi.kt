package com.eltex.androidschool.api.auth

import com.eltex.androidschool.data.auth.LoginResponse
import com.eltex.androidschool.data.auth.RegisterResponse

import retrofit2.create
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("api/users/authentication")
    suspend fun login(
        @Query("login ") login: String,
        @Query("pass ") password: String,
    ): LoginResponse

    @POST("api/users/registration")
    suspend fun register(
        @Query("login ") login: String,
        @Query("pass") password: String,
        @Query("name") name: String,
    ): RegisterResponse

    companion object {
        val INSTANCE: AuthApi by lazy {
            RetrofitFactoryAuth.INSTANCE.create<AuthApi>()
        }
    }
}
