package com.eltex.androidschool.repository.jobs

import com.eltex.androidschool.data.jobs.JobData

/**
 * Интерфейс репозитория для работы с вакансиями.
 * Определяет методы для получения, сохранения и удаления вакансий.
 */
interface JobRepository {

    /**
     * Получает список вакансий для конкретного пользователя по его ID.
     *
     * @param userId ID пользователя, для которого нужно получить вакансии.
     * @return Список вакансий (JobData).
     * @see JobData
     */
    suspend fun getJobsUserById(userId: Long): List<JobData>

    /**
     * Получает список вакансий на стене текущего пользователя.
     *
     * @return Список вакансий (JobData).
     * @see JobData
     */
    suspend fun getJobsWall(): List<JobData>

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
     * @see JobData
     */
    suspend fun saveJobWall(
        jobId: Long,
        name: String,
        position: String,
        start: String,
        finish: String,
        link: String
    ): JobData

    /**
     * Удаляет вакансию с стены текущего пользователя по ID вакансии.
     *
     * @param jobId ID вакансии, которую нужно удалить.
     */
    suspend fun deleteJobWall(jobId: Long)
}
