package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.databinding.ItemSkeletonPostBinding

/**
 * ViewHolder для отображения скелетонов (заглушек) в RecyclerView.
 * Используется для показа анимированных заглушек во время загрузки данных.
 *
 * @property binding Привязка к макету `ItemSkeletonPostBinding`, который содержит UI для отображения скелетона.
 */
class SkeletonViewHolder(
    private val binding: ItemSkeletonPostBinding
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Привязывает данные к ViewHolder и запускает анимацию скелетона.
     */
    fun bind() {
        binding.skeletonLayout.showSkeleton()
    }
}
