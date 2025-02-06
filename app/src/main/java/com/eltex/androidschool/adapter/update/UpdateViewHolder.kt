package com.eltex.androidschool.adapter.update

import android.content.Context
import android.content.Intent

import android.net.Uri

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
        binding.textVersionAndDateUpdate.text = buildString {
            append(context.getString(R.string.version))
            append(": ")
            append(updateData.version)
            append(" | ")
            append(context.getString(R.string.date))
            append(": ")
            append(updateData.date)
        }

        binding.textDescriptionUpdate.text = updateData.description

        binding.buttonOpenGitHubRepository.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(updateData.link)))
        }
    }
}
