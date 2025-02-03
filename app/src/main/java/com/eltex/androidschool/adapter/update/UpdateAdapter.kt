package com.eltex.androidschool.adapter.update

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.data.update.UpdateData
import com.eltex.androidschool.databinding.CardUpdateBinding

/**
 * Адаптер для отображения списка обновлений в RecyclerView.
 *
 * @property updateData Список обновлений, которые будут отображаться.
 */
class UpdateAdapter(
    private val updateData: List<UpdateData>,
    private val context: Context,
) : ListAdapter<UpdateData, UpdateViewHolder>(UpdateItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardUpdateBinding.inflate(layoutInflater, parent, false)

        return UpdateViewHolder(
            context = parent.context,
            binding = binding,
        )
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(
            updateData = getItem(position)
        )
    }
}
