package com.eltex.androidschool.di

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

/**
 * Модуль Dagger Hilt для предоставления [DataStore], используемого для хранения настроек пользователя.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Расширение для доступа к [DataStore] через контекст.
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    /**
     * Предоставляет экземпляр [DataStore] для хранения настроек пользователя.
     *
     * @param context Контекст приложения.
     * @return Экземпляр [DataStore].
     *
     * @see DataStore
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}
