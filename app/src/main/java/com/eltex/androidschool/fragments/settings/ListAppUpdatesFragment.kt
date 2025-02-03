package com.eltex.androidschool.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eltex.androidschool.databinding.FragmentUpdateBinding

class ListAppUpdatesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUpdateBinding.inflate(layoutInflater)

        return binding.root
    }
}
