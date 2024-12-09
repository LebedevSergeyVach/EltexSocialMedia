package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.data.PostData
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception

/**
 * ViewModel для управления созданием и обновлением постов.
 *
 * Этот ViewModel отвечает за сохранение нового поста или обновление существующего.
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
                status = Status.Loading
            )
        }

        repository.save(
            postId = postId,
            content = content,
            callback = object : Callback<PostData> {
                override fun onSuccess(data: PostData) {
                    _state.update { newPostState: NewPostState ->
                        newPostState.copy(
                            status = Status.Idle,
                            post = data,
                        )
                    }

                    PostViewModel(repository).load()
                }

                override fun onError(exception: Exception) {
                    _state.update { newPostState: NewPostState ->
                        newPostState.copy(
                            status = Status.Error(exception)
                        )
                    }
                }
            }
        )
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { newPostState: NewPostState ->
            newPostState.copy(status = Status.Idle)
        }
    }
}
