package com.eltex.androidschool.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Объект-помощник для применения отступов для системных панелей в режиме "от края до края" (Edge-to-Edge).
 *
 * @see ViewCompat Класс для совместимости с различными версиями Android.
 * @see WindowInsetsCompat Класс для работы с вставками окна.
 */
object EdgeToEdgeHelper {
    /**
     * Применяет отступы для системных панелей к указанному представлению.
     *
     * Этот метод устанавливает слушатель на вставки окна, чтобы динамически изменять отступы представления
     * в зависимости от размеров системных панелей.
     *
     * @param view Представление, к которому будут применены отступы.
     */
    fun applyingIndentationOfSystemFields(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}
