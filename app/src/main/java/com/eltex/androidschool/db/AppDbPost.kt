package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

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

                val dbFile = application.getDatabasePath(PostTableInfo.DB_NAME)

                // Проверка, существует ли база данных и пуста ли она
                if (!dbFile.exists() || isDatabaseEmpty(dbFile)) {
                    // Копирование базы данных из ассетов
                    copyDatabaseFromAssets(application, dbFile)
                }

                val appDbPost: AppDbPost =
                    Room.databaseBuilder(
                        context = application,
                        klass = AppDbPost::class.java,
                        name = PostTableInfo.DB_NAME
                    )
                        .fallbackToDestructiveMigration() // разрешаем стереть данные при увеличении версии БД
                        .allowMainThreadQueries() // разрешаем запросы с главного потока
                        .build()

                INSTANCE = appDbPost

                return appDbPost
            }
        }

        /**
         * Проверяет, пуста ли база данных.
         *
         * @param dbFile Файл базы данных.
         * @return true, если база данных пуста, иначе false.
         */
        private fun isDatabaseEmpty(dbFile: File): Boolean {
            val db =
                SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${PostTableInfo.TABLE_NAME}", null)
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()
            db.close()

            return count == 0
        }

        /**
         * Копирует базу данных из ассетов в папку приложения.
         *
         * @param context Контекст приложения.
         * @param dbFile Файл базы данных.
         */
        private fun copyDatabaseFromAssets(context: Context, dbFile: File) {
            try {
                context.assets.open("post_prepopulated_db.db").use { inputStream: InputStream ->
                    FileOutputStream(dbFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException("Error copying database", e)
            }
        }
    }
}
