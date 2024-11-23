package com.eltex.androidschool.utils

import android.database.Cursor

/**
 * Возвращает значение типа Long для указанного столбца.
 *
 * @param columnName Имя столбца.
 * @return Значение типа Long.
 * @throws IllegalArgumentException Если столбец не найден.
 */
fun Cursor.getLongOrThrow(columnName: String): Long =
    getLong(getColumnIndexOrThrow(columnName))

/**
 * Возвращает значение типа String для указанного столбца.
 *
 * @param columnName Имя столбца.
 * @return Значение типа String.
 * @throws IllegalArgumentException Если столбец не найден.
 */
fun Cursor.getStringOrThrow(columnName: String): String =
    getString(getColumnIndexOrThrow(columnName))

/**
 * Возвращает значение типа Int для указанного столбца.
 *
 * @param columnName Имя столбца.
 * @return Значение типа Int.
 * @throws IllegalArgumentException Если столбец не найден.
 */
fun Cursor.getIntOrThrow(columnName: String): Int =
    getInt(getColumnIndexOrThrow(columnName))

/**
 * Возвращает значение типа Boolean для указанного столбца.
 *
 * @param columnName Имя столбца.
 * @return Значение типа Boolean.
 * @throws IllegalArgumentException Если столбец не найден.
 */
fun Cursor.getBooleanOrThrow(columnName: String): Boolean =
    getIntOrThrow(columnName) != 0
