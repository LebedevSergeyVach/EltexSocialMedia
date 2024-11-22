package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase

import com.eltex.androidschool.dao.EventDaoImpl

class AppDbEvent private constructor(
    db: SQLiteDatabase
) {
    val eventDao = EventDaoImpl(db)

    companion object {
        @Volatile
        private var INSTANCE: AppDbEvent? = null

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
