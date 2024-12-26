package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.TestCoroutinesRule
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.TestPostRepository

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

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
     * Тест для проверки успешного сохранения нового поста.
     */
    @Test
    fun `save new post successfully`() {
        val postId = 0L
        val content = "New Post Content"

        val newPost = PostData(
            id = postId,
            content = content
        )

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String): PostData = newPost
            },
            postId = postId
        )

        val expected = NewPostState(
            post = newPost,
            statusPost = StatusPost.Idle
        )

        viewModel.save(content = content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при сохранении нового поста.
     */
    @Test
    fun `save new post with error`() {
        val error = RuntimeException("test error save")

        val postId = 0L
        val content = "New Post Content"

        val viewModel = NewPostViewModel(
            repository = object : TestPostRepository {
                override suspend fun save(postId: Long, content: String) = throw error
            },
            postId = postId
        )

        val expected = NewPostState(
            post = null,
            statusPost = StatusPost.Error(error)
        )

        viewModel.save(content = content)

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

        val updatedPost = PostData(
            id = postId,
            content = content
        )

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

        viewModel.save(content = content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
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

        viewModel.save(content = content)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }
}
