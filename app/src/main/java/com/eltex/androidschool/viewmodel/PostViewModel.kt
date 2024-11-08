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

/**
 * ViewModel для управления состоянием Поста
 * Взаимодейтвует с [PostRepository] для получения и обновления данных о Посте
 *
 * @property repository Репозиторий Постов
 * @property state [StateFlow] с текущим состоянием Поста
 */
class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    /**
     * Инициализация ViewModel
     * Подписывается на изменения данных о Посте из репозитория и обновляет состояние
     */
    init {
        repository.getPost()
            .onEach { post ->
                _state.update { statePost ->
                    statePost.copy(post = post)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Поставить лайк Посту
     * Вызывает метод [PostRepository.like] для обновления состояния Пота
     */
    fun like() {
        repository.like()
    }
}
