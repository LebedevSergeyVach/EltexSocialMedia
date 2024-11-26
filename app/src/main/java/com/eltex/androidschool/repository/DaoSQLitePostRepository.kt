package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.dao.PostDao
import com.eltex.androidschool.data.PostData
import com.eltex.androidschool.entity.PostEntity

/**
 * Репозиторий для работы с данными постов, использующий SQLite и StateFlow.
 *
 * @property postDao DAO для работы с данными постов.
 */
class DaoSQLitePostRepository(
    private val postDao: PostDao
) : PostRepository {
    /**
     * Получает Flow со списком постов.
     *
     * @return Flow со списком постов.
     */
    override fun getPost(): Flow<List<PostData>> =
        postDao.getAll()
            .map { posts: List<PostEntity> ->
                posts.map(
                    PostEntity::toPostData
                )
            }

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    override fun likeById(postId: Long) {
        postDao.likeById(postId)
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    override fun deleteById(postId: Long) {
        postDao.deleteById(postId)
    }

    /**
     * Обновляет содержимое поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     */
    override fun updateById(postId: Long, content: String) {
        val lastModified = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        postDao.updateById(
            postId = postId,
            content = content,
            lastModified = lastModified
        )
    }

    /**
     * Добавляет новый пост.
     *
     * @param content Содержимое нового поста.
     */
    override fun addPost(content: String) {
        postDao.save(
            PostEntity.fromPostData(
                PostData(
                    content = content,
                    author = "Student"
                )
            )
        )
    }
}
