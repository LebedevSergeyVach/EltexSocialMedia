package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.entity.EventEntity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Репозиторий для работы с данными событий, использующий SQLite и StateFlow.
 *
 * @property eventDao DAO для работы с данными событий.
 */
class DaoSQLiteEventRepository(
    private val eventDao: EventDao
) : EventRepository {
    /**
     * Получает Flow со списком событий.
     *
     * @return Flow со списком событий.
     */
    override fun getEvent(): Flow<List<EventData>> =
        eventDao.getAll()
            .map { events: List<EventEntity> ->
                events.map(
                    EventEntity::toEventData
                )
            }

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun likeById(eventId: Long) {
        eventDao.likeById(eventId)
    }

    /**
     * Переключает состояние участия у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun participateById(eventId: Long) {
        eventDao.participateById(eventId)
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun deleteById(eventId: Long) {
        eventDao.deleteById(eventId)
    }

    /**
     * Обновляет содержимое события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param content Новое содержимое события.
     * @param link Ссылка на событие.
     * @param option Вариант проведения события.
     * @param data Дата события.
     */
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

    /**
     * Добавляет новое событие.
     *
     * @param content Содержимое нового события.
     * @param link Ссылка на событие.
     * @param option Вариант проведения события.
     * @param data Дата события.
     */
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
