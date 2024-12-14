/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется PostDaoImpl ROOM ORM
*/

package com.eltex.androidschool.development.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import androidx.core.content.contentValuesOf

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow
import com.eltex.androidschool.utils.getStringOrNull

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.development.database.PostTableSQLite

/**
 * Реализация интерфейса [PostDao] для работы с данными постов в базе данных SQLite.
 *
 * @property db База данных SQLite.
 */
class PostDaoImplSQLite(
    private val db: SQLiteDatabase
) : PostDaoSQLite {
    /**
     * Получает все посты из базы данных.
     *
     * @return Список всех постов.
     */
    override fun getAll(): List<PostData> {
        db.query(
            PostTableSQLite.TABLE_NAME,
            PostTableSQLite.allColumns,
            null,
            null,
            null,
            null,
            "${PostTableSQLite.ID} DESC",
        ).use { cursor: Cursor ->
            val posts = mutableListOf<PostData>()

            while (cursor.moveToNext()) {
                posts.add(cursor.readPost())
            }

            return posts
        }
    }

    /**
     * Сохраняет новый пост в базу данных.
     *
     * @param post Пост для сохранения.
     * @return Сохраненный пост с обновленным идентификатором.
     */
    override fun save(post: PostData): PostData {
        val contentValues = contentValuesOf(
            PostTableSQLite.AUTHOR to post.author,
            PostTableSQLite.PUBLISHED to post.published,
            PostTableSQLite.LAST_MODIFIED to post.lastModified,
            PostTableSQLite.CONTENT to post.content,
            PostTableSQLite.LIKE_BY_ME to post.likeByMe,
        )

        if (post.id != 0L) {
            contentValues.put(PostTableSQLite.ID, post.id)
        }

        val postId: Long = db.insert(PostTableSQLite.TABLE_NAME, null, contentValues)

        return getPostById(postId)
    }

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @return Обновленный пост.
     */
    override fun likeById(postId: Long): PostData {
        db.execSQL(
            """
                UPDATE ${PostTableSQLite.TABLE_NAME} SET
                    ${PostTableSQLite.LIKE_BY_ME} = CASE WHEN ${PostTableSQLite.LIKE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${PostTableSQLite.ID} = ?;
            """.trimIndent(),
            arrayOf(postId.toString())
        )

        return getPostById(postId)
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    override fun deleteById(postId: Long) {
        db.delete(
            PostTableSQLite.TABLE_NAME,
            "${PostTableSQLite.ID} = ?",
            arrayOf(postId.toString())
        )
    }

    /**
     * Обновляет содержимое поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     */
    override fun updateById(postId: Long, content: String) {
        val contentValues = contentValuesOf(
            PostTableSQLite.CONTENT to content,
            PostTableSQLite.LAST_MODIFIED to LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )

        db.update(
            PostTableSQLite.TABLE_NAME,
            contentValues,
            "${PostTableSQLite.ID} = ?",
            arrayOf(postId.toString())
        )
    }

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @return Пост с указанным идентификатором.
     * @throws IllegalArgumentException Если пост с указанным идентификатором не найден.
     */
    private fun getPostById(postId: Long): PostData =
        db.query(
            PostTableSQLite.TABLE_NAME,
            PostTableSQLite.allColumns,
            "${PostTableSQLite.ID} = ?",
            arrayOf(postId.toString()),
            null,
            null,
            null,
        ).use { cursor: Cursor ->
            cursor.moveToFirst()

            return cursor.readPost()
        }
}

/**
 * Расширение для [Cursor], которое преобразует данные из курсора в объект [PostData].
 *
 * @return Объект [PostData], созданный из данных курсора.
 */
private fun Cursor.readPost(): PostData =
    PostData(
        id = getLongOrThrow(PostTableSQLite.ID),
        author = getStringOrThrow(PostTableSQLite.AUTHOR),
        published = getStringOrThrow(PostTableSQLite.PUBLISHED),
        lastModified = getStringOrNull(PostTableSQLite.LAST_MODIFIED),
        content = getStringOrThrow(PostTableSQLite.CONTENT),
        likeByMe = getBooleanOrThrow(PostTableSQLite.LIKE_BY_ME),
    )
