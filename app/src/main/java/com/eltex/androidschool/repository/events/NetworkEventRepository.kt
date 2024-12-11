package com.eltex.androidschool.repository.events

import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.utils.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback as RetrofitCallback

/**
 * Репозиторий для работы с данными событий через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления, удаления и управления событиями.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @property EventRepository
 * @property EventsApi
 */
class NetworkEventRepository : EventRepository {

    /**
     * Получает список всех событий с сервера.
     *
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun getEvents(callback: Callback<List<EventData>>) {
        val call = EventsApi.INSTANCE.getAllEvents()

        call.enqueue(
            object : RetrofitCallback<List<EventData>> {
                override fun onResponse(
                    call: Call<List<EventData>>, response: Response<List<EventData>>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<List<EventData>>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun likeById(eventId: Long, likedByMe: Boolean, callback: Callback<EventData>) {
        val call = if (likedByMe) {
            EventsApi.INSTANCE.unlikeEventById(eventId)
        } else {
            EventsApi.INSTANCE.likeEventById(eventId)
        }

        call.enqueue(
            object : RetrofitCallback<EventData> {
                override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<EventData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Участвует или отказывается от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun participateById(
        eventId: Long,
        participatedByMe: Boolean,
        callback: Callback<EventData>
    ) {
        val call = if (participatedByMe) {
            EventsApi.INSTANCE.unparticipateEventById(eventId)
        } else {
            EventsApi.INSTANCE.participateEventById(eventId)
        }

        call.enqueue(
            object : RetrofitCallback<EventData> {
                override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<EventData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun deleteById(eventId: Long, callback: Callback<Unit>) {
        val call = EventsApi.INSTANCE.deleteEventById(eventId)

        call.enqueue(
            object : RetrofitCallback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Unit>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события.
     * @param content Содержимое события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String,
        callback: Callback<EventData>
    ) {
        val call = EventsApi.INSTANCE.saveEvent(
            EventData(
                id = eventId,
                content = content,
                link = link,
                optionConducting = option,
                dataEvent = data
            )
        )

        call.enqueue(
            object : RetrofitCallback<EventData> {
                override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<EventData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun getEventById(eventId: Long, callback: Callback<EventData>) {
        val call = EventsApi.INSTANCE.getEventById(eventId)

        call.enqueue(
            object : RetrofitCallback<EventData> {
                override fun onResponse(call: Call<EventData>, response: Response<EventData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<EventData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }
}
