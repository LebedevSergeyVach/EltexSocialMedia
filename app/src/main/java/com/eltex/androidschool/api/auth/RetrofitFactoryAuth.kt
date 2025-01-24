package com.eltex.androidschool.api.auth

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json

import okhttp3.MediaType.Companion.toMediaType

import retrofit2.Retrofit

object RetrofitFactoryAuth {
    private val JSON_TYPE = "application/json".toMediaType()
    private const val API_BASE_URL_AUTH = "https://eltex-android.ru/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClientFactoryAuth.INSTANCE)
            .baseUrl(API_BASE_URL_AUTH)
            .addConverterFactory(json.asConverterFactory(JSON_TYPE))
            .build()
    }

}
