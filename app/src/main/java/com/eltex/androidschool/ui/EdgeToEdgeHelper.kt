package com.eltex.androidschool.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object EdgeToEdgeHelper {
    /**
     * Применяет отступы для системных панелей (навигации, статуса).
     *
     * @see ViewCompat.setOnApplyWindowInsetsListener Устанавливает слушатель для применения отступов.
     */
    fun applyingIndentationOfSystemFields(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
