package com.eltex.androidschool.repository

import com.eltex.androidschool.data.PostData

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с постами.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see DaoSQLitePostRepository Реализация интерфейса в памяти.
 */
interface PostRepository {
    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @return Flow<List<Post>> Flow, излучающий список постов.
     */
    fun getPost(): Flow<List<PostData>>

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     */
    fun likeById(postId: Long)

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    fun deleteById(postId: Long)

    /**
     * Обновляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     */
    fun updateById(postId: Long, content: String)

    /**
     * Добавляет новый пост.
     *
     * @param content Содержание нового поста.
     */
    fun addPost(content: String)
}