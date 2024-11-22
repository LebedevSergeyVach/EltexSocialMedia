package com.eltex.androidschool.db

/**
 * Объект, содержащий константы для работы с таблицей постов в базе данных.
 */
object PostTable {
    /**
     * Имя базы данных.
     */
    const val DB_NAME = "posts_database"

    /**
     * Версия базы данных.
     */
    const val DB_VERSION = 1

    /**
     * Имя таблицы постов.
     */
    const val TABLE_NAME = "posts_table"

    /**
     * Имя столбца для идентификатора поста.
     */
    const val ID = "id"

    /**
     * Имя столбца для автора поста.
     */
    const val AUTHOR = "author"

    /**
     * Имя столбца для даты публикации поста.
     */
    const val PUBLISHED = "published"

    /**
     * Имя столбца для даты последнего изменения поста.
     */
    const val LAST_MODIFIED = "last_modified"

    /**
     * Имя столбца для содержимого поста.
     */
    const val CONTENT = "content"

    /**
     * Имя столбца для флага, указывающего, лайкнул ли пользователь пост.
     */
    const val LIKE_BY_ME = "like_by_me"

    /**
     * Массив имен всех столбцов таблицы постов.
     */
    val allColumns = arrayOf(ID, AUTHOR, PUBLISHED, LAST_MODIFIED, CONTENT, LIKE_BY_ME)
}
