package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Класс для управления созданием и обновлением базы данных SQLite.
 *
 * @property context Контекст приложения.
 */
class DbHelperPost(
    context: Context,
) : SQLiteOpenHelper(context, PostTable.DB_NAME, null, PostTable.DB_VERSION) {
    /**
     * Создает таблицу постов в базе данных.
     *
     * @param db База данных SQLite.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE ${PostTable.TABLE_NAME} (
                    ${PostTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${PostTable.AUTHOR} TEXT NOT NULL,
                    ${PostTable.PUBLISHED} TEXT NOT NULL,
                    ${PostTable.LAST_MODIFIED} TEXT,
                    ${PostTable.CONTENT} TEXT NOT NULL,
                    ${PostTable.LIKE_BY_ME} INTEGER NOT NULL DEFAULT 0
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
        db.execSQL("DROP TABLE IF EXISTS ${PostTable.TABLE_NAME}")

        onCreate(db)
    }
}
