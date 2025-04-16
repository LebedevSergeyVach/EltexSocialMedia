package com.eltex.androidschool.fragments.update

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.update.UpdateAdapter
import com.eltex.androidschool.data.update.UpdateData
import com.eltex.androidschool.databinding.FragmentUpdateBinding
import com.eltex.androidschool.ui.offset.OffsetDecoration

class ListAppUpdatesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUpdateBinding.inflate(layoutInflater)

        val updates = createListUpdatesApp()
        val updateAdapter = UpdateAdapter(
            updates,
            context = requireContext(),
        )

        binding.list.adapter = updateAdapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )
        updateAdapter.submitList(updates)

        return binding.root
    }

    private fun createListUpdatesApp() = listOf(
        UpdateData(
            id = 0,
            version = "v1.0.2",
            date = "25.02.2025",
            description = getString(R.string.v1_0_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v1.0.2"
        ),
        UpdateData(
            id = 1,
            version = "v1.1.2",
            date = "19.03.2025",
            description = getString(R.string.v1_1_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v1.1.2"
        ),
        UpdateData(
            id = 2,
            version = "v2.0.0",
            date = "16.04.2025",
            description = getString(R.string.v2_0_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v2.0.0"
        ),
    )
        .reversed()
}
