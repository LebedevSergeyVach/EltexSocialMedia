/**
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
class DbHelperPostSQLite(
    context: Context,
) : SQLiteOpenHelper(context, PostTableSQLite.DB_NAME, null, PostTableSQLite.DB_VERSION) {
    /**
     * Создает таблицу постов в базе данных.
     *
     * @param db База данных SQLite.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE ${PostTableSQLite.TABLE_NAME} (
                    ${PostTableSQLite.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${PostTableSQLite.AUTHOR} TEXT NOT NULL,
                    ${PostTableSQLite.PUBLISHED} TEXT NOT NULL,
                    ${PostTableSQLite.LAST_MODIFIED} TEXT,
                    ${PostTableSQLite.CONTENT} TEXT NOT NULL,
                    ${PostTableSQLite.LIKE_BY_ME} INTEGER NOT NULL DEFAULT 0
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
        db.execSQL("DROP TABLE IF EXISTS ${PostTableSQLite.TABLE_NAME}")

        onCreate(db)
    }
}
