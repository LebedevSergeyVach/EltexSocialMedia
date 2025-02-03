package com.eltex.androidschool.repository.jobs

import com.eltex.androidschool.api.jobs.JobsApi
import com.eltex.androidschool.data.jobs.JobData

import java.time.Instant

import javax.inject.Inject

/**
 * Реализация интерфейса JobRepository, использующая сетевой API для работы с вакансиями.
 */
class NetworkJobRepository @Inject constructor(
    private val jobsApi: JobsApi,
) : JobRepository {

    /**
     * Получает список вакансий для конкретного пользователя по его ID.
     *
     * @param userId ID пользователя, для которого нужно получить вакансии.
     * @return Список вакансий (JobData).
     * @see JobsApi.getJobsUserById
     */
    override suspend fun getJobsUserById(userId: Long): List<JobData> =
        jobsApi.getJobsUserById(userId = userId)

    /**
     * Получает список вакансий на стене текущего пользователя.
     *
     * @return Список вакансий (JobData).
     * @see JobsApi.getJobsWall
     */
    override suspend fun getJobsWall(): List<JobData> =
        jobsApi.getJobsWall()

    /**
     * Сохраняет вакансию на стене текущего пользователя.
     *
     * @param jobId ID вакансии.
     * @param name Название вакансии.
     * @param position Должность.
     * @param start Дата начала работы.
     * @param finish Дата окончания работы.
     * @param link Ссылка на вакансию.
     * @return Сохраненная вакансия (JobData).
     * @see JobsApi.saveJobWall
     */
    override suspend fun saveJobWall(
        jobId: Long,
        name: String,
        position: String,
        start: String,
        finish: String,
        link: String
    ) = jobsApi.saveJobWall(
        job = JobData(
            id = jobId,
            name = name,
            position = position,
            start = Instant.parse(start),
            finish = Instant.parse(finish),
            link = link,
        )
    )

    /**
     * Удаляет вакансию с стены текущего пользователя по ID вакансии.
     *
     * @param jobId ID вакансии, которую нужно удалить.
     * @see JobsApi.deleteJobWall
     */
    override suspend fun deleteJobWall(jobId: Long) =
        jobsApi.deleteJobWall(jobId = jobId)
}
