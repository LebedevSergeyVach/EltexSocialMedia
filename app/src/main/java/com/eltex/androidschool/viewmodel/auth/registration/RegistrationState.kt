package com.eltex.androidschool.viewmodel.auth.registration

import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

data class RegistrationState(
    val isButtonEnabled: Boolean = false,
    val statusRegistration: StatusLoad = StatusLoad.Idle,
    val file: FileModel? = null,
) {
    val isLoading: Boolean
        get() = statusRegistration == StatusLoad.Loading

    val isSuccess: Boolean
        get() = statusRegistration == StatusLoad.Success
}
