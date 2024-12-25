package com.eltex.androidschool.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository

/**
 * ViewModel для управления созданием и обновлением постов.
 *
 * Этот ViewModel отвечает за сохранение нового поста или обновление существующего.
 *
 * @param repository Репозиторий для работы с данными постов.
 * @param postId Идентификатор поста (по умолчанию 0, если создается новое событие).
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 * @see PostRepository Репозиторий для работы с данными постов.
 */
class NewPostViewModel(
    private val repository: PostRepository,
    private val postId: Long = 0L,
) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние создания или обновления поста.
     *
     * @see NewPostState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(NewPostState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию создания или обновления поста.
     *
     * @see NewPostState Состояние, которое предоставляется этим Flow.
     */
    val state = _state.asStateFlow()

    /**
     * Сохраняет или обновляет пост.
     *
     * Если идентификатор поста равен 0, создается новый пост.
     * В противном случае обновляется существующий пост.
     *
     * @param content Содержимое поста.
     */
    fun save(content: String) {
        _state.update { newPostState: NewPostState ->
            newPostState.copy(
                statusPost = StatusPost.Loading
            )
        }

        viewModelScope.launch {
            try {
                val post: PostData = repository.save(
                    postId = postId,
                    content = content
                )

                _state.update { newPostState: NewPostState ->
                    newPostState.copy(
                        statusPost = StatusPost.Idle,
                        post = post,
                    )
                }
            } catch (e: Exception) {
                _state.update { newPostState: NewPostState ->
                    newPostState.copy(
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
        _state.update { newPostState: NewPostState ->
            newPostState.copy(
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
