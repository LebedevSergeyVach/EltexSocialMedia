package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider

/**
 * Интерфейс контейнера зависимостей.
 * Этот интерфейс определяет методы для получения фабрик ViewModel.
 *
 * @see ViewModelProvider.Factory
 */
interface DependencyContainer {

    /**
     * Возвращает фабрику для создания [PostViewModel].
     *
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getPostsViewModelFactory(): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [NewPostViewModel].
     *
     * @param postId Идентификатор поста.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getNewPostViewModelFactory(postId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [PostWallViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getPostsWallViewModelFactory(userId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [EventViewModel].
     *
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getEventsViewModelFactory(): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [NewEventViewModel].
     *
     * @param eventId Идентификатор события.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getNewEventViewModelFactory(eventId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [EventWallViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getEventsWallViewModelFactory(userId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [UsersViewModel].
     *
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getUsersViewModelFactory(): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [UserViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getAccountViewModelFactory(userId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [JobViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getJobWallViewModelFactory(userId: Long): ViewModelProvider.Factory

    /**
     * Возвращает фабрику для создания [NewJobViewModel].
     *
     * @param jobId Идентификатор работы.
     * @return [ViewModelProvider.Factory] Фабрика для создания ViewModel.
     */
    fun getNewJobViewModelFactory(jobId: Long): ViewModelProvider.Factory
}
