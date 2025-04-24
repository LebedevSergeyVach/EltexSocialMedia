package com.eltex.androidschool.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

import android.graphics.Color

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.FrameLayout

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

import androidx.core.content.ContextCompat

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.SnackbarCustomInfoBinding
import com.eltex.androidschool.utils.helper.LoggerHelper

import com.google.android.material.snackbar.Snackbar

fun View.showTopSnackbar(
    message: String,
    short: Boolean = true,
    @DrawableRes iconRes: Int? = null,
    @ColorRes iconTintRes: Int = R.color.active_element
) {
    val snackbar =
        Snackbar.make(this, "", if (short) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG)

    // Настраиваем стандартный view Snackbar
    snackbar.view.apply {
        setBackgroundColor(Color.TRANSPARENT)

        (layoutParams as? FrameLayout.LayoutParams)?.apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            topMargin = 32.dpToPx(context)
        }
    }

    // Инициализируем кастомный layout
    val binding = SnackbarCustomInfoBinding.inflate(LayoutInflater.from(context))

    // Настраиваем контент
    with(binding) {
        this.message.text = message

        iconRes?.let { resId: Int ->
            icon.visibility = View.VISIBLE
            icon.setImageResource(resId)
            icon.setColorFilter(ContextCompat.getColor(context, iconTintRes))
        } ?: run {
            icon.visibility = View.GONE
        }

        // Удаляем стандартный view и добавляем наш
        (snackbar.view as? ViewGroup)?.apply {
            removeAllViews()
            addView(root)
        }
    }

    snackbar.show()
}

fun Context.showTopSnackbar(
    message: String,
    short: Boolean = true,
    @DrawableRes iconRes: Int? = null,
    @ColorRes iconTintRes: Int = R.color.active_element,
    top: Boolean = false
) {
    // Получаем root View из Activity
    val rootView = when (this) {
        is Activity -> findViewById<ViewGroup>(android.R.id.content)
        is ContextWrapper -> (baseContext as? Activity)?.findViewById<ViewGroup>(android.R.id.content)
        else -> null
    } ?: run {
        LoggerHelper.e("Snackbar: Could not find root view from context")
        return
    }

    val snackbar =
        Snackbar.make(rootView, "", if (short) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG)

    // Настраиваем стандартный view Snackbar
    snackbar.view.apply {
        setBackgroundColor(Color.TRANSPARENT)
        (layoutParams as? FrameLayout.LayoutParams)?.apply {
            gravity = if (top) {
                Gravity.TOP or Gravity.CENTER_HORIZONTAL
            } else {
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            }
            bottomMargin = 32.dpToPx(this@showTopSnackbar)// Добавляем отступ снизу
        }
    }

    // Инициализируем кастомный layout
    val binding = SnackbarCustomInfoBinding.inflate(LayoutInflater.from(this))

    // Настраиваем контент
    with(binding) {
        this.message.text = message

        iconRes?.let { resId ->
            icon.visibility = View.VISIBLE
            icon.setImageResource(resId)
            icon.setColorFilter(ContextCompat.getColor(this@showTopSnackbar, iconTintRes))
        } ?: run {
            icon.visibility = View.GONE
        }

        // Удаляем стандартный view и добавляем наш
        (snackbar.view as? ViewGroup)?.apply {
            removeAllViews()
            addView(root)
        }
    }

    snackbar.show()
}

// Вспомогательное расширение для dpToPx
fun Int.dpToPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).toInt()

// Вспомогательное расширение для получения цвета
fun Context.getColorCompat(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)
