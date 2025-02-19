package com.eltex.androidschool.viewmodel.auth.registration

import android.content.ContentResolver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.repository.auth.AuthRepository
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.viewmodel.common.FileModel
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
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    fun register(
        login: String,
        username: String,
        password: String,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit
    ) {
        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                statusRegistration = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val response: AuthData = repository.register(
                    login = login,
                    username = username,
                    password = password,
                    fileModel = _state.value.file,
                    contentResolver = contentResolver,
                    onProgress = onProgress,
                )

                if (response.token.isNotEmpty()) {
                    _state.update { stateRegistration: RegistrationState ->
                        stateRegistration.copy(
                            statusRegistration = StatusLoad.Success,
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { stateRegistration: RegistrationState ->
                    stateRegistration.copy(
                        statusRegistration = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun updateButtonState(login: String, username: String, password: String) {
        val isButtonEnabled: Boolean =
            username.isNotBlank() && password.isNotBlank() && login.isNotBlank()

        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                isButtonEnabled = isButtonEnabled,
            )
        }
    }

    fun saveAttachmentFileType(file: FileModel?) {
        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                file = file,
            )
        }
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     *
     * Этот метод вызывается для сброса состояния ошибки и возврата в состояние Idle. Используется после обработки ошибки в UI.
     */
    fun consumerError() {
        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                statusRegistration = StatusLoad.Idle,
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
