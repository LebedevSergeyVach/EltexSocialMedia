package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Post

/**
 * Репозиторий для работы с постами в памяти
 */
class InMemoryPostRepository : PostRepository {
    private val _state = MutableStateFlow(
        Post(
            content = "Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
        )
    )

    override fun getPost(): Flow<Post> = _state.asStateFlow()

    override fun like() {
        _state.update { post ->
            post.copy(likeByMe = !post.likeByMe)
        }
    }
}
