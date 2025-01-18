package com.eltex.androidschool.adapter.job

import android.content.Context

import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.eltex.androidschool.databinding.CardJobBinding
import com.eltex.androidschool.ui.jobs.JobUiModel

/**
 * ViewHolder для отображения карточки места работы.
 *
 * Этот класс отвечает за привязку данных [JobUiModel] к элементам интерфейса карточки места работы.
 * Он также управляет видимостью кнопок удаления и обновления в зависимости от прав пользователя.
 *
 * @property binding Привязка данных к элементам интерфейса карточки места работы.
 * @property context Контекст, используемый для доступа к ресурсам и системным сервисам.
 *
 * @see JobUiModel Модель данных для отображения информации о месте работы.
 * @see ViewHolder Базовый класс для ViewHolder в RecyclerView.
 */
class JobViewHolder(
    private val binding: CardJobBinding,
    private val context: Context
) : ViewHolder(binding.root) {

    /**
     * Привязывает данные о месте работы к элементам интерфейса.
     *
     * Этот метод заполняет карточку места работы данными из [JobUiModel] и управляет видимостью
     * кнопок удаления и обновления в зависимости от прав пользователя.
     *
     * @param job Модель данных о месте работы.
     * @param currentUserId Идентификатор текущего пользователя.
     * @param authorId Идентификатор автора места работы.
     *
     * @see JobUiModel Модель данных для отображения информации о месте работы.
     */
    fun bindJob(job: JobUiModel, currentUserId: Long, authorId: Long) {
        binding.name.text = job.name
        binding.workDates.text = buildString {
            append(job.start)
            append(" - ")
            append(job.finish)
        }
        binding.position.text = job.position
        binding.link.text = job.link

        binding.delete.isVisible = currentUserId == authorId
    }
}
