package com.eltex.androidschool.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.entity.EventEntity


@Database(
    entities = [EventEntity::class],
    version = EventTableInfo.DB_VERSION,
)
abstract class AppDbEvent : RoomDatabase() {
    abstract val eventDao: EventDao

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

                val appDbEvent: AppDbEvent =
                    Room.databaseBuilder(
                        context = application,
                        klass = AppDbEvent::class.java,
                        name = EventTableInfo.DB_NAME
                    )
                        .fallbackToDestructiveMigration() // разрешаем стереть данные при увеличении версии БД
                        .allowMainThreadQueries() // разрешаем запросы с главного потока
                        .build()

                INSTANCE = appDbEvent

                return appDbEvent
            }
        }
    }
}
