package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Event
import kotlinx.coroutines.flow.Flow


interface EventRepository {
    fun getEvent(): Flow<List<Event>>
    fun likeById(eventId: Long)
    fun participateById(eventId: Long)
}
