package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.utils.ErrorUtils.getErrorText

/**
 * ViewHolder для отображения состояния ошибки в RecyclerView.
 * Используется для показа сообщения об ошибке, когда загрузка данных не удалась.
 *
 * @property binding Привязка к макету `ItemErrorBinding`, который содержит UI для отображения ошибки.
 */
class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Привязывает данные об ошибке к ViewHolder.
     *
     * @param error Исключение (Throwable), содержащее информацию об ошибке.
     * @see getErrorText Утилита для получения текста ошибки в зависимости от контекста.
     */
    fun bind(error: Throwable) {
        binding.errorTextItem.text = error.getErrorText(binding.root.context)
    }
}
