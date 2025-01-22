package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.databinding.ItemProgressBinding

/**
 * ViewHolder для отображения состояния загрузки в RecyclerView.
 * Используется для показа индикатора загрузки (например, ProgressBar).
 *
 * @property binding Привязка к макету `ItemProgressBinding`, который содержит UI для отображения загрузки.
 */
class LoadingViewHolder(binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root)
