@file:Suppress("DEPRECATION")

package com.eltex.androidschool.utils

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.eltex.androidschool.R

/**
 * Создаёт и показывает кастомный Toast
 *
 * @param res – ссылка на строковый ресурс, который показываем
 * @param short - если true, то Toast.LENGTH_SHORT, иначе Toast.LENGTH_LONG
 */
@SuppressLint("InflateParams")
fun Context.toast(
    @StringRes res: Int,
    short: Boolean = true
) {
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.custom_toast, null)

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = getString(res)

    val toast = Toast(this)
    toast.duration = if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}

/**
 * Перегружнный метод `fun Context.toast(@StringRes res: Int, short: Boolean = true)`
 * Создаёт и показывает кастомный Toast
 *
 * @param res – Строковый ресурс, который показываем
 * @param short - если true, то Toast.LENGTH_SHORT, иначе Toast.LENGTH_LONG
 */
@SuppressLint("InflateParams")
fun Context.toast(
    res: String,
    short: Boolean = true
) {
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.custom_toast, null)

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = res

    val toast = Toast(this)
    toast.duration = if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}
