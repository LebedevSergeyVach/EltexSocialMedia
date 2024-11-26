package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.db.EventTableInfo

@Entity(tableName = EventTableInfo.TABLE_NAME)
class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0L,
    @ColumnInfo("author")
    val author: String = "",
    @ColumnInfo("published")
    val published: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    @ColumnInfo("lastModified")
    val lastModified: String? = null,
    @ColumnInfo("optionConducting")
    val optionConducting: String = "",
    @ColumnInfo("dataEvent")
    val dataEvent: String = "",
    @ColumnInfo("content")
    val content: String = "",
    @ColumnInfo("link")
    val link: String = "",
    @ColumnInfo("likeByMe")
    val likeByMe: Boolean = false,
    @ColumnInfo("participateByMe")
    val participateByMe: Boolean = false,
) {
    companion object {
        fun fromEventData(event: EventData): EventEntity =
            with(event) {
                EventEntity(
                    id = id,
                    author = author,
                    published = published,
                    lastModified = lastModified,
                    optionConducting = optionConducting,
                    dataEvent = dataEvent,
                    content = content,
                    link = link,
                    likeByMe = likeByMe,
                    participateByMe = participateByMe,
                )
            }
    }

    fun toEventData(): EventData =
        EventData(
            id = id,
            author = author,
            published = published,
            lastModified = lastModified,
            optionConducting = optionConducting,
            dataEvent = dataEvent,
            content = content,
            link = link,
            likeByMe = likeByMe,
            participateByMe = participateByMe,
        )
}