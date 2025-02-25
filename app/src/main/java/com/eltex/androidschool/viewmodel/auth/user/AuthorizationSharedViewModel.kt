package com.eltex.androidschool.viewmodel.auth.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.store.UserPreferences

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

/**
 * ViewModel для управления состоянием авторизации пользователя.
 * Предоставляет информацию о том, авторизован ли пользователь, и позволяет очистить данные авторизации.
 *
 * @property userPreferences Хранилище для работы с данными пользователя.
 */
@HiltViewModel
class AuthorizationSharedViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    /**
     * Внутренний [MutableStateFlow], хранящий состояние авторизации.
     */
    private val _isAuthorized = MutableStateFlow(false)

    /**
     * Публичный [StateFlow], предоставляющий состояние авторизации для UI.
     */
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    /**
     * Внутренний [MutableStateFlow], хранящий состояние загрузки.
     */
    private val _isLoading = MutableStateFlow(true)

    /**
     * Публичный [StateFlow], предоставляющий состояние загрузки для UI.
     */
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        checkAuthState()
    }

    /**
     * Проверяет состояние авторизации пользователя.
     * Обновляет [_isAuthorized] и [_isLoading] в зависимости от наличия токена.
     */
    private fun checkAuthState() {
        viewModelScope.launch {
            try {
                userPreferences.authTokenFlow.collect { authToken ->
                    _isAuthorized.value = authToken != null
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isAuthorized.value = false
                _isLoading.value = false
            }
        }
    }

    /**
     * Очищает данные авторизации пользователя.
     *
     * @sample
     * ```
     * viewModel.clearUserData()
     * ```
     */
    suspend fun clearUserData() {
        userPreferences.clearAuthData()
    }
}
