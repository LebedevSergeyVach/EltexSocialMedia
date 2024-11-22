package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.data.EventData

class SQLiteEventRepository(
    private val eventDao: EventDao
) : EventRepository {
    private val _state: MutableStateFlow<List<EventData>> = MutableStateFlow(readEvents())

    override fun getEvent(): Flow<List<EventData>> = _state.asStateFlow()

    override fun likeById(eventId: Long) {
        eventDao.likeById(eventId)

        sync()
    }

    override fun participateById(eventId: Long) {
        eventDao.participateById(eventId)

        sync()
    }

    override fun deleteById(eventId: Long) {
        eventDao.deleteById(eventId)

        sync()
    }

    override fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ) {
        eventDao.updateById(
            eventId = eventId,
            content = content,
            link = link,
            option = option,
            data = data
        )

        sync()
    }

    override fun addEvent(content: String, link: String, option: String, data: String) {
        eventDao.save(
            EventData(
                content = content,
                link = link,
                optionConducting = option,
                dataEvent = data,
                author = "Student"
            )
        )

        sync()
    }

    private fun sync() {
        _state.update {
            readEvents()
        }
    }

    private fun readEvents(): List<EventData> = eventDao.getAll()
}
