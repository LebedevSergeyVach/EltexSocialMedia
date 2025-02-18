package com.eltex.androidschool.viewmodel.auth.authorizations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.repository.auth.AuthRepository
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<AuthorizationState> =
        MutableStateFlow(AuthorizationState())
    val state: StateFlow<AuthorizationState> = _state.asStateFlow()

    fun login(login: String, password: String) {
        _state.update { stateAuthorization: AuthorizationState ->
            stateAuthorization.copy(
                statusAuthorization = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val response: AuthData = repository.login(
                    login = login,
                    password = password,
                )

                if (response.token.isNotEmpty()) {
                    _state.update { stateAuthorization: AuthorizationState ->
                        stateAuthorization.copy(
                            statusAuthorization = StatusLoad.Success,
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { stateAuthorization: AuthorizationState ->
                    stateAuthorization.copy(
                        statusAuthorization = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun updateButtonState(username: String, password: String) {
        val isButtonEnabled = username.isNotBlank() && password.isNotBlank()
        _state.update { stateAuthorization: AuthorizationState ->
            stateAuthorization.copy(
                isButtonEnabled = isButtonEnabled,
            )
        }
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     *
     * Этот метод вызывается для сброса состояния ошибки и возврата в состояние Idle. Используется после обработки ошибки в UI.
     */
    fun consumerError() {
        _state.update { stateAuthorization: AuthorizationState ->
            stateAuthorization.copy(
                statusAuthorization = StatusLoad.Idle,
            )
        }
    }

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с корутинами. Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope Scope для корутин, связанных с жизненным циклом ViewModel.
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}
