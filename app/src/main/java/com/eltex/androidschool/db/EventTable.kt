package com.eltex.androidschool.db


object EventTable {
    const val DB_NAME = "events_database"
    const val DB_VERSION = 1
    const val TABLE_NAME = "events_table"
    const val ID = "id"
    const val AUTHOR = "author"
    const val PUBLISHED = "published"
    const val LAST_MODIFIED = "last_modified"
    const val OPTIONS_CONDUCTING = "option_conducting"
    const val DATA_EVENT = "data_event"
    const val CONTENT = "content"
    const val LINK = "link"
    const val LIKE_BY_ME = "like_by_me"
    const val PARTICIPATE_BY_ME = "participate_by_me"

    val allColumns = arrayOf(
        ID,
        AUTHOR,
        PUBLISHED,
        LAST_MODIFIED,
        OPTIONS_CONDUCTING,
        DATA_EVENT,
        CONTENT,
        LINK,
        LIKE_BY_ME,
        PARTICIPATE_BY_ME
    )
}
