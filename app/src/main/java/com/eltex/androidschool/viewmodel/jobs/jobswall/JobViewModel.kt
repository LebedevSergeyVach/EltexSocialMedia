package com.eltex.androidschool.viewmodel.jobs.jobswall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.data.jobs.JobData
import com.eltex.androidschool.repository.jobs.JobRepository
import com.eltex.androidschool.ui.jobs.JobUiModel
import com.eltex.androidschool.ui.jobs.JobUiModelMapper
import com.eltex.androidschool.viewmodel.status.StatusLoad

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * ViewModel для управления данными о местах работы пользователя.
 *
 * Этот ViewModel отвечает за загрузку, отображение и управление местами работы пользователя.
 * Он использует `JobRepository` для получения данных из сети и управления состоянием мест работы.
 *
 * @property repository Репозиторий для работы с данными о местах работы.
 * @property userId Идентификатор пользователя, для которого загружаются данные.
 * @see ViewModel
 */
@HiltViewModel(assistedFactory = JobViewModel.ViewModelFactory::class)
class JobViewModel @AssistedInject constructor(
    private val repository: JobRepository,
    @Assisted private val userId: Long = BuildConfig.USER_ID,
) : ViewModel() {

    /**
     * Маппер для преобразования данных вакансии (JobData) в модель для UI (JobUiModel).
     */
    private val mapper = JobUiModelMapper()

    /**
     * Внутреннее состояние ViewModel, хранящее данные о вакансиях и статусе загрузки.
     */
    private val _state: MutableStateFlow<JobState> = MutableStateFlow(JobState())

    /**
     * Публичное состояние ViewModel, доступное для наблюдения из UI.
     */
    val state: StateFlow<JobState> = _state.asStateFlow()

    init {
        getJobsByUserId(userId)
    }

    /**
     * Загружает данные о местах работы пользователя.
     *
     * @param userId Идентификатор пользователя, для которого загружаются данные.
     * @see JobRepository.getJobsUserById
     */
    fun getJobsByUserId(userId: Long) {
        _state.update { stateJobs: JobState ->
            stateJobs.copy(
                statusJobs = StatusLoad.Idle
            )
        }

        viewModelScope.launch {
            try {
                val jobs: List<JobData> = repository.getJobsUserById(userId = userId)

                val jobsUiModels: List<JobUiModel> = withContext(Dispatchers.Default) {
                    jobs.map { job: JobData ->
                        mapper.map(job)
                    }
                }

                _state.update { stateJobs: JobState ->
                    stateJobs.copy(
                        statusJobs = StatusLoad.Idle,
                        jobs = jobsUiModels,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateJobs: JobState ->
                    stateJobs.copy(
                        statusJobs = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Удаляет место работы по его идентификатору.
     *
     * @param jobId Идентификатор места работы, которое нужно удалить.
     * @see JobRepository.deleteJobWall
     */
    fun deleteById(jobId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteJobWall(jobId = jobId)

                _state.update { stateJobs: JobState ->
                    stateJobs.copy(
                        statusJobs = StatusLoad.Idle,
                        jobs = _state.value.jobs.orEmpty().filter { job: JobUiModel ->
                            job.id != jobId
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { stateJobs: JobState ->
                    stateJobs.copy(
                        statusJobs = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Сбрасывает состояние ошибки и возвращает состояние в `Idle`.
     *
     * @see StatusEvent
     */
    fun consumerError() {
        _state.update { stateJobs: JobState ->
            stateJobs.copy(
                statusJobs = StatusLoad.Idle
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

    @AssistedFactory
    interface ViewModelFactory {
        fun create(userId: Long = 0L): JobViewModel
    }
}
