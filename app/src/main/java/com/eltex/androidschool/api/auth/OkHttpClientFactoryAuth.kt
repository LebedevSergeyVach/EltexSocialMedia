package com.eltex.androidschool.api.auth

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.utils.DnsSelector

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.util.concurrent.TimeUnit

object OkHttpClientFactoryAuth {
    private const val API_KEY = "Api-Key"

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
                        .build()
                )
            }
            .dns(DnsSelector())
            .build()
    }
}
