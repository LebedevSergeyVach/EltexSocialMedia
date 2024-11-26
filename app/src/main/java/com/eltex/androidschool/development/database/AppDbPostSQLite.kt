/*
    Не используется
    Нужен для работы напрямую с запросами SQLite
    На данный момент испоьлзуется AppDbPost ROOM ORM
*/

package com.eltex.androidschool.development.database

import android.content.Context

import android.database.sqlite.SQLiteDatabase

/**
 * Класс для управления базой данных приложения.
 *
 * @property db База данных SQLite.
 */
class AppDbPostSQLite private constructor(
    db: SQLiteDatabase,
) {
    /**
     * DAO для работы с данными постов.
     */
    val postDao = PostDaoImplSQLite(db)

    companion object {
        /**
         * Единственный экземпляр класса [AppDbPostSQLite].
         */
        @Volatile
        private var INSTANCE: AppDbPostSQLite? = null

        /**
         * Получает экземпляр класса [AppDbPostSQLite].
         *
         * @param context Контекст приложения.
         * @return Экземпляр класса [AppDbPostSQLite].
         */
        fun getInstance(context: Context): AppDbPostSQLite {
            val application = context.applicationContext

            INSTANCE?.let { appDbPostSQLite: AppDbPostSQLite ->
                return appDbPostSQLite
            }

            synchronized(this) {
                INSTANCE?.let { appDbPostSQLite: AppDbPostSQLite ->
                    return appDbPostSQLite
                }

                val appDbPostSQLite =
                    AppDbPostSQLite(DbHelperPostSQLite(application).writableDatabase)

                INSTANCE = appDbPostSQLite

                return appDbPostSQLite
            }
        }
    }
}
