package com.eltex.androidschool.viewmodel.auth.authorizations

import com.eltex.androidschool.viewmodel.status.StatusLoad

data class AuthorizationState(
    val isButtonEnabled: Boolean = false,
    val statusAuthorization: StatusLoad = StatusLoad.Idle,
) {
    val isLoading: Boolean
        get() = statusAuthorization == StatusLoad.Loading

    val isSuccess: Boolean
        get() = statusAuthorization == StatusLoad.Success
}
