package com.eltex.androidschool

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

/**
 * Основной класс приложения, который предоставляет контейнер зависимостей.
 * Этот класс реализует интерфейс [DependencyContainerProvider] для предоставления
 * экземпляра [DependencyContainerImpl], который содержит все зависимости приложения.
 *
 * @see DependencyContainerProvider
 * @see DependencyContainerImpl
 */
@HiltAndroidApp
class App : Application()
