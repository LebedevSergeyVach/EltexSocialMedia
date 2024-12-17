package com.eltex.androidschool.repository.events

import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.data.events.EventData

import io.reactivex.rxjava3.core.Single

import java.time.Instant

/**
 * Репозиторий для работы с данными событий через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления, удаления и управления событиями.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @see EventRepository Интерфейс, который реализует этот класс.
 * @see EventsApi API для работы с событиями.
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
class NetworkEventRepository : EventRepository {

    /**
     * Получает список всех событий с сервера.
     *
     * @return Single<List<EventData>> Объект Single для выполнения запроса.
     */
    override fun getEvents() =
        EventsApi.INSTANCE.getAllEvents()

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    override fun likeById(eventId: Long, likedByMe: Boolean): Single<EventData> {
        return if (likedByMe) {
            EventsApi.INSTANCE.unlikeEventById(eventId = eventId)
        } else {
            EventsApi.INSTANCE.likeEventById(eventId = eventId)
        }
    }

    /**
     * Участвует или отказывается от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    override fun participateById(eventId: Long, participatedByMe: Boolean): Single<EventData> {
        return if (participatedByMe) {
            EventsApi.INSTANCE.unsubscribeEventById(eventId = eventId)
        } else {
            EventsApi.INSTANCE.participateEventById(eventId = eventId)
        }
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Completable Объект Completable для выполнения запроса.
     */
    override fun deleteById(eventId: Long) =
        EventsApi.INSTANCE.deleteEventById(eventId = eventId)

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события.
     * @param content Содержимое события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    override fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ) = EventsApi.INSTANCE.saveEvent(
        event = EventData(
            id = eventId,
            content = content,
            link = link,
            optionConducting = option,
            dataEvent = Instant.parse(data),
        )
    )

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    fun getEventById(eventId: Long) =
        EventsApi.INSTANCE.getEventById(eventId = eventId)
}
