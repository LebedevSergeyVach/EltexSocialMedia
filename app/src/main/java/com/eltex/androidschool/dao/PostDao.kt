package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

import kotlinx.coroutines.flow.Flow

import com.eltex.androidschool.db.PostTableInfo
import com.eltex.androidschool.entity.PostEntity

/**
 * Data Access Object (DAO) для работы с сущностями [PostEntity] в базе данных.
 */
@Dao
interface PostDao {
    /**
     * Получает все посты из базы данных, отсортированные по идентификатору в порядке убывания.
     *
     * @return Flow со списком постов.
     */
    @Query(
        """
            SELECT * FROM ${PostTableInfo.TABLE_NAME} ORDER BY id DESC
        """
    )
    fun getAll(): Flow<List<PostEntity>>

    /**
     * Сохраняет или обновляет пост в базе данных.
     *
     * @param post Пост для сохранения или обновления.
     * @return Идентификатор сохраненного или обновленного поста.
     */
    @Upsert
    fun save(post: PostEntity): Long

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    @Query(
        """
            UPDATE ${PostTableInfo.TABLE_NAME} SET
                likeByMe =  CASE WHEN likeByMe THEN 0 ELSE 1 END
            WHERE id = :postId
        """
    )
    fun likeById(postId: Long)

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    @Query(
        """
            DELETE FROM ${PostTableInfo.TABLE_NAME} WHERE id = :postId
        """
    )
    fun deleteById(postId: Long)

    /**
     * Обновляет содержимое поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     * @param lastModified Дата и время последнего изменения.
     */
    @Query(
        """
            UPDATE ${PostTableInfo.TABLE_NAME} 
                SET content = :content, lastModified = :lastModified WHERE id = :postId
        """
    )
    fun updateById(
        postId: Long,
        content: String,
        lastModified: String
    )
}
