//package com.eltex.androidschool.entity.events
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//import com.eltex.androidschool.data.events.EventData
//import com.eltex.androidschool.db.events.EventTableInfo
//import java.time.Instant
//
///**
// * Сущность, представляющая событие в базе данных.
// */
//@Entity(tableName = EventTableInfo.TABLE_NAME)
//class EventEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("id")
//    val id: Long = 0L,
//    @ColumnInfo("author")
//    val author: String = "",
//    @ColumnInfo("published")
//    val published: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//    @ColumnInfo("lastModified")
//    val lastModified: String? = null,
//    @ColumnInfo("optionConducting")
//    val optionConducting: String = "",
//    @ColumnInfo("dataEvent")
//    val dataEvent: String = "",
//    @ColumnInfo("content")
//    val content: String = "",
//    @ColumnInfo("link")
//    val link: String = "",
//    @ColumnInfo("likeByMe")
//    val likeByMe: Boolean = false,
//    @ColumnInfo("participateByMe")
//    val participateByMe: Boolean = false,
//) {
//    companion object {
//        /**
//         * Создает экземпляр [EventEntity] из [EventData].
//         *
//         * @param event Данные события.
//         * @return Экземпляр [EventEntity].
//         */
//        fun fromEventData(event: EventData): EventEntity =
//            with(event) {
//                EventEntity(
//                    id = id,
//                    author = author,
//                    published = published.toString(),
//                    optionConducting = optionConducting,
//                    dataEvent = dataEvent.toString(),
//                    content = content,
//                    link = link,
//                    likeByMe = likedByMe,
//                    participateByMe = participatedByMe,
//                )
//            }
//    }
//
//    /**
//     * Преобразует экземпляр [EventEntity] в [EventData].
//     *
//     * @return Экземпляр [EventData].
//     */
//    fun toEventData(): EventData =
//        EventData(
//            id = id,
//            author = author,
//            published = Instant.parse(published),
//            optionConducting = optionConducting,
//            dataEvent = Instant.parse(dataEvent),
//            content = content,
//            link = link,
//            likedByMe = likeByMe,
//            participatedByMe = participateByMe,
//            // TODO: add serializer for db data
//            likeOwnerIds = emptySet(),
//            // TODO: add serializer for db data
//            participantsIds = emptySet(),
//        )
//}
