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
import com.eltex.androidschool.ui.common.OffsetDecoration

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
            version = "v0.10.0",
            date = "03.01.2025",
            description = getString(R.string.v0_10_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.10.0"
        ),
        UpdateData(
            id = 1,
            version = "v0.11.0",
            date = "04.01.2025",
            description = getString(R.string.v0_11_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.11.0"
        ),
        UpdateData(
            id = 2,
            version = "v0.13.0",
            date = "12.01.2025",
            description = getString(R.string.v0_13_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.13.0"
        ),
        UpdateData(
            id = 3,
            version = "v0.14.0",
            date = "17.01.2025",
            description = getString(R.string.v0_14_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.14.0"
        ),
        UpdateData(
            id = 4,
            version = "v0.15.2",
            date = "21.01.2025",
            description = getString(R.string.v0_15_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.15.2"
        ),
        UpdateData(
            id = 5,
            version = "v0.16.0",
            date = "23.01.2025",
            description = getString(R.string.v0_16_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.16.0"
        ),
        UpdateData(
            id = 6,
            version = "v0.18.1",
            date = "30.01.2025",
            description = getString(R.string.v0_18_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.18.1"
        ),
        UpdateData(
            id = 7,
            version = "v0.20.0",
            date = "03.02.2025",
            description = getString(R.string.v0_20_0_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.20.0"
        ),
        UpdateData(
            id = 8,
            version = "v0.21.1",
            date = "04.02.2025",
            description = getString(R.string.v0_21_1_description),
            link = "https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.21.0"
        ),
    )
        .reversed()
}
