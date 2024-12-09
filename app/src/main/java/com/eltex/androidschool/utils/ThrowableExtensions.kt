package com.eltex.androidschool.utils

import android.content.Context
import com.eltex.androidschool.R
import okio.IOException

/**
 * Расширение для класса Throwable, которое возвращает текстовое описание ошибки.
 *
 * @param context Контекст приложения.
 * @return Текстовое описание ошибки.
 */
fun Throwable.getErrorText(context: Context): CharSequence = when (this) {
    is IOException -> context.getString(R.string.network_error)
    else -> context.getString(R.string.unknown_error)
}
