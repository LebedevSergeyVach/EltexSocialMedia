package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase

import com.eltex.androidschool.dao.PostDaoImpl

/**
 * Класс для управления базой данных приложения.
 *
 * @property db База данных SQLite.
 */
class AppDbPost private constructor(
    db: SQLiteDatabase,
) {
    /**
     * DAO для работы с данными постов.
     */
    val postDao = PostDaoImpl(db)

    companion object {
        /**
         * Единственный экземпляр класса [AppDbPost].
         */
        @Volatile
        private var INSTANCE: AppDbPost? = null

        /**
         * Получает экземпляр класса [AppDbPost].
         *
         * @param context Контекст приложения.
         * @return Экземпляр класса [AppDbPost].
         */
        fun getInstance(context: Context): AppDbPost {
            val application = context.applicationContext

            INSTANCE?.let { appDbPost: AppDbPost ->
                return appDbPost
            }

            synchronized(this) {
                INSTANCE?.let { appDbPost: AppDbPost ->
                    return appDbPost
                }

                val appDbPost = AppDbPost(DbHelperPost(application).writableDatabase)

                INSTANCE = appDbPost

                return appDbPost
            }
        }
    }
}