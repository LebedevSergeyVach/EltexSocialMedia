package com.eltex.androidschool.adapter.events

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.databinding.ItemSkeletonEventBinding

/**
 * ViewHolder для отображения скелетона события в RecyclerView.
 * Используется для показа анимированных заглушек во время загрузки данных.
 *
 * @property binding Привязка к макету `ItemSkeletonEventBinding`, который содержит UI для отображения скелетона.
 */
class SkeletonEventViewHolder(
    private val binding: ItemSkeletonEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Привязывает данные к ViewHolder и запускает анимацию скелетона.
     */
    fun bind() {
        binding.skeletonLayout.showSkeleton()
    }
}
