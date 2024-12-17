package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository

import io.reactivex.rxjava3.core.Single

import org.junit.Assert.assertEquals
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
 * @see PostRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestSchedulersProvider Провайдер планировщиков для синхронного выполнения тестов.
 */
class NewPostViewModelTest {

    /**
     * Тест для проверки успешного сохранения нового поста.
     *
     * @param content Содержимое нового поста.
     * @return Успешное состояние с сохраненным постом.
     */
    @Test
    fun `save new post successfully`() {
        val content = "New Post Content"
        val newPost = PostData(id = 1, content = content)

        val viewModel = NewPostViewModel(
            repository = object : PostRepository {
                override fun save(postId: Long, content: String): Single<PostData> =
                    Single.just(newPost)
            },
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content)

        assertEquals(StatusPost.Idle, viewModel.state.value.statusPost)
        assertEquals(newPost, viewModel.state.value.post)
    }

    /**
     * Тест для проверки обработки ошибки при сохранении нового поста.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.
     */
    @Test
    fun `save new post with error`() {
        val error = RuntimeException("test error save")
        val content = "New Post Content"

        val viewModel = NewPostViewModel(
            repository = object : PostRepository {
                override fun save(postId: Long, content: String): Single<PostData> =
                    Single.error(error)
            },
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content)

        assertEquals(StatusPost.Error(error), viewModel.state.value.statusPost)
    }

    /**
     * Тест для проверки успешного обновления существующего поста.
     *
     * @param postId Идентификатор существующего поста.
     * @param content Новое содержимое поста.
     * @return Успешное состояние с обновленным постом.
     */
    @Test
    fun `update existing post successfully`() {
        val postId = 1L
        val content = "Updated Post Content"
        val updatedPost = PostData(id = postId, content = content)

        val viewModel = NewPostViewModel(
            repository = object : PostRepository {
                override fun save(postId: Long, content: String): Single<PostData> =
                    Single.just(updatedPost)
            },
            postId = postId,
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content)

        assertEquals(StatusPost.Idle, viewModel.state.value.statusPost)
        assertEquals(updatedPost, viewModel.state.value.post)
    }

    /**
     * Тест для проверки обработки ошибки при обновлении существующего поста.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.
     */
    @Test
    fun `update existing post with error`() {
        val error = RuntimeException("test error update")
        val postId = 1L
        val content = "Updated Post Content"

        val viewModel = NewPostViewModel(
            repository = object : PostRepository {
                override fun save(postId: Long, content: String): Single<PostData> =
                    Single.error(error)
            },
            postId = postId,
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content)

        assertEquals(StatusPost.Error(error), viewModel.state.value.statusPost)
    }
}
