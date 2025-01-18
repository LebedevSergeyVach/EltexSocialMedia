package com.eltex.androidschool.adapter.job

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.ui.jobs.JobUiModel

/**
 * Callback для сравнения элементов списка мест работы.
 *
 * Этот класс используется для определения изменений в списке [JobUiModel] и оптимизации
 * обновления RecyclerView. Он сравнивает элементы по их идентификаторам и содержимому.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 * @see JobUiModel Модель данных для отображения информации о месте работы.
 */
class JobItemCallback : DiffUtil.ItemCallback<JobUiModel>() {

    /**
     * Проверяет, являются ли два элемента одинаковыми по идентификатору.
     *
     * @param oldItem Старый элемент списка.
     * @param newItem Новый элемент списка.
     * @return `true`, если идентификаторы элементов совпадают, иначе `false`.
     */
    override fun areItemsTheSame(oldItem: JobUiModel, newItem: JobUiModel): Boolean =
        oldItem.id == newItem.id

    /**
     * Проверяет, совпадает ли содержимое двух элементов.
     *
     * @param oldItem Старый элемент списка.
     * @param newItem Новый элемент списка.
     * @return `true`, если содержимое элементов совпадает, иначе `false`.
     */
    override fun areContentsTheSame(oldItem: JobUiModel, newItem: JobUiModel): Boolean =
        oldItem == newItem
}
