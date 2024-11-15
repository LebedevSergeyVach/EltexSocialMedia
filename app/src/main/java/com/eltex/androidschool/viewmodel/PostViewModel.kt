package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.data.Post

/**
 * ViewModel для управления состоянием постов и взаимодействия с репозиторием.
 *
 * @param repository Репозиторий, который предоставляет данные о постах.
 *
 * @see PostRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see PostState Состояние, которое управляется этим ViewModel.
 */
class PostViewModel(private val repository: PostRepository) : ViewModel() {
    /**
     * Flow, хранящий текущее состояние постов.
     *
     * @see PostState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(PostState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию постов.
     *
     * @see PostState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<PostState> = _state.asStateFlow()

    /**
     * Инициализатор ViewModel.
     * Подписывается на изменения в Flow репозитория и обновляет состояние постов.
     */
    init {
        repository.getPost()
            .onEach { posts: List<Post> ->
                _state.update { statePost: PostState ->
                    statePost.copy(posts = posts)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     */
    fun likeById(postId: Long) {
        repository.likeById(postId)
    }

    /**
     * Удаляет пост по его id.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    fun deleteById(postId: Long) {
        repository.deleteById(postId)
    }

    /**
     * Обновляет пост по его id.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     */
    fun updateById(postId: Long, content: String) {
        repository.updateById(postId, content)
    }

    /**
     * Добавляет новый пост.
     *
     * @param content Содержание нового поста.
     */
    fun addPost(content: String) {
        repository.addPost(content)
    }
}
