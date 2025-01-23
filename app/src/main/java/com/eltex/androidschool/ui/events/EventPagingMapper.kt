package com.eltex.androidschool.ui.events

import android.content.Context
import com.eltex.androidschool.R

import com.eltex.androidschool.reducer.events.EventReducer
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventStatus

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    fun map(state: EventState, context: Context): List<PagingModel<EventUiModel>> {
        val events: List<PagingModel.Data<EventUiModel>> = state.events.map { event: EventUiModel ->
            PagingModel.Data(event)
        }

        val  groupedEvents = mutableListOf<EventPagingModel>()
        var lastDate: String? = null

        for (event in events) {
            val eventDate = getFormattedDate(date = event.value.published, context = context)

            if (eventDate != lastDate) {
                groupedEvents.add(PagingModel.DateSeparator(eventDate))
                lastDate = eventDate
            }

            groupedEvents.add(event)
        }

        return when (val statusValue = state.statusEvent) {
            EventStatus.EmptyLoading -> List(EventReducer.PAGE_SIZE) { PagingModel.Loading }
            EventStatus.NextPageLoading -> events + List(EventReducer.PAGE_SIZE) { PagingModel.Loading }
            is EventStatus.NextPageError -> events + PagingModel.Error(reason = statusValue.reason)
            is EventStatus.EmptyError,
            is EventStatus.Idle,
            EventStatus.Refreshing -> groupedEvents
        }
    }

    /**
     * Форматирует дату в строку с использованием строковых ресурсов.
     *
     * @param date Дата в формате строки.
     * @param context Контекст для доступа к строковым ресурсам.
     * @return Отформатированная строка с датой.
     */
    private fun getFormattedDate(date: String, context: Context): String {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val postEvent = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"))

        return when (postEvent) {
            today -> context.getString(R.string.today)
            yesterday -> context.getString(R.string.yesterday)
            else -> postEvent.format(DateTimeFormatter.ofPattern("dd MM yyyy"))
        }
    }
}
