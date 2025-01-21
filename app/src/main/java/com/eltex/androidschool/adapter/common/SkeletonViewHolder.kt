package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.ItemSkeletonPostBinding

class SkeletonViewHolder(
    private val binding: ItemSkeletonPostBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.skeletonLayout.showSkeleton()
    }
}
