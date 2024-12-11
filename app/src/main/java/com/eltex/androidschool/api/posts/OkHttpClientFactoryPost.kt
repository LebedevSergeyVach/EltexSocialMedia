package com.eltex.androidschool.api.posts

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.utils.DnsSelector

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.util.concurrent.TimeUnit

/**
 * Фабрика для создания экземпляра OkHttpClient.
 *
 * Этот объект отвечает за настройку и создание экземпляра OkHttpClient, который используется для выполнения сетевых запросов.
 * OkHttpClient настроен с использованием интерсепторов для добавления заголовков и логирования.
 */
object OkHttpClientFactoryPost {
    private const val API_KEY = "Api-Key"
    private const val API_AUTHORIZATION = "Authorization"

    /**
     * Экземпляр OkHttpClient, настроенный для работы с API.
     *
     * Ленивая инициализация, чтобы создать экземпляр OkHttpClient только при первом обращении.
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
