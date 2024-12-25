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
 * Тестовый класс для проверки функциональности ViewModel, отвечающего за создание и обновление постов.
 *
 * Этот класс содержит тесты для проверки корректной работы методов `NewPostViewModel`, таких как:
 * - Сохранение нового поста.
 * - Обновление существующего поста.
 * - Обработка ошибок при выполнении операций.
 *
 * Тесты используют моки репозитория для имитации поведения сервера и проверки корректности обновления состояния ViewModel.
 *
 * @see NewPostViewModel ViewModel, которое тестируется.
 * @see TestPostRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestCoroutinesRule Правило для управления корутинами в тестах.
 */
class NewPostViewModelTest {
    @get:Rule
    val coroutinesRule: TestCoroutinesRule = TestCoroutinesRule()

    /**
     * Маппер для преобразования данных поста в UI-модель.
     *
     * @see PostUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = PostUiModelMapper()

    /**
     * Тест для проверки успешного сохранения нового поста.
     */
    @Test
    fun `save new post successfully`() {
        val content = "New Post Content"
        val newPost = PostData(id = 1, content = content)

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String): PostData = newPost
            }
        )

        val expected = NewPostState(
            post = newPost,
            statusPost = StatusPost.Idle
        )

        viewModel.save(content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
        assertEquals(newPost, actual.post)
    }

    /**
     * Тест для проверки обработки ошибки при сохранении нового поста.
     */
    @Test
    fun `save new post with error`() {
        val error = RuntimeException("test error save")
        val content = "New Post Content"

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String) = throw error
            }
        )

        val expected = NewPostState(
            post = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.save(content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки успешного обновления существующего поста.
     */
    @Test
    fun `update existing post successfully`() {
        val postId = 1L
        val content = "Updated Post Content"
        val updatedPost = PostData(id = postId, content = content)

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String): PostData =
                    updatedPost
            },
            postId = postId
        )

        val expected = NewPostState(
            post = updatedPost,
            statusPost = StatusPost.Idle
        )

        viewModel.save(content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
        assertEquals(updatedPost, viewModel.state.value.post)
    }

    /**
     * Тест для проверки обработки ошибки при обновлении существующего поста.
     */
    @Test
    fun `update existing post with error`() {
        val error = RuntimeException("test error update")
        val postId = 1L
        val content = "Updated Post Content"

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String): PostData = throw error
            },
            postId = postId
        )

        val expected = NewPostState(
            post = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.save(content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }
}
