package com.eltex.androidschool.utils

import android.content.Context

import com.eltex.androidschool.R

import okio.IOException
import retrofit2.HttpException

/**
 * Расширение для класса Throwable, которое возвращает текстовое описание ошибки.
 *
 * @param context Контекст приложения.
 * @return Текстовое описание ошибки.
 */
fun Throwable.getErrorTextAuth(context: Context): CharSequence =
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


