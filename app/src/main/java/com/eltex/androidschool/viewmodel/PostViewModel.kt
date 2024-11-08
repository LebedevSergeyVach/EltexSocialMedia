package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    /**
     * Получить Пост, c использованием интерфейса Flow<T> (Flow<Post>)
     * @param PostViewModel class PostViewModel(private val repository: PostRepository) : ViewModel()
     *
     * @sample PostRepository
     * @sample InMemoryPostRepository
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
     * Поставить лайк посту
     * @param PostViewModel class PostViewModel(private val repository: PostRepository) : ViewModel()
     *
     * @sample PostViewModel
     * @sample InMemoryPostRepository
     */
    fun like() {
        repository.like()
    }
}
