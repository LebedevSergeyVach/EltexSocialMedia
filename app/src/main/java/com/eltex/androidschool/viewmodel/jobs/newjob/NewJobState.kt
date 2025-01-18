package com.eltex.androidschool.viewmodel.jobs.newjob

import com.eltex.androidschool.data.jobs.JobData
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние экрана создания новой вакансии.
 *
 * @property job Данные вакансии, если она была успешно сохранена.
 * @property statusJob Статус загрузки (Idle, Loading, Error).
 */
data class NewJobState (
    val job: JobData? = null,
    val statusJob: StatusLoad = StatusLoad.Idle,
)
