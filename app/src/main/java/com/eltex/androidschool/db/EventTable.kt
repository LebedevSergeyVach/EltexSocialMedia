package com.eltex.androidschool.db

/**
 * Объект, содержащий константы для работы с таблицей событий в базе данных.
 */
object EventTable {
    /**
     * Имя базы данных.
     */
    const val DB_NAME = "events_database"

    /**
     * Версия базы данных.
     */
    const val DB_VERSION = 1

    /**
     * Имя таблицы событий.
     */
    const val TABLE_NAME = "events_table"

    /**
     * Имя столбца для идентификатора события.
     */
    const val ID = "id"

    /**
     * Имя столбца для автора события.
     */
    const val AUTHOR = "author"

    /**
     * Имя столбца для даты публикации события.
     */
    const val PUBLISHED = "published"

    /**
     * Имя столбца для даты последнего изменения события.
     */
    const val LAST_MODIFIED = "last_modified"

    /**
     * Имя столбца для опции проведения события.
     */
    const val OPTIONS_CONDUCTING = "option_conducting"

    /**
     * Имя столбца для даты события.
     */
    const val DATA_EVENT = "data_event"

    /**
     * Имя столбца для содержимого события.
     */
    const val CONTENT = "content"

    /**
     * Имя столбца для URL-адреса события.
     */
    const val LINK = "link"

    /**
     * Имя столбца для флага, указывающего, лайкнул ли пользователь событие.
     */
    const val LIKE_BY_ME = "like_by_me"

    /**
     * Имя столбца для флага, указывающего, участвует ли пользователь в событии.
     */
    const val PARTICIPATE_BY_ME = "participate_by_me"

    /**
     * Массив имен всех столбцов таблицы событий.
     */
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
