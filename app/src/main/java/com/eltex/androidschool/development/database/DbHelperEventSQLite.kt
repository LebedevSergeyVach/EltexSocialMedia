/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется PostDao ROOM ORM
*/

package com.eltex.androidschool.development.database

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Класс для управления созданием и обновлением базы данных SQLite.
 *
 * @property context Контекст приложения.
 */
class DbHelperEventSQLite(
    context: Context,
) : SQLiteOpenHelper(context, EventTableSQLite.DB_NAME, null, EventTableSQLite.DB_VERSION) {
    /**
     * Создает таблицу событий в базе данных.
     *
     * @param db База данных SQLite.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE ${EventTableSQLite.TABLE_NAME} (
                    ${EventTableSQLite.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${EventTableSQLite.AUTHOR} TEXT NOT NULL,
                    ${EventTableSQLite.PUBLISHED} TEXT NOT NULL,
                    ${EventTableSQLite.LAST_MODIFIED} TEXT,
                    ${EventTableSQLite.OPTIONS_CONDUCTING} TEXT NOT NULL,
                    ${EventTableSQLite.DATA_EVENT} TEXT NOT NULL,
                    ${EventTableSQLite.CONTENT} TEXT NOT NULL,
                    ${EventTableSQLite.LINK} TEXT NOT NULL,
                    ${EventTableSQLite.LIKE_BY_ME} INTEGER NOT NULL DEFAULT 0,
                    ${EventTableSQLite.PARTICIPATE_BY_ME} INTEGER NOT NULL DEFAULT 0
                )
            """.trimIndent()
        )
    }

    /**
     * Обновляет базу данных при изменении версии.
     *
     * @param db База данных SQLite.
     * @param oldVersion Старая версия базы данных.
     * @param newVersion Новая версия базы данных.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${EventTableSQLite.TABLE_NAME}")

        onCreate(db)
    }
}
