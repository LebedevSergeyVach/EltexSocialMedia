package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Класс для управления созданием и обновлением базы данных SQLite.
 *
 * @property context Контекст приложения.
 */
class DbHelperEvent(
    context: Context,
) : SQLiteOpenHelper(context, EventTable.DB_NAME, null, EventTable.DB_VERSION) {
    /**
     * Создает таблицу событий в базе данных.
     *
     * @param db База данных SQLite.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE ${EventTable.TABLE_NAME} (
                    ${EventTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${EventTable.AUTHOR} TEXT NOT NULL,
                    ${EventTable.PUBLISHED} TEXT NOT NULL,
                    ${EventTable.LAST_MODIFIED} TEXT,
                    ${EventTable.OPTIONS_CONDUCTING} TEXT NOT NULL,
                    ${EventTable.DATA_EVENT} TEXT NOT NULL,
                    ${EventTable.CONTENT} TEXT NOT NULL,
                    ${EventTable.LINK} TEXT NOT NULL,
                    ${EventTable.LIKE_BY_ME} INTEGER NOT NULL DEFAULT 0,
                    ${EventTable.PARTICIPATE_BY_ME} INTEGER NOT NULL DEFAULT 0
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
        db.execSQL("DROP TABLE IF EXISTS ${EventTable.TABLE_NAME}")

        onCreate(db)
    }
}
