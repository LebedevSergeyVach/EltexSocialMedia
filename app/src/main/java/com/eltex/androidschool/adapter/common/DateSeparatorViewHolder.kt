package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.databinding.ItemDateSeparatorBinding

class DateSeparatorViewHolder(
    private val binding: ItemDateSeparatorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(date: String) {
        binding.dateHeader.text = date
    }
}
