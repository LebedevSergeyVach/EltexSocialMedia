package com.eltex.androidschool.data.update

/**
 * Модель данных, представляющая информацию об обновлении приложения.
 *
 * @property name Название обновления.
 * @property version Версия обновления.
 * @property date Дата выпуска обновления.
 * @property description Описание обновления.
 * @property link Ссылка на дополнительную информацию (например, на GitHub).
 */
data class UpdateData(
    val id: Int = 0,
    val name: String = "",
    val version: String = "",
    val date: String = "",
    val description: String = "",
    val link: String = "",
)
