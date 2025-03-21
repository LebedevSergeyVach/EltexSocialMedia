package com.eltex.androidschool.viewmodel.users

import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние списка пользователей.
 *
 * Этот класс содержит данные о списке пользователей и текущем состоянии загрузки.
 * Он также предоставляет вычисляемые свойства для удобства работы с состоянием.
 *
 * @property users Список пользователей. Может быть `null`, если данные еще не загружены.
 * @property statusUsers Текущее состояние загрузки данных (Idle, Loading, Error).
 *
 * @see StatusLoad Интерфейс, описывающий состояние загрузки данных.
 * @see UserData Модель данных пользователя.
 */
data class UsersState (
    val users: List<UserData>? = null,
    val statusUsers: StatusLoad = StatusLoad.Idle,
) {
    /**
     * Возвращает `true`, если данные обновляются, и список пользователей не пуст.
     */
    val isRefreshing: Boolean
        get() = statusUsers == StatusLoad.Loading && users?.isNotEmpty() == true

    /**
     * Возвращает `true`, если данные загружаются, и список пользователей пуст.
     */
    val isEmptyLoading: Boolean
        get() = statusUsers == StatusLoad.Loading && users.isNullOrEmpty()

    /**
     * Возвращает `true`, если произошла ошибка при обновлении данных, и список пользователей не пуст.
     */
    val isRefreshError: Boolean
        get() = statusUsers is StatusLoad.Error && users?.isNotEmpty() == true

    /**
     * Возвращает `true`, если произошла ошибка при загрузке данных, и список пользователей пуст.
     */
    val isEmptyError: Boolean
        get() = statusUsers is StatusLoad.Error && users.isNullOrEmpty()
}
