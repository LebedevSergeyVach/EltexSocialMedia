package com.eltex.androidschool.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс [EventDataParcelable], реализующий интерфейс [Parcelable] с использованием аннотации [Parcelize].
 * Используется для передачи данных о событии через [Intent].
 *
 * @property content Содержание события.
 * @property date Дата события.
 * @property option Вариант проведения события.
 * @property link Ссылка на событие.
 * @property eventId Уникальный идентификатор события.
 *
 * @sample EventDataParcelable
 */
@Parcelize
data class EventDataParcelable(
    val content: String,
    val date: String,
    val option: String,
    val link: String,
    val eventId: Long
) : Parcelable
