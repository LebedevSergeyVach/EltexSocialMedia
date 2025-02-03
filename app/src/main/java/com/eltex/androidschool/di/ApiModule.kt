package com.eltex.androidschool.di

import com.eltex.androidschool.api.common.OkHttpClientFactory
import com.eltex.androidschool.api.common.RetrofitFactory
import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.api.users.UsersApi
import com.eltex.androidschool.api.jobs.JobsApi
import com.eltex.androidschool.api.media.MediaApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.create

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClientFactory.createOkHttpClient()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        RetrofitFactory.createRetrofit(okHttpClient)

    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()

    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create()

    @Provides
    fun provideJobsApi(retrofit: Retrofit): JobsApi = retrofit.create()

    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()
}
