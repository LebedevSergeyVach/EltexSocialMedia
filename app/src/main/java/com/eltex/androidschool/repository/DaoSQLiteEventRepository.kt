package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.entity.EventEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DaoSQLiteEventRepository(
    private val eventDao: EventDao
) : EventRepository {
    override fun getEvent(): Flow<List<EventData>> =
        eventDao.getAll()
            .map { events: List<EventEntity> ->
                events.map(
                    EventEntity::toEventData
                )
            }

    override fun likeById(eventId: Long) {
        eventDao.likeById(eventId)
    }

    override fun participateById(eventId: Long) {
        eventDao.participateById(eventId)
    }

    override fun deleteById(eventId: Long) {
        eventDao.deleteById(eventId)
    }

    override fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ) {
        val lastModified = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        eventDao.updateById(
            eventId = eventId,
            content = content,
            link = link,
            option = option,
            data = data,
            lastModified = lastModified,
        )
    }

    override fun addEvent(content: String, link: String, option: String, data: String) {
        eventDao.save(
            EventEntity.fromEventData(
                EventData(
                    content = content,
                    link = link,
                    optionConducting = option,
                    dataEvent = data,
                    author = "Student"
                )
            )
        )
    }
}
