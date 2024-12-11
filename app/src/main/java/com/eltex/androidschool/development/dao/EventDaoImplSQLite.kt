/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется EventDaoImpl ROOM ORM
*/
package com.eltex.androidschool.development.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import androidx.core.content.contentValuesOf
import com.eltex.androidschool.dao.events.EventDao

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow
import com.eltex.androidschool.utils.getStringOrNull

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.development.database.EventTableSQLite

/**
 * Реализация интерфейса [EventDao] для работы с данными событий в базе данных SQLite.
 *
 * @property db База данных SQLite.
 */
class EventDaoImplSQLite(
    private val db: SQLiteDatabase
) : EventDaoSQLite {
    /**
     * Получает все события из базы данных.
     *
     * @return Список всех событий.
     */
    override fun getAll(): List<EventData> {
        db.query(
            EventTableSQLite.TABLE_NAME,
            EventTableSQLite.allColumns,
            null,
            null,
            null,
            null,
            "${EventTableSQLite.ID} DESC",
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
            EventTableSQLite.AUTHOR to event.author,
            EventTableSQLite.PUBLISHED to event.published,
            EventTableSQLite.LAST_MODIFIED to event.lastModified,
            EventTableSQLite.OPTIONS_CONDUCTING to event.optionConducting,
            EventTableSQLite.DATA_EVENT to event.dataEvent,
            EventTableSQLite.CONTENT to event.content,
            EventTableSQLite.LINK to event.link,
            EventTableSQLite.LIKE_BY_ME to event.likedByMe,
            EventTableSQLite.PARTICIPATE_BY_ME to event.participatedByMe,
        )

        if (event.id != 0L) {
            contentValues.put(EventTableSQLite.ID, event.id)
        }

        val eventId: Long = db.insert(EventTableSQLite.TABLE_NAME, null, contentValues)

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
                UPDATE ${EventTableSQLite.TABLE_NAME} SET
                    ${EventTableSQLite.LIKE_BY_ME} = CASE WHEN ${EventTableSQLite.LIKE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${EventTableSQLite.ID} =?;
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
                UPDATE ${EventTableSQLite.TABLE_NAME} SET
                    ${EventTableSQLite.PARTICIPATE_BY_ME} = CASE WHEN ${EventTableSQLite.PARTICIPATE_BY_ME} THEN 0 ELSE 1 END
                WHERE ${EventTableSQLite.ID} =?;
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
            EventTableSQLite.TABLE_NAME,
            "${EventTableSQLite.ID} = ?",
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
            EventTableSQLite.CONTENT to content,
            EventTableSQLite.LINK to link,
            EventTableSQLite.OPTIONS_CONDUCTING to option,
            EventTableSQLite.DATA_EVENT to data,
            EventTableSQLite.LAST_MODIFIED to LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )

        db.update(
            EventTableSQLite.TABLE_NAME,
            contentValues,
            "${EventTableSQLite.ID} = ?",
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
            EventTableSQLite.TABLE_NAME,
            EventTableSQLite.allColumns,
            "${EventTableSQLite.ID} = ?",
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
        id = getLongOrThrow(EventTableSQLite.ID),
        author = getStringOrThrow(EventTableSQLite.AUTHOR),
        published = getStringOrThrow(EventTableSQLite.PUBLISHED),
        lastModified = getStringOrNull(EventTableSQLite.LAST_MODIFIED),
        optionConducting = getStringOrThrow(EventTableSQLite.OPTIONS_CONDUCTING),
        dataEvent = getStringOrThrow(EventTableSQLite.DATA_EVENT),
        content = getStringOrThrow(EventTableSQLite.CONTENT),
        link = getStringOrThrow(EventTableSQLite.LINK),
        likedByMe = getBooleanOrThrow(EventTableSQLite.LIKE_BY_ME),
        participatedByMe = getBooleanOrThrow(EventTableSQLite.PARTICIPATE_BY_ME),
    )
