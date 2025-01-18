package com.eltex.androidschool.repository.jobs

import com.eltex.androidschool.api.jobs.JobsApi
import com.eltex.androidschool.data.jobs.JobData
import java.time.Instant

/**
 * Реализация интерфейса JobRepository, использующая сетевой API для работы с вакансиями.
 */
class NetworkJobRepository : JobRepository {

    /**
     * Получает список вакансий для конкретного пользователя по его ID.
     *
     * @param userId ID пользователя, для которого нужно получить вакансии.
     * @return Список вакансий (JobData).
     * @see JobsApi.getJobsUserById
     */
    override suspend fun getJobsUserById(userId: Long): List<JobData> =
        JobsApi.INSTANCE.getJobsUserById(userId = userId)

    /**
     * Получает список вакансий на стене текущего пользователя.
     *
     * @return Список вакансий (JobData).
     * @see JobsApi.getJobsWall
     */
    override suspend fun getJobsWall(): List<JobData> =
        JobsApi.INSTANCE.getJobsWall()

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
    ) = JobsApi.INSTANCE.saveJobWall(
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
        JobsApi.INSTANCE.deleteJobWall(jobId = jobId)
}
