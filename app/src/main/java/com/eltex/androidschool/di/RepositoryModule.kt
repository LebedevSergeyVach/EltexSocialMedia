package com.eltex.androidschool.di

import com.eltex.androidschool.repository.auth.AuthRepository
import com.eltex.androidschool.repository.auth.NetworkAuthRepository
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.repository.events.NetworkEventRepository
import com.eltex.androidschool.repository.jobs.JobRepository
import com.eltex.androidschool.repository.jobs.NetworkJobRepository
import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.repository.users.NetworkUserRepository
import com.eltex.androidschool.repository.users.UserRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Модуль Dagger Hilt для связывания интерфейсов репозиториев с их реализациями.
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    /**
     * Связывает интерфейс [PostRepository] с его реализацией [NetworkPostRepository].
     *
     * @param implPostRepository Реализация [NetworkPostRepository].
     * @return Экземпляр [PostRepository].
     *
     * @see PostRepository
     * @see NetworkPostRepository
     */
    @Binds
    fun bindNetworkPostRepository(implPostRepository: NetworkPostRepository): PostRepository

    /**
     * Связывает интерфейс [EventRepository] с его реализацией [NetworkEventRepository].
     *
     * @param implEventRepository Реализация [NetworkEventRepository].
     * @return Экземпляр [EventRepository].
     *
     * @see EventRepository
     * @see NetworkEventRepository
     */
    @Binds
    fun bindNetworkEventRepository(implEventRepository: NetworkEventRepository): EventRepository

    /**
     * Связывает интерфейс [UserRepository] с его реализацией [NetworkUserRepository].
     *
     * @param implUserRepository Реализация [NetworkUserRepository].
     * @return Экземпляр [UserRepository].
     *
     * @see UserRepository
     * @see NetworkUserRepository
     */
    @Binds
    fun bindNetworkUserRepository(implUserRepository: NetworkUserRepository): UserRepository

    /**
     * Связывает интерфейс [JobRepository] с его реализацией [NetworkJobRepository].
     *
     * @param impJobRepository Реализация [NetworkJobRepository].
     * @return Экземпляр [JobRepository].
     *
     * @see JobRepository
     * @see NetworkJobRepository
     */
    @Binds
    fun bindNetworkJobRepository(impJobRepository: NetworkJobRepository): JobRepository

    /**
     * Связывает интерфейс [AuthRepository] с его реализацией [NetworkAuthRepository].
     *
     * @param implAuthRepository Реализация [NetworkAuthRepository].
     * @return Экземпляр [AuthRepository].
     *
     * @see AuthRepository
     * @see NetworkAuthRepository
     */
    @Binds
    fun bindNetworkAuthRepository(implAuthRepository: NetworkAuthRepository): AuthRepository
}
