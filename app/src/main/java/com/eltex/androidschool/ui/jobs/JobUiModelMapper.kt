package com.eltex.androidschool.ui.jobs

import com.eltex.androidschool.data.jobs.JobData

import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Маппер для преобразования данных о местах работы из `JobData` в `JobUiModel`.
 *
 * Этот класс используется для преобразования данных, полученных из сети, в модель, удобную для отображения в UI.
 *
 * @see JobData
 * @see JobUiModel
 */
class JobUiModelMapper {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    }

    /**
     * Преобразует объект `JobData` в `JobUiModel`.
     *
     * @param job Данные о месте работы.
     * @return Преобразованный объект `JobUiModel`.
     */
    fun map(job: JobData): JobUiModel = with(job)
    {
        JobUiModel(
            id = id,
            name = name,
            position = position,
            start = FORMATTER.format(start.atZone(ZoneId.systemDefault())),
            finish = FORMATTER.format(finish.atZone(ZoneId.systemDefault())),
            link = link
        )
    }
}
