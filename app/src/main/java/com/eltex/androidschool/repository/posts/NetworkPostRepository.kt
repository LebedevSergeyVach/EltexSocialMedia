package com.eltex.androidschool.repository.posts

import android.content.Context

import com.eltex.androidschool.api.media.MediaDto
import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.media.uploadMedia
import com.eltex.androidschool.viewmodel.common.FileModel

/**
 * Репозиторий для работы с данными постов через сетевой API.
 * Этот класс реализует интерфейс [PostRepository] и использует [PostsApi] для выполнения сетевых запросов.
 *
 * @see PostRepository Интерфейс репозитория, который реализует этот класс.
 * @see PostsApi Интерфейс для работы с API постов.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
class NetworkPostRepository : PostRepository {

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора.
     *
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список постов.
     */
    override suspend fun getBeforePosts(id: Long, count: Int): List<PostData> =
        PostsApi.INSTANCE.getBeforePosts(id = id, count = count)

    /**
     * Получает последние посты.
     *
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список последних постов.
     */
    override suspend fun getLatestPosts(count: Int): List<PostData> =
        PostsApi.INSTANCE.getLatestPosts(count = count)

    /**
     * Получает список всех постов.
     *
     * @return List<PostData> Список всех постов.
     */
    override suspend fun getPosts() =
        PostsApi.INSTANCE.getAllPosts()

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return PostData Пост с обновленным состоянием лайка.
     */
    override suspend fun likeById(postId: Long, likedByMe: Boolean): PostData {
        return if (likedByMe) {
            PostsApi.INSTANCE.unlikePostById(postId = postId)
        } else {
            PostsApi.INSTANCE.likePostById(postId = postId)
        }
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    override suspend fun deleteById(postId: Long) =
        PostsApi.INSTANCE.deletePostById(postId = postId)

    /**
     * Сохраняет или обновляет пост.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержание поста.
     *
     * @return PostData Обновленный или сохраненный пост.
     */
    override suspend fun save(
        postId: Long,
        content: String,
        fileModel: FileModel?,
        context: Context,
        onProgress: (Int) -> Unit
    ): PostData {
        val post: PostData = fileModel?.let { file: FileModel ->
            val media: MediaDto =
                uploadMedia(fileModel = file, context = context, onProgress = onProgress)

            PostData(
                id = postId,
                content = content,
                attachment = Attachment(url = media.url, type = file.type),
            )
        } ?: PostData(
            id = postId, content = content,
        )

        return PostsApi.INSTANCE.savePost(post = post)
    }

    /**
     * Получает посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @return List<PostData> Список постов пользователя.
     */
    override suspend fun getPostsByAuthorId(authorId: Long) =
        PostsApi.INSTANCE.getAllPostsByAuthorId(authorId = authorId)

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список постов.
     */
    override suspend fun getBeforePostsByAuthorId(
        authorId: Long,
        id: Long,
        count: Int
    ): List<PostData> =
        PostsApi.INSTANCE.getBeforePostsByAuthorId(
            authorId = authorId,
            id = id,
            count = count
        )

    /**
     * Получает последние посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список последних постов.
     */
    override suspend fun getLatestPostsByAuthorId(authorId: Long, count: Int): List<PostData> =
        PostsApi.INSTANCE.getLatestPostsByAuthorId(
            authorId = authorId, count = count
        )
}
