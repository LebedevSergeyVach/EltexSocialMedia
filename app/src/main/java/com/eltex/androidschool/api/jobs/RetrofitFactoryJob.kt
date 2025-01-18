package com.eltex.androidschool.api.jobs

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json

import okhttp3.MediaType.Companion.toMediaType

import retrofit2.Retrofit

/**
 * Фабрика для создания экземпляра Retrofit с настройками для работы с API вакансий.
 */
object RetrofitFactoryJob {
    private val JSON_TYPE = "application/json".toMediaType()
    private const val API_BASE_URL_EVENT = "https://eltex-android.ru/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Ленивая инициализация экземпляра Retrofit.
     * Настраивает базовый URL, конвертер JSON и клиент OkHttp.
     *
     * @return Настроенный экземпляр Retrofit.
     * @see OkHttpClientFactoryJob
     */
    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClientFactoryJob.INSTANCE)
            .baseUrl(API_BASE_URL_EVENT)
            .addConverterFactory(json.asConverterFactory(JSON_TYPE))
            .build()
    }
}
