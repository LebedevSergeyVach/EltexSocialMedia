package com.eltex.androidschool.di

import android.content.ContentResolver
import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Модуль Dagger Hilt для предоставления [ContentResolver].
 */
@Module
@InstallIn(SingletonComponent::class)
class ContentResolverModule {

    /**
     * Предоставляет экземпляр [ContentResolver] для работы с файлами и медиа.
     *
     * @param context Контекст приложения.
     * @return Экземпляр [ContentResolver].
     *
     * @see ContentResolver
     */
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver
}
