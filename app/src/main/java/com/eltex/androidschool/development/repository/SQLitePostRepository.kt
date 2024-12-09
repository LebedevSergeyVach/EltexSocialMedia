/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется DaoSQLitePostRepository ROOM ORM
*//*


package com.eltex.androidschool.development.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.development.dao.PostDaoSQLite
import com.eltex.androidschool.data.PostData
import com.eltex.androidschool.repository.PostRepository

*/
/**
 * Репозиторий для работы с данными постов, использующий SQLite и StateFlow.
 *
 * @property postDao DAO для работы с данными постов.
 *//*

class SQLitePostRepository(
    private val postDao: PostDaoSQLite
) : PostRepository {
    */
/**
     * Flow для хранения состояния списка постов.
     *//*

    private val _state: MutableStateFlow<List<PostData>> = MutableStateFlow(readPosts())

    */
/**
     * Получает Flow со списком постов.
     *
     * @return Flow со списком постов.
     *//*

    override fun getPost(): Flow<List<PostData>> = _state.asStateFlow()

    */
/**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     *//*

    override fun likeById(postId: Long) {
        postDao.likeById(postId)

        sync()
    }

    */
/**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     *//*

    override fun deleteById(postId: Long) {
        postDao.deleteById(postId)

        sync()
    }

    */
/**
     * Обновляет содержимое поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     *//*

    override fun updateById(postId: Long, content: String) {
        postDao.updateById(
            postId = postId,
            content = content
        )

        sync()
    }

    */
/**
     * Добавляет новый пост.
     *
     * @param content Содержимое нового поста.
     *//*

    override fun addPost(content: String) {
        postDao.save(
            PostData(
                content = content,
                author = "Student"
            )
        )

        sync()
    }

    */
/**
     * Синхронизирует состояние Flow с данными из базы данных.
     *//*

    private fun sync() {
        _state.update {
            readPosts()
        }
    }

    */
/**
     * Читает все посты из базы данных.
     *
     * @return Список всех постов.
     *//*

    private fun readPosts(): List<PostData> = postDao.getAll()
}
*/
