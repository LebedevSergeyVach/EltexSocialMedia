package com.eltex.androidschool.viewmodel.posts

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.rx.posts.SchedulersProvider

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

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

        repository.save(
            postId = postId,
            content = content
        )
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { newPost: PostData ->
                    _state.update { newPostState: NewPostState ->
                        newPostState.copy(
                            statusPost = StatusPost.Idle,
                            post = newPost,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { newPostState: NewPostState ->
                        newPostState.copy(
                            statusPost = StatusPost.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
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

    override fun onCleared() {
        disposable.dispose()
    }
}
