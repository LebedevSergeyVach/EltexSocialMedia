package com.eltex.androidschool.utils

import android.content.Context
import com.eltex.androidschool.R
import okio.IOException
import retrofit2.HttpException

/**
 * Утилитарные функции для обработки ошибок и получения пользовательских сообщений об ошибках.
 * Эти функции позволяют преобразовывать исключения в читаемые сообщения, которые можно отображать пользователю.
 */
object ErrorUtils {

    /**
     * Возвращает текстовое сообщение об ошибке на основе переданного исключения.
     * Используется для обработки общих ошибок, таких как проблемы с сетью или неизвестные ошибки.
     *
     * @param context Контекст приложения, необходимый для доступа к строковым ресурсам.
     * @return Сообщение об ошибке в виде [CharSequence]. Возвращает строку "Сетевая ошибка" для [IOException]
     *         и "Неизвестная ошибка" для всех остальных исключений.
     *
     * @see IOException
     */
    fun Throwable.getErrorText(context: Context): CharSequence = when (this) {
        is IOException -> context.getString(R.string.network_error)
        else -> context.getString(R.string.unknown_error)
    }

    /**
     * Возвращает текстовое сообщение об ошибке, связанной с авторизацией.
     * Используется для обработки ошибок, возникающих при попытке входа в систему.
     *
     * @param context Контекст приложения, необходимый для доступа к строковым ресурсам.
     * @return Сообщение об ошибке в виде [CharSequence]. Возвращает:
     *         - "Сетевая ошибка" для [IOException].
     *         - "Неправильный пароль" для HTTP-ошибки 400.
     *         - "Пользователь не зарегистрирован" для HTTP-ошибки 404.
     *         - "Неизвестная ошибка" для всех остальных случаев.
     *
     * @see IOException
     * @see HttpException
     */
    fun Throwable.getErrorTextAuthorization(context: Context): CharSequence =
        when (this) {
            is IOException -> context.getString(R.string.network_error)
            is HttpException -> {
                when (code()) {
                    400 -> context.getString(R.string.incorrect_password)
                    404 -> context.getString(R.string.user_not_registered)
                    else -> context.getString(R.string.unknown_error)
                }
            }
            else -> context.getString(R.string.unknown_error)
        }

    /**
     * Возвращает текстовое сообщение об ошибке, связанной с регистрацией.
     * Используется для обработки ошибок, возникающих при попытке регистрации нового пользователя.
     *
     * @param context Контекст приложения, необходимый для доступа к строковым ресурсам.
     * @return Сообщение об ошибке в виде [CharSequence]. Возвращает:
     *         - "Сетевая ошибка" для [IOException].
     *         - "Пользователь уже зарегистрирован" для HTTP-ошибки 403.
     *         - "Неправильный формат фотографии" для HTTP-ошибки 415.
     *         - "Неизвестная ошибка" для всех остальных случаев.
     *
     * @see IOException
     * @see HttpException
     * ```
     */
    fun Throwable.getErrorTextRegistration(context: Context): CharSequence =
        when (this) {
            is IOException -> context.getString(R.string.network_error)
            is HttpException -> {
                when (code()) {
                    403 -> context.getString(R.string.the_user_is_already_registered)
                    415 -> context.getString(R.string.incorrect_photo_format)
                    else -> context.getString(R.string.unknown_error)
                }
            }
            else -> context.getString(R.string.unknown_error)
        }
}
