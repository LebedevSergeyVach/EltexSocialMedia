package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние ViewModel для событий.
 * Содержит список событий, которые будут отображаться в UI.
 *
 * @property events Список событий, которые будут отображаться в UI. По умолчанию пустой список.
 * @property statusEvent Состояние операции. По умолчанию Idle.
 *
 * @sample [EventViewModel] Пример использования состояния в EventViewModel.
 */
data class EventState(
    val events: List<EventUiModel>? = null,
    val statusEvent: StatusLoad = StatusLoad.Idle,
) {
    /**
     * Флаг, указывающий, что идет обновление списка событий.
     */
    val isRefreshing: Boolean
        get() = statusEvent == StatusLoad.Loading && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что идет загрузка списка событий.
     */
    val isEmptyLoading: Boolean
        get() = statusEvent == StatusLoad.Loading && events.isNullOrEmpty()

    /**
     * Флаг, указывающий, что произошла ошибка при обновлении списка событий.
     */
    val isRefreshError: Boolean
        get() = statusEvent is StatusLoad.Error && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что произошла ошибка при загрузке списка событий.
     */
    val isEmptyError: Boolean
        get() = statusEvent is StatusLoad.Error && events.isNullOrEmpty()
}
