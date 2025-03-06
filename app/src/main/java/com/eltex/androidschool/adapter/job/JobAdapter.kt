package com.eltex.androidschool.adapter.job

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.databinding.CardJobBinding
import com.eltex.androidschool.ui.jobs.JobUiModel
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck

/**
 * Адаптер для отображения списка мест работы.
 *
 * Этот адаптер управляет отображением списка [JobUiModel] в RecyclerView. Он также обрабатывает
 * нажатия на кнопки удаления и обновления, передавая события через интерфейс [JobListener].
 *
 * @property listener Интерфейс для обработки событий удаления и обновления.
 * @property context Контекст, используемый для доступа к ресурсам и системным сервисам.
 * @property currentUserId Идентификатор текущего пользователя.
 * @property authorId Идентификатор автора места работы.
 *
 * @see ListAdapter Базовый класс для адаптеров RecyclerView с поддержкой DiffUtil.
 * @see JobUiModel Модель данных для отображения информации о месте работы.
 * @see JobListener Интерфейс для обработки событий удаления и обновления.
 */
class JobAdapter (
    private val listener: JobListener,
    private val context: Context,
    private val currentUserId: Long,
    private val authorId: Long
) : ListAdapter<JobUiModel, JobViewHolder>(JobItemCallback()) {

    /**
     * Интерфейс для обработки событий удаления и обновления места работы.
     */
    interface JobListener {
        /**
         * Вызывается при нажатии на кнопку удаления.
         *
         * @param job Модель данных о месте работы, которое нужно удалить.
         */
        fun onDeleteClicked(job: JobUiModel)

        /**
         * Вызывается при нажатии на кнопку обновления.
         *
         * @param job Модель данных о месте работы, которое нужно обновить.
         */
        fun onUpdateClicked(job: JobUiModel)
    }

    /**
     * Создает новый ViewHolder для отображения карточки места работы.
     *
     * @param parent Родительский ViewGroup, в который будет добавлен новый ViewHolder.
     * @param viewType Тип ViewHolder (не используется в данном случае).
     * @return Новый экземпляр [JobViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =CardJobBinding.inflate(layoutInflater, parent, false)

        val viewHolder = JobViewHolder(
            binding = binding,
            context = context
        )

        binding.delete.setOnClickListener {
            listener.onDeleteClicked(getItem(viewHolder.bindingAdapterPosition))
            context.singleVibrationWithSystemCheck(35)
        }

        binding.update.setOnClickListener {
            listener.onUpdateClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        return viewHolder
    }

    /**
     * Привязывает данные о месте работы к ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bindJob(
            job = getItem(position),
            currentUserId = currentUserId,
            authorId = authorId,
        )
    }
}
