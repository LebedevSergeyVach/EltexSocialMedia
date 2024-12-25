package com.eltex.androidschool.fragments.users

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentUserBinding
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel

class UserFragment : Fragment() {

    companion object {
        const val USER_NAME = "USER_NAME"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(layoutInflater)

        /**
         * Получаем ViewModel для управления состоянием панели инструментов
         *
         * @see ToolBarViewModel
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val userName = arguments?.getString(USER_NAME) ?: ""

        binding.nameUser.text = userName
        binding.initial.text = userName.take(2)

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = userName

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> {
                            Unit
                        }
                    }
                }
            }
        )

        return binding.root
    }
}
