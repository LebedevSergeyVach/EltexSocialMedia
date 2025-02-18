package com.eltex.androidschool.utils

import android.content.Context

import com.eltex.androidschool.R

import okio.IOException
import retrofit2.HttpException

/**
 * Расширение для класса Throwable, которое возвращает текстовое описание ошибки при Авторизации.
 *
 * @param context Контекст приложения.
 * @return Текстовое описание ошибки.
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
 * Расширение для класса Throwable, которое возвращает текстовое описание ошибки при Регистрации.
 *
 * @param context Контекст приложения.
 * @return Текстовое описание ошибки.
 */
fun Throwable.getErrorTextRegistration(context: Context): CharSequence =
    when(this) {
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
