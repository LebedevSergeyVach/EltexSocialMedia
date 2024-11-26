package com.eltex.androidschool.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.eltex.androidschool.dao.PostDao
import com.eltex.androidschool.entity.PostEntity

/**
 * Класс для управления базой данных приложения.
 *
 * @property postDao DAO для работы с сущностями [PostEntity].
 */
@Database(
    entities = [PostEntity::class],
    version = PostTableInfo.DB_VERSION,
)
abstract class AppDbPost : RoomDatabase() {
    abstract val postDao: PostDao

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

                val appDbPost: AppDbPost =
                    Room.databaseBuilder(application, AppDbPost::class.java, PostTableInfo.DB_NAME)
                        .fallbackToDestructiveMigration() // разрешаем стереть данные при увеличении версии БД
                        .allowMainThreadQueries() // разрешаем запросы с главного потока
                        .build()

                INSTANCE = appDbPost

                return appDbPost
            }
        }
    }
}