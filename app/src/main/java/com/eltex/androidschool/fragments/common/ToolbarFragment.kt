package com.eltex.androidschool.fragments.common

import android.content.Context

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentToolbarBinding
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Фрагмент, отвечающий за отображение панели инструментов.
 *
 * Этот фрагмент управляет отображением и скрытием кнопок сохранения, настроек и списка всех пользователей
 * в зависимости от состояния приложения. Он также обрабатывает клики по этим кнопкам.
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

        /**
         * Навигационный контроллер для управления переходами между фрагментами.
         *
         * Эта переменная инициализирует `NavController`, который управляет навигацией между фрагментами.
         * Она используется для настройки панели инструментов (Toolbar) и обработки переходов.
         *
         * @see NavController Контроллер для управления навигацией между фрагментами.
         * @see childFragmentManager Менеджер фрагментов для доступа к дочерним фрагментам.
         * @see R.id.toolbarContainer Идентификатор контейнера для панели инструментов.
         */
        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.toolbarContainer)).findNavController()

        binding.toolbar.setupWithNavController(navController)

        /**
         * ViewModel для управления состоянием панели инструментов (Toolbar).
         *
         * Эта переменная инициализирует `ToolBarViewModel`, который управляет видимостью кнопок
         * на панели инструментов (например, кнопки сохранения, настроек и списка всех пользователей).
         * ViewModel используется для синхронизации состояния панели инструментов между фрагментами.
         *
         * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
         * @see activityViewModels Делегат для получения ViewModel, привязанного к активности.
         */
        val toolBarViewModel by activityViewModels<ToolBarViewModel>()

        val menu = binding.toolbar.menu
        requireActivity().menuInflater.inflate(R.menu.menu_all_users, menu)
        requireActivity().menuInflater.inflate(R.menu.menu_settings, menu)

        val newPostItem = binding.toolbar.menu.findItem(R.id.save_post)

        val settingsItem = binding.toolbar.menu.findItem(R.id.settings)

        val allUsersItem = binding.toolbar.menu.findItem(R.id.all_users)

        toolBarViewModel.saveVisible.onEach { display: Boolean ->
            newPostItem.isVisible = display
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolBarViewModel.settingsVisible.onEach { display: Boolean ->
            settingsItem.isVisible = display
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolBarViewModel.allUsersVisible.onEach { display: Boolean ->
            allUsersItem.isVisible = display
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        newPostItem.setOnMenuItemClickListener {
            toolBarViewModel.onSaveClicked(true)
            true
        }

        settingsItem.setOnMenuItemClickListener {
            try {
                val navControllerForSettings =
                    (childFragmentManager.findFragmentById(R.id.toolbarContainer) as NavHostFragment).navController

                navControllerForSettings.navigate(
                    R.id.action_global_settingsFragment
                )
            } catch (e: Exception) {
                requireContext().toast(R.string.unknown_error)

                if (BuildConfig.DEBUG) {
                    Logger.e("Navigation error: ${e.message}")
                }
            }
            true
        }

        allUsersItem.setOnMenuItemClickListener {
            try {
                val navControllerForSettings =
                    (childFragmentManager.findFragmentById(R.id.toolbarContainer) as NavHostFragment).navController

                navControllerForSettings.navigate(
                    R.id.action_global_usersFragment
                )
            } catch (e: Exception) {
                requireContext().toast(R.string.unknown_error)

                if (BuildConfig.DEBUG) {
                    Logger.e("Navigation error: ${e.message}")
                }
            }
            true
        }

        return binding.root
    }
}
