package com.eltex.androidschool.api.common

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.store.UserPreferences

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import okhttp3.Interceptor
import okhttp3.Response

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    private companion object {
        private const val API_KEY = "Api-Key"
        private const val API_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken: String? = runBlocking { userPreferences.authTokenFlow.first() }

        val newRequest = if (authToken.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader(API_KEY, BuildConfig.API_KEY)
                .build()
        } else {
            chain.request().newBuilder()
                .addHeader(API_KEY, BuildConfig.API_KEY)
                .addHeader(API_AUTHORIZATION, authToken)
                .build()
        }
        return chain.proceed(newRequest)
    }
}
