package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Post

/**
 * Реализация интерфейса [PostRepository], который хранит данные о Посте в памяти
 * Получить Пост, поставить лайк
 *
 * @property _state [MutableStateFlow] с данными о событии
 */
class InMemoryPostRepository : PostRepository {
    private val _state = MutableStateFlow(
        Post(
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            content = "Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
        )
    )

    /**
     * Получить пост в виде потока данных [Flow]
     *
     * @return [Flow] с данными о посте
     *
     * @sample [PostRepository]
     * @sample [InMemoryPostRepository]
     */
    override fun getPost(): Flow<Post> = _state.asStateFlow()

    /**
     * Поставить лайк Событию пользователем
     * true & false
     *
     * @sample [PostRepository]
     * @sample [InMemoryPostRepository]
     */
    override fun like() {
        _state.update { post ->
            post.copy(likeByMe = !post.likeByMe)
        }
    }
}
