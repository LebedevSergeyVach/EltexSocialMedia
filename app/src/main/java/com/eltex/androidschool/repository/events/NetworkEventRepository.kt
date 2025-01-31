package com.eltex.androidschool.repository.events

import android.content.Context

import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.data.media.MediaDto
import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.media.uploadMedia
import com.eltex.androidschool.viewmodel.common.FileModel

import java.time.Instant

/**
 * Репозиторий для работы с данными событий через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления, удаления и управления событиями.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @see EventRepository Интерфейс, который реализует этот класс.
 * @see EventsApi API для работы с событиями.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
class NetworkEventRepository : EventRepository {

    /**
     * Получает список всех событий с сервера.
     *
     * @return List<[EventData]> Список событий, полученных с сервера.
     */
    override suspend fun getEvents() =
        EventsApi.INSTANCE.getAllEvents()

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return [EventData] Событие с обновленным состоянием лайка.
     */
    override suspend fun likeById(eventId: Long, likedByMe: Boolean): EventData {
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
     * @return [EventData] Событие с обновленным состоянием участия.
     */
    override suspend fun participateById(eventId: Long, participatedByMe: Boolean): EventData {
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
     */
    override suspend fun deleteById(eventId: Long) =
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
     * @return [EventData] Обновленное или сохраненное событие.
     */
    override suspend fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String,
        fileModel: FileModel?,
        context: Context,
        onProgress: (Int) -> Unit,
    ): EventData {
        val event: EventData = fileModel?.let { file: FileModel ->
            val media: MediaDto =
                uploadMedia(fileModel = file, context = context, onProgress = onProgress)

            EventData(
                id = eventId,
                content = content,
                link = link,
                optionConducting = option,
                dataEvent = Instant.parse(data),
                attachment = Attachment(url = media.url, type = file.type),
            )
        } ?: EventData(
            id = eventId,
            content = content,
            link = link,
            optionConducting = option,
            dataEvent = Instant.parse(data),
        )

        return EventsApi.INSTANCE.saveEvent(event = event)
    }

    /**
     * Получает список событий, которые были опубликованы до указанного идентификатора.
     *
     * @param id Идентификатор события, начиная с которого нужно загрузить предыдущие события.
     * @param count Количество событий, которые нужно загрузить.
     * @return [List]<[EventData]> Список событий.
     */
    override suspend fun getBeforeEvents(id: Long, count: Int): List<EventData> =
        EventsApi.INSTANCE.getBeforeEvents(id = id, count = count)

    /**
     * Получает последние события.
     *
     * @param count Количество событий, которые нужно загрузить.
     * @return [List]<[EventData]> Список последних событий.
     */
    override suspend fun getLatestEvents(count: Int): List<EventData> =
        EventsApi.INSTANCE.getLatestEvents(count = count)

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return [EventData] Событие, соответствующее указанному идентификатору.
     */
    suspend fun getEventById(eventId: Long) =
        EventsApi.INSTANCE.getEventById(eventId = eventId)
}
