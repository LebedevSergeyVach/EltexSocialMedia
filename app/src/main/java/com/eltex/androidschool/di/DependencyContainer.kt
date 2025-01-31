package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider

interface DependencyContainer {
    fun getPostsViewModelFactory(): ViewModelProvider.Factory
    fun getNewPostViewModelFactory(postId: Long): ViewModelProvider.Factory
}
