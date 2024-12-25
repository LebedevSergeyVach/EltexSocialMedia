package com.eltex.androidschool.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper

/**
 * ViewModel для управления состоянием постов и взаимодействия с репозиторием.
 *
 * Этот ViewModel отвечает за загрузку, обновление и управление состоянием постов.
 *
 * @param repository Репозиторий, который предоставляет данные о постах.
 *
 * @see PostRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see PostState Состояние, которое управляется этим ViewModel.
 */
class PostViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    /**
     * Маппер для преобразования данных поста в UI-модель.
     *
     * @see PostUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = PostUiModelMapper()

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

        viewModelScope.launch {
            try {
                val posts: List<PostData> = repository.getPosts()

                val postsUiModels: List<PostUiModel> = withContext(Dispatchers.Default) {
                    posts.map { post: PostData ->
                        mapper.map(post)
                    }
                }

                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Idle,
                        posts = postsUiModels,
                    )
                }
            } catch (e: Exception) {
                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     */
    fun likeById(postId: Long, likedByMe: Boolean) {
        viewModelScope.launch {
            try {
                val post: PostData = repository.likeById(
                    postId = postId,
                    likedByMe = likedByMe
                )

                val postsUiModel: List<PostUiModel> = withContext(Dispatchers.Default) {
                    _state.value.posts.orEmpty().map { postUiModel: PostUiModel ->
                        if (postUiModel.id == post.id) {
                            mapper.map(post)
                        } else {
                            postUiModel
                        }
                    }
                }

                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Idle,
                        posts = postsUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    fun deleteById(postId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteById(postId = postId)

                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Idle,
                        posts = _state.value.posts.orEmpty().filter { post: PostUiModel ->
                            post.id != postId
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { statePost: PostState ->
                    statePost.copy(
                        statusPost = StatusPost.Error(exception = e)
                    )
                }
            }
        }
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
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с корутинами.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}
