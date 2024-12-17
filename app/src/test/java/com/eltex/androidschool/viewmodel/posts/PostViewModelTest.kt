package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.posts.PostUiModel

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тестовый класс для проверки функциональности ViewModel, отвечающего за управление постами.
 *
 * Этот класс содержит тесты для проверки корректной работы методов `PostViewModel`, таких как:
 * - Загрузка списка постов.
 * - Постановка и снятие лайка у поста.
 * - Удаление поста.
 * - Обработка ошибок при выполнении операций.
 *
 * Тесты используют моки репозитория для имитации поведения сервера и проверки корректности обновления состояния ViewModel.
 *
 * @see PostViewModel ViewModel, которое тестируется.
 * @see PostRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestSchedulersProvider Провайдер планировщиков для синхронного выполнения тестов.
 */
class PostViewModelTest {
    /**
     * Тест для проверки корректной загрузки списка постов.
     *
     * @param posts Список постов, которые будут возвращены из репозитория.
     * @return Успешное состояние с загруженными постами.
     */
    @Test
    fun `load posts successfully`() {
        val posts = listOf(
            PostData(id = 1, content = "Post 1"),
            PostData(id = 2, content = "Post 2")
        )

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun getPosts(): Single<List<PostData>> = Single.just(posts)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        assertEquals(StatusPost.Idle, viewModel.state.value.statusPost)
        assertEquals(posts.size, viewModel.state.value.posts?.size)
    }

    /**
     * Тест для проверки обработки ошибки при загрузке списка постов.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.
     */
    @Test
    fun `load posts with error`() {
        val error = RuntimeException("test error load")

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun getPosts(): Single<List<PostData>> = Single.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        assertEquals(StatusPost.Error(error), viewModel.state.value.statusPost)
    }

    /**
     * Тест для проверки корректного лайка поста.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return Успешное состояние с обновленным списком постов.
     */
    @Test
    fun `like post successfully`() {
        val postId = 1L
        val initialPosts = listOf(
            PostData(id = postId, content = "Post 1", likedByMe = false),
            PostData(id = 2, content = "Post 2", likedByMe = false)
        )
        val likedPost = PostData(id = postId, content = "Post 1", likedByMe = true)

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun getPosts(): Single<List<PostData>> = Single.just(initialPosts)
                override fun likeById(postId: Long, likedByMe: Boolean): Single<PostData> =
                    Single.just(likedPost)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        viewModel.likeById(postId, false)

        assertEquals(StatusPost.Idle, viewModel.state.value.statusPost)
        assertEquals(true, viewModel.state.value.posts?.find { post: PostUiModel ->
            post.id == postId
        }
            ?.likedByMe)
    }

    /**
     * Тест для проверки обработки ошибки при лайке поста.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.
     */
    @Test
    fun `like post with error`() {
        val error = RuntimeException("test error like")

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun likeById(postId: Long, likedByMe: Boolean): Single<PostData> =
                    Single.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.likeById(1, false)

        assertEquals(StatusPost.Error(error), viewModel.state.value.statusPost)
    }

    /**
     * Тест для проверки корректного удаления поста.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     * @return Успешное состояние с обновленным списком постов.
     */
    @Test
    fun `delete post successfully`() {
        val postId = 1L
        val posts = listOf(
            PostData(id = postId, content = "Post 1"),
            PostData(id = 2, content = "Post 2")
        )

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun deleteById(postId: Long): Completable = Completable.complete()
                override fun getPosts(): Single<List<PostData>> = Single.just(posts)
            },
            TestSchedulersProvider
        )

        viewModel.deleteById(postId)

        assertEquals(StatusPost.Idle, viewModel.state.value.statusPost)
        assertEquals(1, viewModel.state.value.posts?.size)
        assertEquals(false, viewModel.state.value.posts?.any { post: PostUiModel ->
            post.id == postId
        })
    }

    /**
     * Тест для проверки обработки ошибки при удалении поста.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.
     */
    @Test
    fun `delete post with error`() {
        val error = RuntimeException("test error delete")

        val viewModel = PostViewModel(
            object : PostRepository {
                override fun deleteById(postId: Long): Completable = Completable.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.deleteById(1)

        assertEquals(StatusPost.Error(error), viewModel.state.value.statusPost)
    }
}
