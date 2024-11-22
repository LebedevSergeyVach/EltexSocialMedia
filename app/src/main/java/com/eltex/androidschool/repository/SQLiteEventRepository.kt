package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.data.EventData

/**
 * Репозиторий для работы с данными событий, использующий SQLite и StateFlow.
 *
 * @property eventDao DAO для работы с данными событий.
 */
class SQLiteEventRepository(
    private val eventDao: EventDao
) : EventRepository {
    /**
     * Flow для хранения состояния списка событий.
     */
    private val _state: MutableStateFlow<List<EventData>> = MutableStateFlow(readEvents())

    /**
     * Получает Flow со списком событий.
     *
     * @return Flow со списком событий.
     */
    override fun getEvent(): Flow<List<EventData>> = _state.asStateFlow()

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun likeById(eventId: Long) {
        eventDao.likeById(eventId)

        sync()
    }

    /**
     * Участвовать или отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun participateById(eventId: Long) {
        eventDao.participateById(eventId)

        sync()
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun deleteById(eventId: Long) {
        eventDao.deleteById(eventId)

        sync()
    }

    /**
     * Обновляет содержимое события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param content Новое содержимое события.
     * @param link Новый URL-адрес события.
     * @param option Новая опция проведения события.
     * @param data Новая дата события.
     */
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

    /**
     * Добавляет новое событие.
     *
     * @param content Содержимое нового события.
     * @param link URL-адрес нового события.
     * @param option Опция проведения нового события.
     * @param data Дата нового события.
     */
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

    /**
     * Синхронизирует состояние Flow с данными из базы данных.
     */
    private fun sync() {
        _state.update {
            readEvents()
        }
    }

    /**
     * Читает все события из базы данных.
     *
     * @return Список всех событий.
     */
    private fun readEvents(): List<EventData> = eventDao.getAll()
}
