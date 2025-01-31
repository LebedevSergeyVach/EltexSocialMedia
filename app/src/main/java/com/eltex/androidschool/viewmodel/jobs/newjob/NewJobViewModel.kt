package com.eltex.androidschool.viewmodel.jobs.newjob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.data.jobs.JobData
import com.eltex.androidschool.repository.jobs.JobRepository
import com.eltex.androidschool.viewmodel.status.StatusLoad

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для создания новой вакансии.
 * Отвечает за сохранение новой вакансии и управление состоянием экрана создания вакансии.
 *
 * @param repository Репозиторий для работы с вакансиями.
 * @param jobId ID вакансии (по умолчанию 0L, если вакансия новая).
 */
class NewJobViewModel(
    private val repository: JobRepository,
    private val jobId: Long = 0L,
) : ViewModel() {

    /**
     * Внутреннее состояние ViewModel, хранящее данные о вакансии и статусе загрузки.
     */
    private val _state = MutableStateFlow(NewJobState())

    /**
     * Публичное состояние ViewModel, доступное для наблюдения из UI.
     */
    val state = _state.asStateFlow()

    /**
     * Сохраняет новую вакансию на сервере.
     *
     * @param name Название вакансии.
     * @param position Должность.
     * @param link Ссылка на вакансию.
     * @param start Дата начала работы.
     * @param finish Дата окончания работы.
     */
    fun save(name: String, position: String, link: String, start: String, finish: String) {
        _state.update { stateJobs: NewJobState ->
            stateJobs.copy(
                statusJob = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val job: JobData = repository.saveJobWall(
                    jobId = jobId,
                    name = name,
                    position = position,
                    start = start,
                    finish = finish,
                    link = link
                )

                _state.update { stateJobs: NewJobState ->
                    stateJobs.copy(
                        statusJob = StatusLoad.Idle,
                        job = job
                    )
                }
            } catch (e: Exception) {
                _state.update { stateJobs: NewJobState ->
                    stateJobs.copy(
                        statusJob = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Сбрасывает состояние ошибки, возвращая статус загрузки в Idle.
     */
    fun consumerError() {
        _state.update { stateJobs: NewJobState ->
            stateJobs.copy(
                statusJob = StatusLoad.Idle
            )
        }
    }

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с корутинами.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}
