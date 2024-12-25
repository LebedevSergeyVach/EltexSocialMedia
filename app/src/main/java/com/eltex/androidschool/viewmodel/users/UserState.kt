package com.eltex.androidschool.viewmodel.users

import com.eltex.androidschool.data.users.UserData

/**
 * Состояние пользователей в ViewModel.
 *
 * @property users Список пользователей [UserData]. По умолчанию пустой список.
 * @property statusUser Статус загрузки или ошибки [StatusUser]. По умолчанию Idle.
 */
data class UserState(
    val users: List<UserData>? = null,
    val statusUser: StatusUser = StatusUser.Idle,
) {
    /**
     * Флаг, указывающий на то, что данные обновляются (рефреш).
     */
    val isRefreshing: Boolean
        get() = statusUser == StatusUser.Loading && users?.isNotEmpty() == true

    /**
     * Флаг, указывающий на то, что данные загружаются, но список пользователей пуст.
     */
    val isEmptyLoading: Boolean
        get() = statusUser == StatusUser.Loading && users.isNullOrEmpty()

    /**
     * Флаг, указывающий на ошибку при обновлении данных, если список пользователей не пуст.
     */
    val isRefreshError: Boolean
        get() = statusUser is StatusUser.Error && users?.isNotEmpty() == true

    /**
     * Флаг, указывающий на ошибку при обновлении данных, если список пользователей пуст.
     */
    val isEmptyError: Boolean
        get() = statusUser is StatusUser.Error && users.isNullOrEmpty()
}
