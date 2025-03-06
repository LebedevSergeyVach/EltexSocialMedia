package com.eltex.androidschool.fragments.common

import android.content.Context

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentToolbarBinding
import com.eltex.androidschool.fragments.auth.AuthorizationFragment
import com.eltex.androidschool.utils.helper.LoggerHelper
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.toast
import com.eltex.androidschool.viewmodel.auth.user.AuthorizationSharedViewModel
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import java.io.File

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

    private val authorizationSharedViewModel: AuthorizationSharedViewModel by activityViewModels()

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

        lifecycleScope.launch {
            authorizationSharedViewModel.isLoading.collect { isLoading ->
                if (!isLoading) {
                    authorizationSharedViewModel.isAuthorized.collect { isAuthorized ->
                        if (!isAuthorized) {
                            navigateToAuthorizationFragment()
                        } else {
                            navController.setGraph(R.navigation.main_navigation)
                            binding.toolbar.setupWithNavController(navController = navController)
                        }
                    }
                }
            }
        }

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
        requireActivity().menuInflater.inflate(R.menu.menu_logout, menu)
        requireActivity().menuInflater.inflate(R.menu.menu_settings, menu)
        requireActivity().menuInflater.inflate(R.menu.menu_rules, menu)

        val newPostItem = binding.toolbar.menu.findItem(R.id.save)
        val settingsItem = binding.toolbar.menu.findItem(R.id.settings)
        val allUsersItem = binding.toolbar.menu.findItem(R.id.all_users)
        val logoutItem = binding.toolbar.menu.findItem(R.id.logout)
        val rulesItem = binding.toolbar.menu.findItem(R.id.rules)

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

        toolBarViewModel.logoutVisible.onEach { display: Boolean ->
            logoutItem.isVisible = display
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolBarViewModel.rulesVisible.onEach { display: Boolean ->
            rulesItem.isVisible = display
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
                    LoggerHelper.e("Navigation error: ${e.message}")
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
                    LoggerHelper.e("Navigation error: ${e.message}")
                }
            }
            true
        }

        logoutItem.setOnMenuItemClickListener {
            showDeleteConfirmationDialog(
                title = getString(R.string.logout_account),
                message = getString(R.string.confirmation_of_account_logout),
                textButtonCancel = getString(R.string.cancel),
                textButtonDelete = getString(R.string.logout),
            ) {
                requireContext().singleVibrationWithSystemCheck(35)

                runBlocking {
                    authorizationSharedViewModel.clearUserData()
                }

                clearCache(context = requireContext())

                requireActivity().supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                    )
                    replace(R.id.container, AuthorizationFragment())
                    setReorderingAllowed(true)
                    remove(this@ToolbarFragment)
                }
            }

            true
        }

        rulesItem.setOnMenuItemClickListener {
            navigateToRulesFragment()
            true
        }

        return binding.root
    }

    /**
     * Переходит к фрагменту авторизации, заменяя текущий фрагмент.
     *
     * Это функция, которая не принимает параметров и не возвращает значений.
     *
     * @see AuthorizationFragment
     */
    private fun navigateToAuthorizationFragment() {
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left,
            )
            replace(R.id.container, AuthorizationFragment())
            setReorderingAllowed(true)
            remove(this@ToolbarFragment)
        }
    }

    private fun navigateToRulesFragment() {
        val navControllerForSettings =
            (childFragmentManager.findFragmentById(R.id.toolbarContainer) as NavHostFragment).navController

        navControllerForSettings.navigate(
            R.id.action_global_rulesFragment
        )
    }

    /**
     * Выполняет навигацию на указанный фрагмент.
     *
     * Эта функция определяет, какой фрагмент нужно открыть, и выполняет навигацию через `NavController`.
     * Поддерживаются переходы на `NewOrUpdatePostFragment` и `NewOrUpdateEventFragment`.
     *
     * @param navController Контроллер навигации, используемый для перехода между фрагментами.
     * @param fragmentName Имя фрагмента, на который нужно перейти. Поддерживаются значения "NewPostFragment" и "NewEventFragment".
     *
     * @see NavController Класс для управления навигацией между фрагментами.
     * @see NewOrUpdatePostFragment Фрагмент для создания или редактирования поста.
     * @see NewOrUpdateEventFragment Фрагмент для создания или редактирования события.
     *
     * @throws IllegalArgumentException Если имя фрагмента не поддерживается.
     */
    private fun navigateToFragment(navController: NavController, fragmentName: String) {
        when (fragmentName) {
            "NewPostFragment" -> {
                navController.navigate(R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment)
            }

            "NewEventFragment" -> {
                navController.navigate(R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment)
            }
        }
    }

    /**
     * Отображает диалог подтверждения удаления с двумя кнопками.
     *
     * @param title Заголовок диалога.
     * @param message Сообщение, которое будет отображаться в диалоге.
     * @param textButtonCancel Текст для кнопки отмены.
     * @param textButtonDelete Текст для кнопки удаления.
     * @param onDeleteConfirmed Лямбда-функция, которая будет вызвана,
     * когда пользователь подтвердит удаление.
     *
     * @see requireContext().showMaterialDialogWithTwoButtons
     */
    private fun showDeleteConfirmationDialog(
        title: String,
        message: String,
        textButtonCancel: String,
        textButtonDelete: String,
        onDeleteConfirmed: () -> Unit,
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            cancelButtonText = textButtonCancel,
            deleteButtonText = textButtonDelete,
            onDeleteConfirmed = onDeleteConfirmed,
        )
    }

    /**
     * Очищает кэш приложения.
     *
     * @param context Контекст приложения.
     */
    private fun clearCache(context: Context) {
        val cacheDir: File = context.cacheDir

        fun deleteFiles(file: File) {
            if (file.isDirectory) {
                file.listFiles()?.forEach { fileCache: File ->
                    deleteFiles(fileCache)
                }
            }

            file.delete()
        }

        deleteFiles(cacheDir)
    }

    /**
     * Устанавливает иконку навигации для панели инструментов.
     *
     * @param binding Объект [FragmentToolbarBinding], используемый для доступа
     * к элементам интерфейса панели инструментов.
     *
     * @see R.drawable.ic_arrow_back_24
     */
    private fun navigationIcon(binding: FragmentToolbarBinding) {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        binding.toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.active_element
            )
        )
    }
}
