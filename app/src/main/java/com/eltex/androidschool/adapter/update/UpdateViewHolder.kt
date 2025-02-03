package com.eltex.androidschool.adapter.update

import android.content.Context

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.eltex.androidschool.R
import com.eltex.androidschool.data.update.UpdateData
import com.eltex.androidschool.databinding.CardUpdateBinding

/**
 * ViewHolder для отображения карточки обновления.
 */
class UpdateViewHolder(
    private val context: Context,
    private val binding: CardUpdateBinding,
) : ViewHolder(binding.root) {

    /**
     * Привязывает данные об обновлении к элементам View.
     *
     * @param updateData Данные об обновлении.
     */
    fun bind(updateData: UpdateData) {
        binding.version.text = buildString {
            append(context.getString(R.string.version))
            append(": ")
            append(updateData.version)
        }
        binding.dateUpdate.text = buildString {
            append(context.getString(R.string.date))
            append(": ")
            append(updateData.date)
        }
        binding.descriptionUpdate.text = updateData.description
        binding.link.text = updateData.link
    }
}
