package com.eltex.androidschool.utils.common

/**
 * Интерфейс обратного вызова для обработки результатов асинхронных операций.
 *
 * @param T Тип данных, которые будут возвращены в случае успеха.
 */
interface Callback<T> {
    /**
     * Вызывается при успешном завершении операции.
     *
     * @param data Данные, возвращенные в результате операции.
     */
    fun onSuccess(data: T)

    /**
     * Вызывается при возникновении ошибки во время операции.
     *
     * @param exception Исключение, возникшее во время операции.
     */
    fun onError(exception: Throwable)
}
