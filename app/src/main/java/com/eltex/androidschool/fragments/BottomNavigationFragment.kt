package com.eltex.androidschool.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding

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
                .navigate(R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment)
        }

        val eventsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment)
        }

        val usersClickListener = View.OnClickListener {
            // TODO: Implement event navigation logic
        }

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    binding.news.setOnClickListener(postsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.eventsFragment -> {
                    binding.news.setOnClickListener(eventsClickListener)
                    binding.news.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.usersFragment -> {
                    binding.news.setOnClickListener(usersClickListener)
                    binding.news.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }

        return binding.root
    }
}
