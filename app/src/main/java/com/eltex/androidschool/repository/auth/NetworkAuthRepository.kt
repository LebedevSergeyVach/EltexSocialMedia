package com.eltex.androidschool.repository.auth

import android.content.ContentResolver

import com.eltex.androidschool.api.auth.AuthApi
import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.store.UserPreferences
import com.eltex.androidschool.data.media.FileModel

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import okio.Buffer
import okio.BufferedSink
import okio.IOException

import javax.inject.Inject

/**
 * Реализация репозитория для работы с аутентификацией и регистрацией.
 * Использует [AuthApi] для выполнения сетевых запросов и [UserPreferences] для сохранения данных пользователя.
 *
 * @property authApi API для работы с аутентификацией и регистрацией.
 * @property userPreferences Хранилище для сохранения данных пользователя (токен и идентификатор).
 */
class NetworkAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences,
) : AuthRepository {

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @throws IOException Если произошла ошибка сети.
     * @see AuthData
     */
    override suspend fun login(login: String, password: String): AuthData {
        val response: AuthData = authApi.login(
            login = login,
            password = password,
        )

        if (response.token.isNotEmpty()) {
            userPreferences.saveUserCredentials(
                authToken = response.token,
                userId = response.id.toString()
            )
        }

        return response
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param login Логин пользователя.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param fileModel Модель файла аватара пользователя. Может быть `null`, если файл не загружен.
     * @param contentResolver [ContentResolver] для работы с файлом.
     * @param onProgress Колбэк для отслеживания прогресса загрузки файла.
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @throws IOException Если файл не найден или произошла ошибка сети.
     * @see AuthData
     * @see FileModel
     */
    override suspend fun register(
        login: String,
        username: String,
        password: String,
        fileModel: FileModel?,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit,
    ): AuthData {
        val response: AuthData = if (fileModel != null) {
            val fileBody = contentResolver.openInputStream(fileModel.uri)?.use { inputStream ->
                val fileBytes = inputStream.readBytes()
                fileBytes.toRequestBody()
            } ?: throw IOException("File not found")

            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? = fileBody.contentType()

                override fun writeTo(sink: BufferedSink) {
                    val totalBytes = fileBody.contentLength()
                    val buffer = Buffer()
                    var bytesWritten = 0L

                    fileBody.writeTo(buffer)

                    while (!buffer.exhausted()) {
                        val bytesRead = buffer.read(sink.buffer, 16364)
                        bytesWritten += bytesRead
                        val progress = ((bytesWritten.toFloat() / totalBytes) * 100).toInt()

                        onProgress(progress)
                    }
                }

                override fun contentLength(): Long = fileBody.contentLength()
            }

            val part = MultipartBody.Part.createFormData("file", "file", requestBody)

            authApi.register(
                login = login,
                password = password,
                name = username,
                file = part,
            )
        } else {
            authApi.register(
                login = login,
                password = password,
                name = username,
                file = MultipartBody.Part.createFormData("file", "file"),
            )
        }

        if (response.token.isNotEmpty()) {
            userPreferences.saveUserCredentials(
                authToken = response.token,
                userId = response.id.toString()
            )
        }

        return response
    }

    override fun getProhibitedUsernamesAndNicknames(): List<String> =
        listOf(
            // Основные запрещенные слова
            "admin", "админ", "administrator", "администратор", "moderator", "модератор",
            "root", "суперпользователь", "superuser", "sysadmin", "сисадмин",

            // Транслитерация и альтернативные написания
            "аdmin", "адmин", "aдмин", "адmиn", "адм1н", "аdмin",
            "adm1n", "4dm1n", "@dm1n", "4dmin", "@dmin",
            "адмiн", "aдмiн", "адм!н", "адм*н", "адм_н",

            // Разделенные символы
            "a d m i n", "а д м и н", "a.d.m.i.n", "а-д-м-и-н",
            "a_d_m_i_n", "а_д_м_и_н", "a*d*m*i*n", "а*д*м*и*н",

            // Добавление цифр
            "admin1", "admin123", "admin007", "admin777", "admin2023",
            "админ1", "админ123", "админ007", "админ777", "админ2023",

            // Замена букв на похожие символы
            "@dmin", "@дмин", "4dmin", "4дмин", "adm!n", "адм!н",
            "adm1n", "адм1н", "admiи", "aдmin", "аdмин", "aдмiн",

            // Обратные написания
            "nimda", "нимда", "nimd4", "нимд4",

            // Комбинации с другими словами
            "adminaccount", "adminuser", "adminlogin", "adminsystem",
            "админаккаунт", "админюзер", "админлогин", "админсистема",

            // Специальные символы
            "admin!", "@admin", "#admin", "\$admin", "%admin", "^admin", "&admin", "*admin",
            "админ!", "@админ", "#админ", "\$админ", "%админ", "^админ", "&админ", "*админ",

            // Смешанные языки
            "aдmиn", "адm1n", "aдm1n", "адm!n",

            // Укороченные варианты
            "adm", "admn", "admi", "адм", "адмн", "адми",

            // Дублирование букв
            "aadmin", "aддмин", "admmiin", "адммиин",

            // Случайные комбинации
            "xadminx", "xxadminxx", "zzadm1nzz", "админxxx", "xxxадмин",

            // Эмодзи и символы
            "🅰️admin", "admin👑", "админ💻", "🛑admin🛑", "👑админ👑",

            // Unicode-обманки
            "а\u0434мин", "a\u0064min", "\u0430dmin", "a\u0250min",

            // Редкие вариации
            "4dmiи", "@дmiи", "aдm!и", "aдm!ии", "aдm!иии"
        )
}
