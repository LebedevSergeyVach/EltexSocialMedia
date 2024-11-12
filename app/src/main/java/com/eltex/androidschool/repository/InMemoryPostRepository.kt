package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Post
import java.time.LocalDateTime

/**
 * Реализация интерфейса [PostRepository], хранящая данные о постах в памяти.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see PostRepository Интерфейс, который реализует этот класс.
 */
class InMemoryPostRepository : PostRepository {
    /**
     * Flow, хранящий текущее состояние списка постов.
     * Инициализируется списком из 20 постов с фиктивными данными.
     *
     * @sample [PostRepository.getPost] Пример использования Flow для получения списка постов.
     */
    private val _state = MutableStateFlow(
        List(20) { int ->
            Post(
                id = int.toLong(),
                author = "Lydia Westervelt",
                published = LocalDateTime.now(),
                content = "№ ${int + 1} Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
            )
        }
            .reversed()
    )

    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @return Flow<List<Post>> Flow, излучающий список постов.
     *
     * @sample [PostRepository.getPost] Пример использования метода для получения списка постов.
     */
    override fun getPost(): Flow<List<Post>> = _state.asStateFlow()

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     *
     * @sample [PostRepository.likeById] Пример использования метода для лайка поста.
     */
    override fun likeById(postId: Long) {
        _state.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    post.copy(likeByMe = !post.likeByMe)
                } else {
                    post
                }
            }
        }
    }
}
