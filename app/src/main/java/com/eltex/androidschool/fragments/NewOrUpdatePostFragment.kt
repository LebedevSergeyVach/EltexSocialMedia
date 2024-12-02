package com.eltex.androidschool.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentNewOrUpdatePostBinding

import com.eltex.androidschool.db.AppDbPost

import com.eltex.androidschool.repository.DaoSQLitePostRepository

import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.utils.vibrateWithEffect

import com.eltex.androidschool.viewmodel.NewPostViewModel
import com.eltex.androidschool.viewmodel.ToolBarViewModel

/**
 * Фрагмент для создания или обновления поста.
 *
 * Этот фрагмент позволяет пользователю создавать новый пост или обновлять существующий.
 * Он также управляет отображением и скрытием кнопки сохранения в зависимости от состояния фрагмента.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see NewPostViewModel ViewModel для управления созданием и обновлением постов.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class NewOrUpdatePostFragment : Fragment() {

    companion object {
        const val POST_ID = "POST_ID"
        const val POST_CONTENT = "POST_CONTENT"
        const val IS_UPDATE = "IS_UPDATE"
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
        val binding = FragmentNewOrUpdatePostBinding.inflate(layoutInflater)

        /**
        Получаем ViewModel для управления состоянием панели инструментов
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val postId = arguments?.getLong(POST_ID) ?: 0L
        val content = arguments?.getString(POST_CONTENT) ?: ""
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) ?: false

        binding.content.setText(content)

        /**
        Получаем ViewModel для управления созданием и обновлением постов
         */
        val newPostVewModel by viewModels<NewPostViewModel> {
            viewModelFactory {
                addInitializer(
                    NewPostViewModel::class
                ) {
                    NewPostViewModel(
                        repository = DaoSQLitePostRepository(
                            AppDbPost.getInstance(requireContext().applicationContext).postDao
                        ),
                        postId = postId
                    )
                }
            }
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty()

                if (newContent.isNotEmpty()) {
                    newPostVewModel.save(content = newContent)
                    findNavController().navigateUp()
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().toast(R.string.error_text_post_is_empty)
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(true)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> {
                            Unit
                        }
                    }
                }
            }
        )

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)

        toolbar.title =
            if (isUpdate) getString(R.string.update_post_title) else getString(R.string.new_post_title)

        return binding.root
    }
}
