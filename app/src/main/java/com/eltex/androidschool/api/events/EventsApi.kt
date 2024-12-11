package com.eltex.androidschool.api.events

import com.eltex.androidschool.data.events.EventData

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface EventsApi {
    @GET("api/events")
    fun getAllEvents(): Call<List<EventData>>

    @POST("api/events")
    fun saveEvent(@Body event: EventData): Call<EventData>

    @POST("api/events/{id}/likes")
    fun likeEventById(@Path("id") eventId: Long): Call<EventData>

    @DELETE("api/events/{id}/likes")
    fun unlikeEventById(@Path("id") eventId: Long): Call<EventData>

    @POST("api/events/{id}/participants")
    fun participateEventById(@Path("id") eventId: Long): Call<EventData>

    @DELETE("api/events/{id}/participants")
    fun unparticipateEventById(@Path("id") eventId: Long): Call<EventData>

    @DELETE("api/events/{id}")
    fun deleteEventById(@Path("id") eventId: Long): Call<Unit>

    @GET("api/events/{id}")
    fun getEventById(@Path("id") eventId: Long): Call<EventData>
}
