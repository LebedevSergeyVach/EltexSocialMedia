package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.TestCoroutinesRule
import com.eltex.androidschool.TestUtils.loading

import com.eltex.androidschool.repository.TestPostRepository
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostViewModel

import org.junit.Assert.assertEquals
import org.junit.Rule
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
 * @see TestPostRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestCoroutinesRule Правило для управления корутинами в тестах.
 */
class PostViewModelTest {

    /**
     * Правило для управления корутинами в тестах.
     *
     * Это правило заменяет основной диспетчер (`Dispatchers.Main`) на тестовый диспетчер (`TestDispatcher`)
     * перед выполнением теста и восстанавливает его после завершения теста. Это позволяет контролировать
     * выполнение корутин в тестах и избегать проблем с асинхронным кодом.
     *
     * Используется в тестах, где необходимо управлять корутинами, чтобы гарантировать корректное выполнение
     * асинхронных операций.
     *
     * @see TestCoroutinesRule Класс, реализующий правило для управления корутинами в тестах.
     */
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
        val firstPostId = 1L
        val secondPostId = 2L

        val firstContent = "Post 1"
        val secondContent = "Post 2"

        val posts = listOf(
            PostData(
                id = firstPostId,
                content = firstContent,
            ),

            PostData(
                id = secondPostId,
                content = secondContent,
            )
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
        val firstPostId = 1L
        val secondPostId = 2L

        val firstContent = "Post 1"
        val secondContent = "Post 2"

        val firstLikedByMe = false
        val secondLikedByMe = false

        val initialPosts = listOf(
            PostData(
                id = firstPostId,
                content = firstContent,
                likedByMe = firstLikedByMe
            ),

            PostData(
                id = secondPostId,
                content = secondContent,
                likedByMe = secondLikedByMe
            )
        )

        val likedPost = PostData(
            id = firstPostId,
            content = firstContent,
            likedByMe = true
        )

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

        viewModel.likeById(
            postId = firstPostId,
            likedByMe = likedPost.likedByMe
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при лайке поста.
     */
    @Test
    fun `like post with error`() {
        val error = RuntimeException("test error like")

        val postId = 1L
        val likedByMe = false

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

        viewModel.likeById(
            postId = postId,
            likedByMe = likedByMe
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного удаления поста.
     */
    @Test
    fun `delete post successfully`() {
        val firstPostId = 1L
        val secondPostId = 2L

        val firstContent = "Post 1"
        val secondContent = "Post 2"

        val firstLikedByMe = false
        val secondLikedByMe = false

        val initialPosts = listOf(
            PostData(
                id = firstPostId,
                content = firstContent,
                likedByMe = firstLikedByMe
            ),

            PostData(
                id = secondPostId,
                content = secondContent,
                likedByMe = secondLikedByMe
            )
        )

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun getPosts(): List<PostData> = initialPosts
                override suspend fun deleteById(postId: Long) {}
            }
        )

        val expectedPosts = initialPosts.filter { post: PostData ->
            post.id != firstPostId
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

        viewModel.deleteById(postId = firstPostId)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при удалении поста.
     */
    @Test
    fun `delete post with error`() {
        val error = RuntimeException("test error delete")

        val postId = 1L

        val viewModel = PostViewModel(
            object : TestPostRepository {
                override suspend fun deleteById(postId: Long) = throw error
            }
        )

        val expected = PostState(
            posts = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.deleteById(postId = postId)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }
}
