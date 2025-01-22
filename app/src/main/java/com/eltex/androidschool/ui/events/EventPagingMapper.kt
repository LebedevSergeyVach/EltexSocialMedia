package com.eltex.androidschool.ui.events

import com.eltex.androidschool.reducer.events.EventReducer
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventStatus

/**
 * Маппер для преобразования состояния событий в модель пагинации.
 * Используется для отображения событий, ошибок и состояния загрузки в RecyclerView.
 */
object EventPagingMapper {

    /**
     * Преобразует состояние событий в список моделей пагинации.
     *
     * @param state Состояние событий.
     * @return Список моделей пагинации.
     */
    fun map(state: EventState): List<PagingModel<EventUiModel>> {
        val events: List<PagingModel.Data<EventUiModel>> = state.events.map { event: EventUiModel ->
            PagingModel.Data(event)
        }

        return when (val statusValue = state.statusEvent) {
            EventStatus.EmptyLoading -> List(EventReducer.PAGE_SIZE) { PagingModel.Loading }
            EventStatus.NextPageLoading -> events + List(EventReducer.PAGE_SIZE) { PagingModel.Loading }
            is EventStatus.NextPageError -> events + PagingModel.Error(reason = statusValue.reason)
            is EventStatus.EmptyError,
            is EventStatus.Idle,
            EventStatus.Refreshing -> events
        }
    }
}
