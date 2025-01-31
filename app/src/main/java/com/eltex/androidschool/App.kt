package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.di.DependencyContainer
import com.eltex.androidschool.di.DependencyContainerImpl
import com.eltex.androidschool.di.DependencyContainerProvider

/**
 * Основной класс приложения, который предоставляет контейнер зависимостей.
 * Этот класс реализует интерфейс [DependencyContainerProvider] для предоставления
 * экземпляра [DependencyContainerImpl], который содержит все зависимости приложения.
 *
 * @see DependencyContainerProvider
 * @see DependencyContainerImpl
 */
class App : Application(), DependencyContainerProvider {
    private val container: DependencyContainerImpl by lazy {
        DependencyContainerImpl()
    }

    /**
     * Возвращает экземпляр контейнера зависимостей.
     *
     * @return [DependencyContainer] Контейнер зависимостей.
     */
    override fun getContainer(): DependencyContainer = container
}
