package com.eltex.androidschool.api.jobs

import com.eltex.androidschool.data.jobs.JobData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Интерфейс, определяющий API для работы с вакансиями.
 * Используется Retrofit для выполнения сетевых запросов.
 */
interface JobsApi {

    /**
     * Получает список вакансий для конкретного пользователя по его ID.
     *
     * @param userId ID пользователя, для которого нужно получить вакансии.
     * @return Список вакансий (JobData).
     * @see JobData
     */
    @GET("api/{userId}/jobs")
    suspend fun getJobsUserById(@Path("userId") userId: Long): List<JobData>

    /**
     * Получает список вакансий на стене текущего пользователя.
     *
     * @return Список вакансий (JobData).
     * @see JobData
     */
    @GET("api/my/jobs")
    suspend fun getJobsWall(): List<JobData>

    /**
     * Сохраняет вакансию на стене текущего пользователя.
     *
     * @param job Данные вакансии, которые нужно сохранить.
     * @return Сохраненная вакансия (JobData).
     * @see JobData
     */
    @POST("api/my/jobs")
    suspend fun saveJobWall(@Body job: JobData): JobData

    /**
     * Удаляет вакансию с стены текущего пользователя по ID вакансии.
     *
     * @param jobId ID вакансии, которую нужно удалить.
     */
    @DELETE("api/my/jobs/{id}")
    suspend fun deleteJobWall(@Path("id") jobId: Long)
}
