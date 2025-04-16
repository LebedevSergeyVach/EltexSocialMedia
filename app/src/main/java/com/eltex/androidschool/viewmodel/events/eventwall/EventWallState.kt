package com.eltex.androidschool.viewmodel.events.eventwall

import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние ViewModel для событий.
 * Содержит список событий, которые будут отображаться в UI.
 *
 * @property events Список событий, которые будут отображаться в UI. По умолчанию пустой список.
 * @property statusEvent Состояние операции. По умолчанию Idle.
 */
data class EventWallState(
    val events: List<EventUiModel>? = null,
    val statusEvent: StatusLoad = StatusLoad.Idle,
) {

    /**
     * Флаг, указывающий, что идет обновление списка событий.
     *
     * @return `true`, если статус загрузки — [StatusLoad.Loading] и список событий не пуст, иначе `false`.
     */
    val isRefreshing: Boolean
        get() = statusEvent == StatusLoad.Loading && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что идет загрузка списка событий.
     *
     * @return `true`, если статус загрузки — [StatusLoad.Loading] и список событий пуст, иначе `false`.
     */
    val isEmptyLoading: Boolean
        get() = statusEvent == StatusLoad.Loading && events.isNullOrEmpty()

    /**
     * Флаг, указывающий, что произошла ошибка при обновлении списка событий.
     *
     * @return `true`, если статус загрузки — [StatusLoad.Error] и список событий не пуст, иначе `false`.
     */
    val isRefreshError: Boolean
        get() = statusEvent is StatusLoad.Error && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что произошла ошибка при загрузке списка событий.
     *
     * @return `true`, если статус загрузки — [StatusLoad.Error] и список событий пуст, иначе `false`.
     */
    val isEmptyError: Boolean
        get() = statusEvent is StatusLoad.Error && events.isNullOrEmpty()
}
