/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется PostDao ROOM ORM
*/

package com.eltex.androidschool.development.database

import com.eltex.androidschool.data.PostData

/**
 * Интерфейс для работы с данными постов в базе данных.
 *
 * @see PostData Класс, представляющий данные поста.
 */
interface PostDaoSQLite {
    /**
     * Получает все посты из базы данных.
     *
     * @return Список всех постов.
     */
    fun getAll(): List<PostData>

    /**
     * Сохраняет новый пост в базу данных.
     *
     * @param post Пост для сохранения.
     * @return Сохраненный пост с обновленным идентификатором.
     */
    fun save(post: PostData): PostData

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @return Обновленный пост.
     */
    fun likeById(postId: Long): PostData

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    fun deleteById(postId: Long)

    /**
     * Обновляет содержимое поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     */
    fun updateById(postId: Long, content: String)
}
