package com.eltex.androidschool.entity.posts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.db.posts.PostTableInfo

/**
 * Сущность, представляющая пост в базе данных.
 */
@Entity(tableName = PostTableInfo.TABLE_NAME)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0L,
    @ColumnInfo("author")
    val author: String = "",
    @ColumnInfo("published")
    val published: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    @ColumnInfo("lastModified")
    val lastModified: String? = null,
    @ColumnInfo("content")
    val content: String = "",
    @ColumnInfo("likeByMe")
    val likeByMe: Boolean = false,
) {
    companion object {
        /**
         * Создает экземпляр [PostEntity] из [PostData].
         *
         * @param post Данные поста.
         * @return Экземпляр [PostEntity].
         */
        fun fromPostData(post: PostData): PostEntity =
            with(post) {
                PostEntity(
                    id = id,
                    author = author,
                    published = published,
                    lastModified = lastModified,
                    content = content,
                    likeByMe = likedByMe,
                )
            }
    }

    /**
     * Преобразует экземпляр [PostEntity] в [PostData].
     *
     * @return Экземпляр [PostData].
     */
    fun toPostData(): PostData =
        PostData(
            id = id,
            author = author,
            published = published,
            lastModified = lastModified,
            content = content,
            likedByMe = likeByMe
        )
}
