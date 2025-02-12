package com.eltex.androidschool.di

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

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindNetworkPostRepository(implPostRepository: NetworkPostRepository): PostRepository

    @Binds
    fun bindNetworkEventRepository(implEventRepository: NetworkEventRepository): EventRepository

    @Binds
    fun bindNetworkUserRepository(implUserRepository: NetworkUserRepository): UserRepository

    @Binds
    fun bindNetworkJobRepository(impJobRepository: NetworkJobRepository): JobRepository
}
