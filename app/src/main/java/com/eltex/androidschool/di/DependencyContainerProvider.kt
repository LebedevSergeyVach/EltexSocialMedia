package com.eltex.androidschool.di

/**
 * Интерфейс для предоставления контейнера зависимостей.
 * Этот интерфейс используется для получения экземпляра [DependencyContainer],
 * который содержит фабрики для создания ViewModel.
 *
 * @see DependencyContainer
 */
interface DependencyContainerProvider {

    /**
     * Возвращает экземпляр контейнера зависимостей.
     *
     * @return [DependencyContainer] Контейнер зависимостей.
     */
    fun getContainer(): DependencyContainer
}
