package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import androidx.core.content.contentValuesOf

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.db.EventTable
import com.eltex.androidschool.data.EventData

class EventDaoImpl(
    private val db: SQLiteDatabase
) : EventDao {
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

    override fun deleteById(eventId: Long) {
        db.delete(
            EventTable.TABLE_NAME,
            "${EventTable.ID} = ?",
            arrayOf(eventId.toString())
        )
    }

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

private fun Cursor.readEvent(): EventData =
    EventData(
        id = getLong(getColumnIndexOrThrow(EventTable.ID)),
        author = getString(getColumnIndexOrThrow(EventTable.AUTHOR)),
        published = getString(getColumnIndexOrThrow(EventTable.PUBLISHED)),
        lastModified = getString(getColumnIndexOrThrow(EventTable.LAST_MODIFIED)),
        optionConducting = getString(getColumnIndexOrThrow(EventTable.OPTIONS_CONDUCTING)),
        dataEvent = getString(getColumnIndexOrThrow(EventTable.DATA_EVENT)),
        content = getString(getColumnIndexOrThrow(EventTable.CONTENT)),
        link = getString(getColumnIndexOrThrow(EventTable.LINK)),
        likeByMe = getInt(getColumnIndexOrThrow(EventTable.LIKE_BY_ME)) == 1,
        participateByMe = getInt(getColumnIndexOrThrow(EventTable.PARTICIPATE_BY_ME)) == 1,
    )
