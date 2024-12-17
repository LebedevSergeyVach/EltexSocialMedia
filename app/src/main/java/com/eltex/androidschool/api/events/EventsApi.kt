package com.eltex.androidschool.api.events

import com.eltex.androidschool.data.events.EventData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

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
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
interface EventsApi {
    /**
     * Получает список всех событий.
     *
     * @return Single<List<EventData>> Объект Single для выполнения запроса.
     */
    @GET("api/events")
    fun getAllEvents(): Single<List<EventData>>

    /**
     * Сохраняет или обновляет событие.
     *
     * @param event Объект EventData, который нужно сохранить или обновить.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @POST("api/events")
    fun saveEvent(@Body event: EventData): Single<EventData>

    /**
     * Поставить лайк событию по его идентификатору.
     *
     * @param eventId Идентификатор события, которому нужно поставить лайк.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @POST("api/events/{id}/likes")
    fun likeEventById(@Path("id") eventId: Long): Single<EventData>

    /**
     * Убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события, у которого нужно убрать лайк.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @DELETE("api/events/{id}/likes")
    fun unlikeEventById(@Path("id") eventId: Long): Single<EventData>

    /**
     * Участвовать в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @POST("api/events/{id}/participants")
    fun participateEventById(@Path("id") eventId: Long): Single<EventData>

    /**
     * Отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно отказаться от участия.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @DELETE("api/events/{id}/participants")
    fun unsubscribeEventById(@Path("id") eventId: Long): Single<EventData>

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     * @return Completable Объект Completable для выполнения запроса.
     */
    @DELETE("api/events/{id}")
    fun deleteEventById(@Path("id") eventId: Long): Completable

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно получить.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    @GET("api/events/{id}")
    fun getEventById(@Path("id") eventId: Long): Single<EventData>

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
