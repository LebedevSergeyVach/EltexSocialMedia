package com.eltex.androidschool.ui.jobs

/**
 * Модель данных для отображения информации о месте работы.
 *
 * @property id Идентификатор места работы.
 * @property name Название места работы.
 * @property position Должность.
 * @property start Дата начала работы.
 * @property finish Дата окончания работы.
 * @property link Ссылка на дополнительную информацию.
 */
data class JobUiModel(
    val id: Long = 0L,
    val name: String = "",
    val position: String = "",
    val start: String = "",
    val finish: String = "",
    val link: String = "",
)
