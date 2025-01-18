package com.eltex.androidschool.viewmodel.jobs.jobswall

import com.eltex.androidschool.ui.jobs.JobUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние для управления данными о местах работы.
 *
 * Этот класс содержит данные о местах работы и статус их загрузки.
 *
 * @property jobs Список мест работы.
 * @property statusJobs Статус загрузки данных.
 * @see StatusLoad
 */
data class JobState(
    val jobs: List<JobUiModel>? = null,
    val statusJobs: StatusLoad = StatusLoad.Idle,
) {

    /**
     * Возвращает true, если данные загружаются и список не пуст.
     */
    val isRefreshing: Boolean
        get() = statusJobs == StatusLoad.Loading && jobs?.isNotEmpty() == true

    /**
     * Возвращает true, если данные загружаются и список пуст.
     */
    val isEmptyLoading: Boolean
        get() = statusJobs == StatusLoad.Loading && jobs.isNullOrEmpty()

    /**
     * Возвращает true, если произошла ошибка и список не пуст.
     */
    val isRefreshError: Boolean
        get() = statusJobs is StatusLoad.Error && jobs?.isNotEmpty() == true

    /**
     * Возвращает true, если произошла ошибка и список пуст.
     */
    val isEmptyError: Boolean
        get() = statusJobs is StatusLoad.Error && jobs.isNullOrEmpty()
}
