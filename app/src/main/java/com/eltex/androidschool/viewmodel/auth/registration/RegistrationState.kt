package com.eltex.androidschool.viewmodel.auth.registration

import com.eltex.androidschool.data.media.FileModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние экрана регистрации.
 *
 * @property isButtonEnabled Флаг, указывающий, активна ли кнопка регистрации.
 * @property statusRegistration Текущий статус регистрации.
 * @property file Модель файла, выбранного пользователем для аватара.
 */
data class RegistrationState(
    val isButtonEnabled: Boolean = false,
    val statusRegistration: StatusLoad = StatusLoad.Idle,
    val file: FileModel? = null,
) {
    /**
     * Флаг, указывающий, выполняется ли загрузка.
     */
    val isLoading: Boolean
        get() = statusRegistration == StatusLoad.Loading

    /**
     * Флаг, указывающий, успешно ли выполнена регистрация.
     */
    val isSuccess: Boolean
        get() = statusRegistration == StatusLoad.Success
}
