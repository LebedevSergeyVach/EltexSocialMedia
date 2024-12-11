package com.eltex.androidschool.viewmodel.posts

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.utils.Callback

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        load()
    }

    /**
     * Загружает список постов с сервера.
     */
    fun load() {
        _state.update { statePost: PostState ->
            statePost.copy(
                statusPost = StatusPost.Loading
            )
        }

        repository.getPosts(
            object : Callback<List<PostData>> {
                override fun onError(exception: Throwable) {
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(throwable = exception)
                        )
                    }
                }

                override fun onSuccess(data: List<PostData>) {
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Idle,
                            posts = data,
                        )
                    }
                }
            }
        )
    }

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     */
    fun likeById(postId: Long, likedByMe: Boolean) {
        _state.update { statePost: PostState ->
            statePost.copy(
                statusPost = StatusPost.Loading
            )
        }

        repository.likeById(
            postId = postId,
            likedByMe = likedByMe,
            callback = object : Callback<PostData> {
                override fun onError(exception: Throwable) {
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(throwable = exception)
                        )
                    }
                }

                override fun onSuccess(data: PostData) {
                    updatePostInList(data)
                }
            }
        )
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    fun deleteById(postId: Long) {
        _state.update { statePost: PostState ->
            statePost.copy(
                statusPost = StatusPost.Loading
            )
        }

        repository.deleteById(
            postId = postId,
            callback = object : Callback<Unit> {
                override fun onError(exception: Throwable) {
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(exception)
                        )
                    }
                }

                override fun onSuccess(data: Unit) {
                   load()
                }
            }
        )
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { postState: PostState ->
            postState.copy(
                statusPost = StatusPost.Idle
            )
        }
    }

    /**
     * Обновляет состояние списка постов, заменяя старый пост на обновленный.
     *
     * @param updatedPost Обновленный пост.
     */
    private fun updatePostInList(updatedPost: PostData) {
        _state.update { statePost: PostState ->
            val updatedPosts = statePost.posts?.map { post: PostData ->
                if (post.id == updatedPost.id) updatedPost else post
            }

            statePost.copy(
                statusPost = StatusPost.Idle,
                posts = updatedPosts
            )
        }
    }
}
