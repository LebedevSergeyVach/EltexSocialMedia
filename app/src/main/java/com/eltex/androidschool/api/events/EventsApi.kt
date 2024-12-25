package com.eltex.androidschool.api.events

import com.eltex.androidschool.data.events.EventData

import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Интерфейс для работы с API событий.
 *
 * Этот интерфейс определяет методы для выполнения сетевых запросов к API событий.
 * Каждый метод соответствует определённому HTTP-методу и URL.
 *
 * @see EventData Класс, представляющий данные события.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface EventsApi {
    /**
     * Получает список всех событий.
     *
     * @return List<[EventData]> Список событий, полученных с сервера
     */
    @GET("api/events")
    suspend fun getAllEvents(): List<EventData>

    /**
     * Сохраняет или обновляет событие.
     *
     * @param event Объект EventData, который нужно сохранить или обновить.
     * @return [EventData] Обновленное или сохраненное событие.
     */
    @POST("api/events")
    suspend fun saveEvent(@Body event: EventData): EventData

    /**
     * Поставить лайк событию по его идентификатору.
     *
     * @param eventId Идентификатор события, которому нужно поставить лайк.
     * @return [EventData] Событие с обновленным состоянием лайка.
     */
    @POST("api/events/{id}/likes")
    suspend fun likeEventById(@Path("id") eventId: Long): EventData

    /**
     * Убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события, у которого нужно убрать лайк.
     * @return [EventData] Событие с обновленным состоянием лайка.
     */
    @DELETE("api/events/{id}/likes")
    suspend fun unlikeEventById(@Path("id") eventId: Long): EventData

    /**
     * Участвовать в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @return [EventData] Событие с обновленным состоянием участия.
     */
    @POST("api/events/{id}/participants")
    suspend fun participateEventById(@Path("id") eventId: Long): EventData

    /**
     * Отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно отказаться от участия.
     * @return [EventData] Событие с обновленным состоянием участия.
     */
    @DELETE("api/events/{id}/participants")
    suspend fun unsubscribeEventById(@Path("id") eventId: Long): EventData

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     */
    @DELETE("api/events/{id}")
    suspend fun deleteEventById(@Path("id") eventId: Long)

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно получить.
     * @return [EventData] Событие, соответствующее указанному идентификатору.
     */
    @GET("api/events/{id}")
    suspend fun getEventById(@Path("id") eventId: Long): EventData

    /**
     * Объект-компаньон для создания экземпляра [EventsApi].
     *
     * Использует ленивую инициализацию для создания экземпляра Retrofit и его настройки.
     */
    companion object {
        /**
         * Экземпляр [EventsApi], созданный с использованием [RetrofitFactoryEvent].
         */
        val INSTANCE by lazy {
            RetrofitFactoryEvent.INSTANCE.create<EventsApi>()
        }
    }
}
