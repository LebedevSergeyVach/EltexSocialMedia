package com.eltex.androidschool.viewmodel.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.repository.users.UserRepository

/**
 * ViewModel для управления состоянием пользователей.
 *
 * Этот ViewModel отвечает за загрузку данных пользователей и управление их состоянием.
 *
 * @param repository Репозиторий, который предоставляет данные о пользователях.
 *
 * @see UserRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see UserState Состояние, которое управляется этим ViewModel.
 */
class UserViewModel(
    private val repository: UserRepository,
) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние пользователей.
     *
     * @see UserState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(UserState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию пользователей.
     *
     * @see UserState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        getUserById(userId = 0L)
    }

    /**
     * Загружает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя, которого нужно загрузить.
     */
    fun getUserById(userId: Long) {
        _state.update { stateUser: UserState ->
            stateUser.copy(
                statusUser = StatusUser.Loading
            )
        }

        viewModelScope.launch {
            try {
                val user: UserData = repository.getUserById(userId = userId)

                _state.update { stateUser: UserState ->
                    stateUser.copy(
                        statusUser = StatusUser.Idle,
                        users = listOf(user)
                    )
                }
            } catch (e: Exception) {
                _state.update { stateUser: UserState ->
                    stateUser.copy(
                        statusUser = StatusUser.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { stateUser: UserState ->
            stateUser.copy(
                statusUser = StatusUser.Idle
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
