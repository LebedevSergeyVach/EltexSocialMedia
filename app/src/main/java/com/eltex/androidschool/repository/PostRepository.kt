package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Post
import kotlinx.coroutines.flow.Flow


/**
 * Интерфейс для репозитория постов
 */
interface PostRepository {
    /**
     * Получить Пост, c использованием интерфейса Flow<T> (Flow<Post>)
     * @param InMemoryPostRepository class instance of PostRepository
     * @sample PostRepository
     * @sample InMemoryPostRepository
     */
    fun getPost(): Flow<Post>

    /**
     * Поставить лайк посту
     * @param InMemoryPostRepository class instance of PostRepository
     * @sample PostRepository
     * @sample InMemoryPostRepository
     */
    fun like()
}
