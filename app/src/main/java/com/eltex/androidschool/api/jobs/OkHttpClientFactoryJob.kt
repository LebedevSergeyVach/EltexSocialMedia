package com.eltex.androidschool.api.jobs

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.utils.DnsSelector

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.util.concurrent.TimeUnit

/**
 * Фабрика для создания экземпляра OkHttpClient с настройками для работы с API вакансий.
 */
object OkHttpClientFactoryJob {
    private const val API_KEY = "Api-Key"
    private const val API_AUTHORIZATION = "Authorization"

    /**
     * Ленивая инициализация экземпляра OkHttpClient.
     * Настраивает таймауты, добавляет интерцепторы для логирования и авторизации.
     *
     * @return Настроенный экземпляр OkHttpClient.
     * @see HttpLoggingInterceptor
     * @see DnsSelector
     */
    val INSTANCE: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .let { clientOkHttp: OkHttpClient.Builder ->
                if (BuildConfig.DEBUG) {
                    clientOkHttp.addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                } else {
                    clientOkHttp
                }
            }
            .addInterceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(API_KEY, BuildConfig.API_KEY)
                        .addHeader(API_AUTHORIZATION, BuildConfig.AUTHORIZATION)
                        .build()
                )
            }
            .dns(DnsSelector())
            .build()
    }
}
