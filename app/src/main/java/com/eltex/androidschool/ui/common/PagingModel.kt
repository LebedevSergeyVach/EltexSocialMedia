package com.eltex.androidschool.ui.common

/**
 * Запечатанный интерфейс, представляющий модель пагинации.
 * Используется для отображения данных, ошибок и состояния загрузки в RecyclerView.
 *
 * @property T Тип данных, которые загружаются.
 */
sealed interface PagingModel<out T> {

    /**
     * Модель данных, содержащая загруженные данные.
     *
     * @property value Загруженные данные.
     */
    data class Data<T>(val value: T) : PagingModel<T>

    /**
     * Модель ошибки, содержащая информацию об ошибке.
     *
     * @property reason Исключение, вызвавшее ошибку.
     */
    data class Error(val reason: Throwable) : PagingModel<Nothing>

    /**
     * Модель состояния загрузки.
     */
    data object Loading : PagingModel<Nothing>

    /**
     * Модель разделителя даты.
     *
     * @property date Дата, которая будет отображаться в разделителе.
     */
    data class DateSeparator(val date: String) : PagingModel<Nothing>
}
