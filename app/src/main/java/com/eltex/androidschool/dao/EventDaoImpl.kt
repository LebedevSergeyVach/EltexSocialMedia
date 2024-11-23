package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import androidx.core.content.contentValuesOf

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow
import com.eltex.androidschool.utils.getStringOrNull

import com.eltex.androidschool.db.EventTable
import com.eltex.androidschool.data.EventData

/**
 * Реализация интерфейса [EventDao] для работы с данными событий в базе данных SQLite.
 *
 * @property db База данных SQLite.
 */
class EventDaoImpl(
    private val db: SQLiteDatabase
) : EventDao {
    /**
     * Получает все события из базы данных.
     *
     * @return Список всех событий.
     */
    override fun getAll(): List<EventData> {
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            null,
            null,
            null,
            null,
            "${EventTable.ID} DESC",
        ).use { cursor: Cursor ->
            val events = mutableListOf<EventData>()

            while (cursor.moveToNext()) {
                events.add(cursor.readEvent())
            }

            return events
        }
    }

    /**
     * Сохраняет новое событие в базу данных.
     *
     * @param event Событие для сохранения.
     * @return Сохраненное событие с обновленным идентификатором.
     */
    override fun save(event: EventData): EventData {
        val contentValues = contentValuesOf(
            EventTable.AUTHOR to event.author,
            EventTable.PUBLISHED to event.published,
            EventTable.LAST_MODIFIED to event.lastModified,
            EventTable.OPTIONS_CONDUCTING to event.optionConducting,
            EventTable.DATA_EVENT to event.dataEvent,
            EventTable.CONTENT to event.content,
            EventTable.LINK to event.link,
            EventTable.LIKE_BY_ME to event.likeByMe,
            EventTable.PARTICIPATE_BY_ME to event.participateByMe,
        )

        if (event.id != 0L) {
            contentValues.put(EventTable.ID, event.id)
        }

        val eventId: Long = db.insert(EventTable.TABLE_NAME, null, contentValues)

        return getEventById(eventId)
    }

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Обновленное событие.
     */
    override fun likeById(eventId: Long): EventData {
        db.execSQL(
            """
                UPDATE ${EventTable.TABLE_NAME} SET
                    ${EventTable.LIKE_BY_ME} = CASE WHEN ${EventTable.LIKE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${EventTable.ID} =?;
            """.trimIndent(),
            arrayOf(eventId.toString())
        )

        return getEventById(eventId)
    }

    /**
     * Участвовать или отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Обновленное событие.
     */
    override fun participateById(eventId: Long): EventData {
        db.execSQL(
            """
                UPDATE ${EventTable.TABLE_NAME} SET
                    ${EventTable.PARTICIPATE_BY_ME} = CASE WHEN ${EventTable.PARTICIPATE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${EventTable.ID} =?;
            """.trimIndent(),
            arrayOf(eventId.toString())
        )

        return getEventById(eventId)
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    override fun deleteById(eventId: Long) {
        db.delete(
            EventTable.TABLE_NAME,
            "${EventTable.ID} = ?",
            arrayOf(eventId.toString())
        )
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
        val contentValues = contentValuesOf(
            EventTable.CONTENT to content,
            EventTable.LINK to link,
            EventTable.OPTIONS_CONDUCTING to option,
            EventTable.DATA_EVENT to data,
            EventTable.LAST_MODIFIED to LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )

        db.update(
            EventTable.TABLE_NAME,
            contentValues,
            "${EventTable.ID} = ?",
            arrayOf(eventId.toString())
        )
    }

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Событие с указанным идентификатором.
     * @throws IllegalArgumentException Если событие с указанным идентификатором не найдено.
     */
    private fun getEventById(eventId: Long): EventData =
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            "${EventTable.ID} = ?",
            arrayOf(eventId.toString()),
            null,
            null,
            null,
        ).use { cursor: Cursor ->
            cursor.moveToFirst()

            return cursor.readEvent()
        }
}

/**
 * Расширение для [Cursor], которое преобразует данные из курсора в объект [EventData].
 *
 * @return Объект [EventData], созданный из данных курсора.
 */
private fun Cursor.readEvent(): EventData =
    EventData(
        id = getLongOrThrow(EventTable.ID),
        author = getStringOrThrow(EventTable.AUTHOR),
        published = getStringOrThrow(EventTable.PUBLISHED),
        lastModified = getStringOrNull(EventTable.LAST_MODIFIED),
        optionConducting = getStringOrThrow(EventTable.OPTIONS_CONDUCTING),
        dataEvent = getStringOrThrow(EventTable.DATA_EVENT),
        content = getStringOrThrow(EventTable.CONTENT),
        link = getStringOrThrow(EventTable.LINK),
        likeByMe = getBooleanOrThrow(EventTable.LIKE_BY_ME),
        participateByMe = getBooleanOrThrow(EventTable.PARTICIPATE_BY_ME),
    )
