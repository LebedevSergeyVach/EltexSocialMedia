package com.eltex.androidschool.db

import android.content.Context

import android.database.sqlite.SQLiteDatabase

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.entity.EventEntity

/**
 * Класс для управления базой данных событий приложения.
 *
 * @property eventDao DAO для работы с сущностями [EventEntity].
 */
@Database(
    entities = [EventEntity::class],
    version = EventTableInfo.DB_VERSION,
)
abstract class AppDbEvent : RoomDatabase() {
    abstract val eventDao: EventDao

    companion object {
        /**
         * Единственный экземпляр класса [AppDbEvent].
         */
        @Volatile
        private var INSTANCE: AppDbEvent? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE ${EventTableInfo.TABLE_NAME} ADD COLUMN newProperty TEXT")
            }
        }

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

                val dbFile = application.getDatabasePath(EventTableInfo.DB_NAME)

                // Проверка, существует ли база данных и пуста ли она
                if (!dbFile.exists() || isDatabaseEmpty(dbFile)) {
                    // Копирование базы данных из ассетов
                    copyDatabaseFromAssets(application, dbFile)
                }

                val appDbEvent: AppDbEvent =
                    Room.databaseBuilder(
                        context = application,
                        klass = AppDbEvent::class.java,
                        name = EventTableInfo.DB_NAME
                    )
                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration() // разрешаем стереть данные при увеличении версии БД
                        .allowMainThreadQueries() // разрешаем запросы с главного потока
                        .build()

                INSTANCE = appDbEvent

                return appDbEvent
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
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${EventTableInfo.TABLE_NAME}", null)
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
                context.assets.open("event_prepopulated_db.db").use { inputStream: InputStream ->
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
