package com.eltex.androidschool.ui.events

import com.eltex.androidschool.data.events.EventData

import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Маппер для преобразования данных события (EventData) в модель UI (EventUiModel).
 *
 * Этот класс используется для преобразования данных, полученных из API, в формат,
 * подходящий для отображения в пользовательском интерфейсе.
 *
 * @see EventData Класс, представляющий данные события.
 * @see EventUiModel Класс, представляющий модель UI для события.
 */
class EventUiModelMapper {
    private companion object {
        /**
         * Форматтер для преобразования даты и времени в строку.
         *
         * Используется для форматирования даты и времени в формате "dd.MM.yy HH.mm".
         */
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    /**
     * Преобразует объект EventData в объект EventUiModel.
     *
     * Этот метод преобразует данные события в формат, подходящий для отображения в UI.
     *
     * @param event Объект EventData, который нужно преобразовать.
     * @return Объект EventUiModel, представляющий событие в UI.
     */
    fun map(event: EventData): EventUiModel = with(event)
    {
        EventUiModel(
            id = id,
            author = author,
            authorId = authorId,
            authorAvatar = authorAvatar,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            optionConducting = optionConducting,
            dataEvent = FORMATTER.format(dataEvent?.atZone(ZoneId.systemDefault())),
            content = content,
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            participatedByMe = participatedByMe,
            participates = participantsIds.size,
            attachment = attachment,
        )
    }
}
