package com.eltex.androidschool.fragments

import android.content.Context

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentToolbarBinding

import com.eltex.androidschool.viewmodel.ToolBarViewModel

/**
 * Фрагмент, отвечающий за отображение панели инструментов.
 *
 * Этот фрагмент управляет отображением и скрытием кнопки сохранения в зависимости от состояния приложения.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class ToolbarFragment : Fragment() {

    /**
     * Вызывается при присоединении фрагмента к контексту.
     *
     * @param context Контекст, к которому присоединен фрагмент.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(this)
            .commit()
    }

    /**
     * Создает и возвращает представление для этого фрагмента.
     *
     * @param inflater Объект, который может преобразовать XML-файл макета в View-объекты.
     * @param container Родительский ViewGroup, в который будет добавлено представление.
     * @param savedInstanceState Сохраненное состояние фрагмента, если оно есть.
     * @return View, представляющий собой корневой элемент макета этого фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentToolbarBinding.inflate(inflater, container, false)

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        binding.toolbar.setupWithNavController(navController)

        val toolBarViewModel by activityViewModels<ToolBarViewModel>()

        val newPostItem = binding.toolbar.menu.findItem(R.id.save_post)

        toolBarViewModel.saveVisible.onEach {
            newPostItem.isVisible = it
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        newPostItem.setOnMenuItemClickListener {
            toolBarViewModel.onSaveClicked(true)
            true
        }

        return binding.root
    }
}
