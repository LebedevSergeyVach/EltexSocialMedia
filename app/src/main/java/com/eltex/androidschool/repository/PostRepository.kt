package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Post
import kotlinx.coroutines.flow.Flow


/**
 * Интерфейс репозитория для работы с постами.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see InMemoryPostRepository Реализация интерфейса в памяти.
 */
interface PostRepository {
    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @return Flow<List<Post>> Flow, излучающий список постов.
     *
     * @sample [InMemoryPostRepository.getPost] Пример использования метода в реализации репозитория.
     */
    fun getPost(): Flow<List<Post>>

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     *
     * @sample [InMemoryPostRepository.likeById] Пример использования метода в реализации репозитория.
     */
    fun likeById(postId: Long)
}
