package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import androidx.core.content.contentValuesOf

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow
import com.eltex.androidschool.utils.getStringOrNull

import com.eltex.androidschool.db.PostTable
import com.eltex.androidschool.data.PostData

/**
 * Реализация интерфейса [PostDao] для работы с данными постов в базе данных SQLite.
 *
 * @property db База данных SQLite.
 */
class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {
    /**
     * Получает все посты из базы данных.
     *
     * @return Список всех постов.
     */
    override fun getAll(): List<PostData> {
        db.query(
            PostTable.TABLE_NAME,
            PostTable.allColumns,
            null,
            null,
            null,
            null,
            "${PostTable.ID} DESC",
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
            PostTable.AUTHOR to post.author,
            PostTable.PUBLISHED to post.published,
            PostTable.LAST_MODIFIED to post.lastModified,
            PostTable.CONTENT to post.content,
            PostTable.LIKE_BY_ME to post.likeByMe,
        )

        if (post.id != 0L) {
            contentValues.put(PostTable.ID, post.id)
        }

        val postId: Long = db.insert(PostTable.TABLE_NAME, null, contentValues)

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
                UPDATE ${PostTable.TABLE_NAME} SET
                    ${PostTable.LIKE_BY_ME} = CASE WHEN ${PostTable.LIKE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${PostTable.ID} = ?;
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
            PostTable.TABLE_NAME,
            "${PostTable.ID} = ?",
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
            PostTable.CONTENT to content,
            PostTable.LAST_MODIFIED to LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )

        db.update(
            PostTable.TABLE_NAME,
            contentValues,
            "${PostTable.ID} = ?",
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
            PostTable.TABLE_NAME,
            PostTable.allColumns,
            "${PostTable.ID} = ?",
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
        id = getLongOrThrow(PostTable.ID),
        author = getStringOrThrow(PostTable.AUTHOR),
        published = getStringOrThrow(PostTable.PUBLISHED),
        lastModified = getStringOrNull(PostTable.LAST_MODIFIED),
        content = getStringOrThrow(PostTable.CONTENT),
        likeByMe = getBooleanOrThrow(PostTable.LIKE_BY_ME),
    )
