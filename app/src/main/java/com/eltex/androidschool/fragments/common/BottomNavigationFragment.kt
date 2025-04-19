package com.eltex.androidschool.fragments.common

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding
import com.eltex.androidschool.viewmodel.common.SharedViewModel

/**
 * Фрагмент, отвечающий за отображение нижней навигационной панели.
 *
 * Этот фрагмент управляет навигацией между различными экранами приложения с помощью нижней навигационной панели.
 * Он также обрабатывает клики на элементы навигации и анимацию при переключении между экранами.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see FragmentBottomNavigationBinding Класс, сгенерированный из XML-файла макета для этого фрагмента.
 */
class BottomNavigationFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var isFabInitialized = false
    private var isFabHiding = false
    private var isFabShowing = false

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
        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

//        displayingIconsInBottomNavigate(navController = navController, binding = binding)

        val postsClickListener = View.OnClickListener {
            findNavController()
                .navigate(
                    R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment,
                    null,
                    NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                )
        }

        val eventsClickListener = View.OnClickListener {
            findNavController()
                .navigate(
                    R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment,
                    null,
                    NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                )
        }

        val jobsClickListener = View.OnClickListener {
            findNavController()
                .navigate(
                    R.id.action_BottomNavigationFragment_to_newOrUpdateJobFragment,
                    null,
                    NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                )
        }

        sharedViewModel.currentTab.observe(viewLifecycleOwner) { tabPosition ->
            when (tabPosition) {
                0 -> binding.news.setOnClickListener(postsClickListener)
                1 -> binding.news.setOnClickListener(eventsClickListener)
                2 -> binding.news.setOnClickListener(jobsClickListener)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    showFab(binding = binding)

                    binding.news.setOnClickListener(postsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }

                R.id.eventsFragment -> {
                    showFab(binding = binding)

                    binding.news.setOnClickListener(eventsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }

                R.id.accountFragment -> {
                    showFab(binding = binding)

                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }
            }
        }

        setupBackButtonHandler(navController = navController)

        // Наблюдаем за состоянием FAB с учетом анимации
        sharedViewModel.fabVisibility.observe(viewLifecycleOwner) { state: SharedViewModel.FabState ->
            when (state) {
                SharedViewModel.FabState.Visible -> showFab(binding = binding)
                SharedViewModel.FabState.Hidden -> hideFab(binding = binding)
            }
        }

        return binding.root
    }

    /**
     * Показывает FloatingActionButton с анимацией.
     */
    private fun showFab(binding: FragmentBottomNavigationBinding) {
        if ((binding.news.isVisible && !isFabHiding) || isFabShowing) return

        isFabShowing = true

        binding.news.visibility = View.VISIBLE
        val params = binding.news.layoutParams as ViewGroup.MarginLayoutParams
        binding.news.translationY = binding.news.height.toFloat() + params.bottomMargin

        binding.news.animate()
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .withStartAction {
                binding.news.isClickable = true
            }
            .withEndAction {
                isFabShowing = false
            }
            .start()
    }

    /**
     * Скрывает FloatingActionButton с анимацией.
     */
    private fun hideFab(binding: FragmentBottomNavigationBinding) {
        if (binding.news.visibility != View.VISIBLE || isFabHiding) return

        isFabHiding = true

        val params = binding.news.layoutParams as ViewGroup.MarginLayoutParams
        val totalOffset = binding.news.height + params.bottomMargin

        binding.news.animate()
            .translationY(totalOffset.toFloat())
            .setDuration(300)
            .setInterpolator(AccelerateInterpolator())
            .withStartAction {
                binding.news.isClickable = false
            }
            .withEndAction {
                binding.news.visibility = View.INVISIBLE
                binding.news.translationY = 0f
                isFabHiding = false
            }
            .start()
    }

    /**
     * Обновляет иконки в нижней навигации в зависимости от текущего назначения.
     *
     * @param navController Объект [NavController], который управляет навигацией.
     * @param binding Объект [FragmentBottomNavigationBinding], используемый для доступа
     * к элементам интерфейса нижней навигации.
     *
     * @see updateBottomNavigationIcons
     */
    private fun displayingIconsInBottomNavigate(
        navController: NavController,
        binding: FragmentBottomNavigationBinding
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    updateBottomNavigationIcons(
                        postsIconRes = R.drawable.ic_posts_active_24,
                        eventsIconRes = R.drawable.ic_events_passive_24,
                        accountIconRes = R.drawable.ic_account_passive_24,
                        binding = binding,
                    )
                }

                R.id.eventsFragment -> {
                    updateBottomNavigationIcons(
                        postsIconRes = R.drawable.ic_posts_passive_24,
                        eventsIconRes = R.drawable.ic_events_active_24,
                        accountIconRes = R.drawable.ic_account_passive_24,
                        binding = binding,
                    )
                }

                R.id.accountFragment -> {
                    updateBottomNavigationIcons(
                        postsIconRes = R.drawable.ic_posts_passive_24,
                        eventsIconRes = R.drawable.ic_events_passive_24,
                        accountIconRes = R.drawable.ic_account_active_24,
                        binding = binding,
                    )
                }

                else -> {
                    updateBottomNavigationIcons(
                        postsIconRes = R.drawable.ic_posts_passive_24,
                        eventsIconRes = R.drawable.ic_events_passive_24,
                        accountIconRes = R.drawable.ic_account_passive_24,
                        binding = binding,
                    )
                }
            }
        }
    }

    /**
     * Обновляет иконки нижней навигации на основе переданных ресурсов иконок.
     *
     * @param postsIconRes Ресурс иконки для вкладки постов.
     * @param eventsIconRes Ресурс иконки для вкладки событий.
     * @param accountIconRes Ресурс иконки для вкладки аккаунта.
     * @param binding Объект [FragmentBottomNavigationBinding], используемый для доступа
     * к элементам интерфейса нижней навигации.
     *
     * @see binding.bottomNavigation.menu
     */
    private fun updateBottomNavigationIcons(
        postsIconRes: Int,
        eventsIconRes: Int,
        accountIconRes: Int,
        binding: FragmentBottomNavigationBinding
    ) {
        val menu = binding.bottomNavigation.menu
        menu.findItem(R.id.postsFragment).setIcon(postsIconRes)
        menu.findItem(R.id.eventsFragment).setIcon(eventsIconRes)
        menu.findItem(R.id.accountFragment).setIcon(accountIconRes)
    }

    /**
     * Настраивает пользовательское поведение кнопки "Назад" для фрагментов, управляемых нижней навигацией.
     *
     * - Если пользователь находится в фрагменте "События" (`eventsFragment`) или "Профиль" (`accountFragment`), при нажатии на кнопку "Назад"
     *   он будет перенаправлен на фрагмент "Список постов" (`postsFragment`).
     * - Если пользователь уже находится в фрагменте "Список постов", то обработка кнопки "Назад" будет передана системе
     *   для выполнения стандартного поведения (например, выхода из приложения или перехода назад).
     *
     * Используется `OnBackPressedDispatcher` для управления поведением кнопки "Назад", что является предпочтительным способом
     * вместо устаревшего метода `onBackPressed()`.
     *
     * @param navController Навигационный контроллер, используемый для управления переходами между фрагментами.
     *
     * @see OnBackPressedDispatcher
     * @see OnBackPressedCallback
     */
    private fun setupBackButtonHandler(navController: NavController) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (navController.currentDestination?.id) {
                        R.id.eventsFragment -> {
                            navController.navigate(
                                R.id.postsFragment,
                                null,
                                NavOptions.Builder()
                                    .build()
                            )
                        }

                        R.id.accountFragment -> {
                            navController.navigate(
                                R.id.postsFragment,
                                null,
                                NavOptions.Builder()
                                    .build()
                            )
                        }

                        R.id.postsFragment -> {
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }

                        else -> {
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        )
    }
}
