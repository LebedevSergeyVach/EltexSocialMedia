package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Post
import kotlinx.coroutines.flow.Flow


/**
 * Интерфейс [PostRepository] предоставляет методы для управления постами
 * Получить Пост, поставить лайк
 */
interface PostRepository {
    /**
     * Получить пост в виде потока данных [Flow]
     *
     * @return [Flow] с данными о посте
     *
     * @sample [PostRepository]
     * @sample [InMemoryPostRepository]
     */
    fun getPost(): Flow<Post>

    /**
     * Поставить лайк Посту пользователем
     * true & false
     *
     * @sample [PostRepository]
     * @sample [InMemoryPostRepository]
     */
    fun like()
}
