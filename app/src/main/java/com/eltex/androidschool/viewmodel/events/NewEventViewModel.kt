package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.repository.events.EventRepository

/**
 * ViewModel для управления созданием и обновлением событий.
 *
 * Этот ViewModel отвечает за сохранение нового события или обновление существующего.
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 * @see EventRepository Репозиторий для работы с данными событий.
 */
class NewEventViewModel(
    private val repository: EventRepository,
    private val eventId: Long = 0L,
) : ViewModel() {

    /**
     * Сохраняет или обновляет событие.
     *
     * Если идентификатор события равен 0, создается новое событие.
     * В противном случае обновляется существующее событие.
     *
     * @param content Содержимое события.
     * @param link Ссылка на событие.
     * @param option Опция проведения события.
     * @param data Дата события.
     */
    fun save(content: String, link: String, option: String, data: String) {
        if (eventId == 0L) {
            repository.addEvent(
                content = content,
                link = link,
                option = option,
                data = data
            )
        } else {
            repository.updateById(
                eventId = eventId,
                content = content,
                link = link,
                option = option,
                data = data
            )
        }
    }
}
