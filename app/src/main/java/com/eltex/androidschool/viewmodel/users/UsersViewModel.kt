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
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * ViewModel для управления состоянием списка пользователей.
 *
 * Этот ViewModel отвечает за загрузку данных о пользователях из репозитория и управление состоянием
 * списка пользователей. Он использует `MutableStateFlow` для хранения состояния и предоставляет
 * `StateFlow` для наблюдения за изменениями состояния.
 *
 * @param repository Репозиторий для получения данных о пользователях.
 *
 * @see UserRepository Интерфейс репозитория для работы с данными пользователей.
 * @see UsersState Состояние списка пользователей.
 * @see StatusLoad Состояние загрузки данных (Idle, Loading, Error).
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    /**
     * Внутренний поток для хранения состояния списка пользователей.
     */
    private val _state: MutableStateFlow<UsersState> = MutableStateFlow(UsersState())

    /**
     * Публичный поток для наблюдения за состоянием списка пользователей.
     */
    val state: StateFlow<UsersState> = _state.asStateFlow()

    /**
     * Инициализация ViewModel. При создании ViewModel автоматически загружает список пользователей.
     */
    init {
        load()
    }

    /**
     * Загружает список пользователей из репозитория.
     *
     * Этот метод обновляет состояние на `Loading`, а затем пытается получить данные из репозитория.
     * В случае успеха состояние обновляется на `Idle` с новым списком пользователей.
     * В случае ошибки состояние обновляется на `Error` с соответствующим исключением.
     *
     * @throws Exception Может быть выброшено, если произошла ошибка при загрузке данных.
     */
    fun load() {
        getAllUsers()
    }

    /**
     * Внутренний метод для загрузки списка пользователей.
     *
     * Этот метод обновляет состояние на `Loading`, а затем асинхронно загружает данные из репозитория.
     * В случае успеха состояние обновляется на `Idle` с новым списком пользователей.
     * В случае ошибки состояние обновляется на `Error` с соответствующим исключением.
     *
     * @see UserRepository.getUsers Метод репозитория для получения списка пользователей.
     */
    private fun getAllUsers() {
        _state.update { stateUsers: UsersState ->
            stateUsers.copy(
                statusUsers = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val users: List<UserData> = repository.getUsers()

                _state.update { stateUsers: UsersState ->
                    stateUsers.copy(
                        statusUsers = StatusLoad.Idle,
                        users = users.reversed()
                    )
                }
            } catch (e: Exception) {
                _state.update { stateUsers: UsersState ->
                    stateUsers.copy(
                        statusUsers = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Сбрасывает состояние ошибки.
     *
     * Этот метод обновляет состояние на `Idle`, что позволяет скрыть сообщение об ошибке.
     */
    fun consumerError() {
        _state.update { stateUsers: UsersState ->
            stateUsers.copy(
                statusUsers = StatusLoad.Idle
            )
        }
    }

    /**
     * Очищает ресурсы ViewModel при его уничтожении.
     *
     * Этот метод отменяет все корутины, запущенные в `viewModelScope`.
     *
     * @see viewModelScope Область видимости корутин, привязанная к ViewModel.
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}
