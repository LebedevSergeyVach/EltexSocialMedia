package com.eltex.androidschool.api.events

import com.eltex.androidschool.data.events.EventData

import retrofit2.Call
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
 */
interface EventsApi {
    /**
     * Получает список всех событий.
     *
     * @return Call<List<EventData>> Объект Call для выполнения запроса.
     */
    @GET("api/events")
    fun getAllEvents(): Call<List<EventData>>

    /**
     * Сохраняет или обновляет событие.
     *
     * @param event Объект EventData, который нужно сохранить или обновить.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @POST("api/events")
    fun saveEvent(@Body event: EventData): Call<EventData>

    /**
     * Поставить лайк событию по его идентификатору.
     *
     * @param eventId Идентификатор события, которому нужно поставить лайк.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @POST("api/events/{id}/likes")
    fun likeEventById(@Path("id") eventId: Long): Call<EventData>

    /**
     * Убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события, у которого нужно убрать лайк.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @DELETE("api/events/{id}/likes")
    fun unlikeEventById(@Path("id") eventId: Long): Call<EventData>

    /**
     * Участвовать в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @POST("api/events/{id}/participants")
    fun participateEventById(@Path("id") eventId: Long): Call<EventData>

    /**
     * Отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно отказаться от участия.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @DELETE("api/events/{id}/participants")
    fun unparticipateEventById(@Path("id") eventId: Long): Call<EventData>

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     * @return Call<Unit> Объект Call для выполнения запроса.
     */
    @DELETE("api/events/{id}")
    fun deleteEventById(@Path("id") eventId: Long): Call<Unit>

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно получить.
     * @return Call<EventData> Объект Call для выполнения запроса.
     */
    @GET("api/events/{id}")
    fun getEventById(@Path("id") eventId: Long): Call<EventData>

    /**
     * Объект-компаньон для создания экземпляра EventsApi.
     *
     * Использует ленивую инициализацию для создания экземпляра Retrofit и его настройки.
     */
    companion object {
        /**
         * Экземпляр EventsApi, созданный с использованием RetrofitFactoryEvent.
         */
        val INSTANCE by lazy {
            RetrofitFactoryEvent.INSTANCE.create<EventsApi>()
        }
    }
}
