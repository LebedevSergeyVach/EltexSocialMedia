package com.eltex.androidschool.viewmodel.posts.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel для управления состоянием постов.
 * Этот класс взаимодействует с [PostStore] для получения состояния постов и обработки сообщений,
 * которые изменяют это состояние.
 *
 * @property postStore Хранилище (Store), которое управляет состоянием постов и эффектами.
 * @see ViewModel
 */
@HiltViewModel
class PostViewModel @Inject constructor(
    private val postStore: PostStore,
) : ViewModel() {

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию постов.
     *
     * @see PostState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<PostState> = postStore.state

    /**
     * Инициализатор ViewModel.
     * При создании ViewModel запускается подключение к хранилищу (Store) для обработки сообщений и эффектов.
     */
    init {
        viewModelScope.launch {
            postStore.connect()
        }
    }

    /**
     * Принимает сообщение и передает его в хранилище (Store) для обработки.
     * Этот метод используется для отправки сообщений, которые изменяют состояние постов,
     * таких как загрузка постов, лайки, удаление и т.д.
     *
     * @param message Сообщение, которое нужно обработать.
     * @see PostMessage Типы сообщений, которые могут быть отправлены в хранилище.
     */
    fun accept(message: PostMessage) {
        postStore.accept(message)
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
