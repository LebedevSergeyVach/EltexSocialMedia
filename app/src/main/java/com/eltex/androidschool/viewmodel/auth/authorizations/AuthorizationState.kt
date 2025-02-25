package com.eltex.androidschool.viewmodel.auth.authorizations

import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние экрана авторизации.
 *
 * @property isButtonEnabled Флаг, указывающий, активна ли кнопка входа.
 * @property statusAuthorization Текущий статус авторизации.
 */
data class AuthorizationState(
    val isButtonEnabled: Boolean = false,
    val statusAuthorization: StatusLoad = StatusLoad.Idle,
) {

    /**
     * Флаг, указывающий, выполняется ли загрузка.
     */
    val isLoading: Boolean
        get() = statusAuthorization == StatusLoad.Loading

    /**
     * Флаг, указывающий, успешно ли выполнена авторизация.
     */
    val isSuccess: Boolean
        get() = statusAuthorization == StatusLoad.Success
}