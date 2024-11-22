package com.eltex.androidschool.dao

import com.eltex.androidschool.data.EventData

interface EventDao {
    fun getAll(): List<EventData>
    fun save(event: EventData): EventData
    fun likeById(eventId: Long): EventData
    fun participateById(eventId: Long): EventData
    fun deleteById(eventId: Long)
    fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    )
}
