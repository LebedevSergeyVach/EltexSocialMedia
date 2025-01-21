package com.eltex.androidschool.adapter.common

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.utils.getErrorText

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(error: Throwable) {
        binding.errorTextItem.text = error.getErrorText(binding.root.context)
    }
}
