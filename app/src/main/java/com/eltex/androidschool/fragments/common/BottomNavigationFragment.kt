package com.eltex.androidschool.fragments.common

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding
import com.eltex.androidschool.utils.Logger
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
class BottomNavigationFragment : Fragment(), FabVisibilityListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentBottomNavigationBinding

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
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

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
                    binding.news.setOnClickListener(postsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }

                R.id.eventsFragment -> {
                    binding.news.setOnClickListener(eventsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }

                R.id.accountFragment -> {
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(200)
                        .start()
                }
            }
        }

        return binding.root
    }

    /**
     * Показывает FloatingActionButton с анимацией.
     */
    override fun showFab() {
        Logger.d("ПОКАЗ КНОПКИ!!!!")

        if (binding.news.visibility == View.GONE) {
            binding.news.visibility = View.VISIBLE
            binding.news.animate()
                .scaleX(1F)
                .scaleY(1F)
                .setDuration(200)
                .start()
        }
    }

    /**
     * Скрывает FloatingActionButton с анимацией.
     */
    override fun hideFab() {
        Logger.d("СКРЫТИЕ КНОПКИ!!!!")

        if (binding.news.visibility == View.VISIBLE) {
            binding.news.animate()
                .scaleX(0F)
                .scaleY(0F)
                .setDuration(200)
                .withEndAction {
                    binding.news.visibility = View.GONE
                }
                .start()
        }
    }

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
}
