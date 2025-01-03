package com.eltex.androidschool.utils

import android.content.Context
import android.graphics.Typeface
import com.eltex.androidschool.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Показывает Material 3 информационный диалог с заголовком, текстом, иконкой и кнопкой для закрытия.
 *
 * @param title Заголовок диалога.
 * @param message Основной текст диалога.
 * @param buttonText Текст кнопки (по умолчанию "Спасибо").
 * @param iconRes Иконка диалога (по умолчанию иконка приложения).
 * @param titleTextSize Размер текста заголовка (по умолчанию 26sp).
 * @param messageTextSize Размер текста сообщения (по умолчанию 16sp).
 * @param buttonTextSize Размер текста кнопки (по умолчанию 16sp).
 * @param isTitleBold Делает заголовок жирным (по умолчанию true).
 */
fun Context.showMaterialDialog(
    title: String,
    message: String,
    buttonText: String = getString(R.string.thanks),
    iconRes: Int = R.mipmap.eltex_logo,
    titleTextSize: Float = 26f,
    messageTextSize: Float = 16f,
    buttonTextSize: Float = 16f,
    isTitleBold: Boolean = true,
) {
    val dialog = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setIcon(iconRes)
        .setPositiveButton(buttonText) { dialog, _ ->
            dialog.dismiss()
        }
        .create()

    dialog.show()

    dialog.findViewById<android.widget.TextView>(android.R.id.title)?.let { titleView ->
        titleView.textSize = titleTextSize
        if (isTitleBold) {
            titleView.setTypeface(null, Typeface.BOLD)
        }
    }

    dialog.findViewById<android.widget.TextView>(android.R.id.message)?.let { messageView ->
        messageView.textSize = messageTextSize
    }

    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)?.let { button ->
        button.setTextColor(getColor(R.color.active_element))
        button.textSize = buttonTextSize
    }
}