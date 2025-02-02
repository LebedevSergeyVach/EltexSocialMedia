package com.eltex.androidschool.viewmodel.posts.postswall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления состоянием постов на стене пользователя.
 * Этот класс взаимодействует с [PostWallStore] для получения состояния постов и обработки сообщений,
 * которые изменяют это состояние.
 *
 * @property postWallStore Хранилище (Store), которое управляет состоянием постов и эффектами.
 * @see ViewModel
 */
@HiltViewModel
class PostWallViewModel @Inject constructor(
    private val postWallStore: PostWallStore,
) : ViewModel() {

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию постов.
     *
     * @see PostWallState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<PostWallState> = postWallStore.state

    /**
     * Инициализатор ViewModel.
     * При создании ViewModel запускается подключение к хранилищу (Store) для обработки сообщений и эффектов.
     */
    init {
        viewModelScope.launch {
            postWallStore.connect()
        }
    }

    /**
     * Принимает сообщение и передает его в хранилище (Store) для обработки.
     * Этот метод используется для отправки сообщений, которые изменяют состояние постов,
     * таких как загрузка постов, лайки, удаление и т.д.
     *
     * @param message Сообщение, которое нужно обработать.
     * @see PostWallMessage Типы сообщений, которые могут быть отправлены в хранилище.
     */
    fun accept(message: PostWallMessage) {
        postWallStore.accept(message)
    }

    /**
     * Вызывается при очистке ViewModel.
     * Этот метод освобождает все ресурсы, связанные с корутинами.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}
