package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase

import com.eltex.androidschool.dao.EventDaoImpl

/**
 * Класс для управления базой данных приложения.
 *
 * @property db База данных SQLite.
 */
class AppDbEvent private constructor(
    db: SQLiteDatabase,
) {
    /**
     * DAO для работы с данными событий.
     */
    val eventDao = EventDaoImpl(db)

    companion object {
        /**
         * Единственный экземпляр класса [AppDbEvent].
         */
        @Volatile
        private var INSTANCE: AppDbEvent? = null

        /**
         * Получает экземпляр класса [AppDbEvent].
         *
         * @param context Контекст приложения.
         * @return Экземпляр класса [AppDbEvent].
         */
        fun getInstance(context: Context): AppDbEvent {
            val application = context.applicationContext

            INSTANCE?.let { appDbEvent: AppDbEvent ->
                return appDbEvent
            }

            synchronized(this) {
                INSTANCE?.let { appDbEvent: AppDbEvent ->
                    return appDbEvent
                }

                val appDbEvent = AppDbEvent(DbHelperEvent(application).writableDatabase)

                INSTANCE = appDbEvent

                return appDbEvent
            }
        }
    }
}
