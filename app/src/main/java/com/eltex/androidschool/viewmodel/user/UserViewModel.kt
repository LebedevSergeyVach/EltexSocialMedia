package com.eltex.androidschool.viewmodel.user

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
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * ViewModel для управления состоянием пользователей.
 *
 * Этот ViewModel отвечает за загрузку данных пользователей и управление их состоянием.
 *
 * @param repository Репозиторий, который предоставляет данные о пользователях.
 * @param userId Идентификатор пользователя.
 *
 * @see UserRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see UserState Состояние, которое управляется этим ViewModel.
 */
@HiltViewModel(assistedFactory = UserViewModel.ViewModelFactory::class)
class UserViewModel @AssistedInject constructor(
    private val repository: UserRepository,
    @Assisted private val userId: Long,
) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние пользователей.
     *
     * @see UserState Состояние, которое хранится в этом Flow.
     */
    private val _state: MutableStateFlow<UserState> = MutableStateFlow(UserState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию пользователей.
     *
     * @see UserState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        getUserById(userId = userId)
    }

    /**
     * Загружает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя, которого нужно загрузить.
     */
    fun getUserById(userId: Long) {
        _state.update { stateUser: UserState ->
            stateUser.copy(
                statusUser = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val user: UserData = repository.getUserById(userId = userId)

                _state.update { stateUser: UserState ->
                    stateUser.copy(
                        statusUser = StatusLoad.Idle,
                        users = listOf(user)
                    )
                }
            } catch (e: Exception) {
                _state.update { stateUser: UserState ->
                    stateUser.copy(
                        statusUser = StatusLoad.Error(exception = e)
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
                statusUser = StatusLoad.Idle
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

    @AssistedFactory
    interface ViewModelFactory {
        fun create(userId: Long = 0L): UserViewModel
    }
}
