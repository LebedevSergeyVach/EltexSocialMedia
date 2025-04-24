package com.eltex.androidschool.viewmodel.auth.registration

import android.content.ContentResolver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.repository.auth.AuthRepository
import com.eltex.androidschool.data.media.FileModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

/**
 * ViewModel для экрана регистрации.
 * Управляет состоянием регистрации и взаимодействует с [AuthRepository] для выполнения регистрации пользователя.
 *
 * @property repository Репозиторий для работы с аутентификацией.
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    companion object {
        // Константы для минимальной и максимальной длины полей
        private const val MIN_LENGTH_LOGIN_USERNAME = 4
        private const val MAX_LENGTH_LOGIN_USERNAME = 25
        private const val MIN_LENGTH_PASSWORD = 12
        private const val MAX_LENGTH_PASSWORD = 25
    }

    // Предоставляем доступ к константам длины, если они нужны в Fragment для сообщений
    fun getMinLengthLoginUsername(): Int = MIN_LENGTH_LOGIN_USERNAME
    fun getMaxLengthLoginUsername(): Int = MAX_LENGTH_LOGIN_USERNAME
    fun getMinLengthPassword(): Int = MIN_LENGTH_PASSWORD
    fun getMaxLengthPassword(): Int = MAX_LENGTH_PASSWORD

    /**
     * Внутренний [MutableStateFlow], хранящий состояние регистрации.
     */
    private val _state: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())

    /**
     * Публичный [StateFlow], предоставляющий состояние регистрации для UI.
     */
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    /**
     * Выполняет регистрацию пользователя.
     *
     * @param login Логин пользователя.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param contentResolver [ContentResolver] для работы с файлом аватара.
     * @param onProgress Колбэк для отслеживания прогресса загрузки файла.
     */
    fun register(
        login: String,
        username: String,
        password: String,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit
    ) {
        // Проверка длины перед началом регистрации
        if (!isLoginLengthValid(login)) {
            _state.update { it.copy(statusRegistration = StatusLoad.Error(IllegalArgumentException("Login must be between $MIN_LENGTH_LOGIN_USERNAME and $MAX_LENGTH_LOGIN_USERNAME characters."))) }
            return
        }
        if (!isUsernameLengthValid(username)) {
            _state.update { it.copy(statusRegistration = StatusLoad.Error(IllegalArgumentException("Username must be between $MIN_LENGTH_LOGIN_USERNAME and $MAX_LENGTH_LOGIN_USERNAME characters."))) }
            return
        }
        if (!isPasswordLengthValid(password)) {
            _state.update { it.copy(statusRegistration = StatusLoad.Error(IllegalArgumentException("Password must be between $MIN_LENGTH_PASSWORD and $MAX_LENGTH_PASSWORD characters."))) }
            return
        }

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

    /**
     * Обновляет состояние кнопки в зависимости от введенных данных.
     *
     * @param login Логин пользователя.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     */
    fun updateButtonState(login: String, username: String, password: String) {
        val isButtonEnabled: Boolean =
            username.isNotBlank() && password.isNotBlank() && login.isNotBlank()

        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                isButtonEnabled = isButtonEnabled,
            )
        }
    }

    /**
     * Сохраняет выбранный файл аватара в состоянии.
     *
     * @param file Модель файла, выбранного пользователем.
     */
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
     * Этот метод вызывается для сброса состояния ошибки и возврата в состояние Idle. Используется после обработки ошибки в UI.l.consumerError()
     */
    fun consumerError() {
        _state.update { stateRegistration: RegistrationState ->
            stateRegistration.copy(
                statusRegistration = StatusLoad.Idle,
            )
        }
    }

    private fun getProhibitedUsernamesAndNicknames(): List<String> =
        repository.getProhibitedUsernamesAndNicknames()

    /**
     * Проверяет, содержит ли строка запрещенные слова ("admin" или "админ") в любом регистре.
     *
     * @param input Строка для проверки.
     * @return `true`, если строка содержит запрещенные слова, иначе `false`.
     */
    fun containsForbiddenWords(input: String): Boolean {
        val forbiddenWords = getProhibitedUsernamesAndNicknames()

        return forbiddenWords.any { word: String ->
            input.contains(word, ignoreCase = true)
        }
    }

    /**
     * Проверяет валидность длины логина.
     *
     * @param login Логин для проверки.
     * @return `true`, если длина логина в допустимых пределах, иначе `false`.
     */
    fun isLoginLengthValid(login: String): Boolean {
        return login.length in MIN_LENGTH_LOGIN_USERNAME..MAX_LENGTH_LOGIN_USERNAME
    }

    /**
     * Проверяет валидность длины имени пользователя.
     *
     * @param username Имя пользователя для проверки.
     * @return `true`, если длина имени пользователя в допустимых пределах, иначе `false`.
     */
    fun isUsernameLengthValid(username: String): Boolean {
        return username.length in MIN_LENGTH_LOGIN_USERNAME..MAX_LENGTH_LOGIN_USERNAME
    }

    /**
     * Проверяет валидность длины пароля.
     *
     * @param password Пароль для проверки.
     * @return `true`, если длина пароля в допустимых пределах, иначе `false`.
     */
    fun isPasswordLengthValid(password: String): Boolean {
        return password.length in MIN_LENGTH_PASSWORD..MAX_LENGTH_PASSWORD
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
