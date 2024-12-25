package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.TestCoroutinesRule
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.TestPostRepository
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
 * @see TestPostRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestCoroutinesRule Правило для управления корутинами в тестах.
 */
class PostViewModelTest {
    @get:Rule
    val coroutinesRule: TestCoroutinesRule = TestCoroutinesRule()

    /**
     * Маппер для преобразования данных поста в UI-модель.
     *
     * @see PostUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = PostUiModelMapper()

    /**
     * Тест для проверки корректной загрузки списка постов.
     */
    @Test
    fun `load posts successfully`() {
        val posts = listOf(
            PostData(id = 1, content = "Post 1"),
            PostData(id = 2, content = "Post 2")
        )

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun getPosts(): List<PostData> = posts
            }
        )

        val expected = PostState(
            posts = posts.map { post: PostData ->
                mapper.map(post)
            },
            statusPost = StatusPost.Idle
        )

        viewModel.load()

        loading()

        val actual = viewModel.state.value

        assertEquals(expected, actual)
        assertEquals(posts.size, actual.posts?.size)
    }

    /**
     * Тест для проверки обработки ошибки при загрузке списка постов.
     */
    @Test
    fun `load posts with error`() {
        val error = RuntimeException("test error load")

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun getPosts(): List<PostData> = throw error
            }
        )

        val expected = PostState(
            posts = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.load()

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного лайка поста.
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
            object : TestPostRepository {
                override suspend fun getPosts(): List<PostData> = initialPosts
                override suspend fun likeById(postId: Long, likedByMe: Boolean): PostData =
                    likedPost
            }
        )

        val expected = PostState(
            posts = initialPosts.map { post: PostData ->
                mapper.map(post)
            },
            statusPost = StatusPost.Idle
        )

        viewModel.load()

        loading()

        viewModel.likeById(postId, likedPost.likedByMe)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
        assertEquals(
            expected.posts?.find { post: PostUiModel ->
                post.id == postId
            }?.likedByMe,

            actual.posts?.find { post: PostUiModel ->
                post.id == postId
            }?.likedByMe
        )
        assertEquals(
            initialPosts[0].likedByMe,

            actual.posts?.find { post: PostUiModel ->
                post.id == postId
            }?.likedByMe
        )
        assertEquals(
            false,

            actual.posts?.find { post: PostUiModel ->
                post.id == postId
            }?.likedByMe
        )
    }

    /**
     * Тест для проверки обработки ошибки при лайке поста.
     */
    @Test
    fun `like post with error`() {
        val error = RuntimeException("test error like")

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun likeById(postId: Long, likedByMe: Boolean): PostData =
                    throw error
            }
        )

        val expected = PostState(
            posts = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.likeById(1, false)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного удаления поста.
     */
    @Test
    fun `delete post successfully`() {
        val postId = 1L

        val initialPosts = listOf(
            PostData(id = postId, content = "Post 1", likedByMe = false),
            PostData(id = 2, content = "Post 2", likedByMe = false)
        )

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun getPosts(): List<PostData> = initialPosts
                override suspend fun deleteById(postId: Long) {}
            }
        )

        val expectedPosts = initialPosts.filter { post: PostData ->
            post.id != postId
        }
            .map { post: PostData ->
                mapper.map(post)
            }

        val expected = PostState(
            posts = expectedPosts,
            statusPost = StatusPost.Idle
        )

        viewModel.load()

        loading()

        viewModel.deleteById(postId)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
        assertEquals(1, viewModel.state.value.posts?.size)
        assertEquals(false, viewModel.state.value.posts?.any { post: PostUiModel ->
            post.id == postId
        })
    }

    /**
     * Тест для проверки обработки ошибки при удалении поста.
     */
    @Test
    fun `delete post with error`() {
        val error = RuntimeException("test error delete")

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun deleteById(postId: Long) = throw error
            }
        )

        val expected = PostState(
            posts = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.deleteById(1)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    private fun loading(timeMillis: Long = 1_000L) = runBlocking {
        launch {
            delay(timeMillis)
        }
    }
}
