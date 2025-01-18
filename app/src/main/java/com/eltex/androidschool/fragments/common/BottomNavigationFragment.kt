package com.eltex.androidschool.fragments.common

import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels

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

        binding.bottomNavigation.setupWithNavController(navController)

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

                R.id.userFragment -> {
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
}
