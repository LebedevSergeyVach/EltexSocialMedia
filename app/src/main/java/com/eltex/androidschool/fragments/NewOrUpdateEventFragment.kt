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

import com.eltex.androidschool.databinding.FragmentNewOrUpdateEventBinding

import com.eltex.androidschool.db.AppDbEvent

import com.eltex.androidschool.repository.DaoSQLiteEventRepository

import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.utils.vibrateWithEffect

import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolBarViewModel

/**
 * Фрагмент для создания или обновления события.
 *
 * Этот фрагмент позволяет пользователю создавать новое событие или обновлять существующее.
 * Он также управляет отображением и скрытием кнопки сохранения в зависимости от состояния фрагмента.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see NewEventViewModel ViewModel для управления созданием и обновлением событий.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class NewOrUpdateEventFragment : Fragment() {

    companion object {
        const val EVENT_ID = "EVENT_ID"
        const val EVENT_CONTENT = "EVENT_CONTENT"
        const val EVENT_LINK = "EVENT_LINK"
        const val EVENT_DATE = "EVENT_DATE"
        const val EVENT_OPTION = "EVENT_OPTION"
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
        val binding = FragmentNewOrUpdateEventBinding.inflate(inflater, container, false)

        /**
        Получаем ViewModel для управления состоянием панели инструментов
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val eventId = arguments?.getLong(EVENT_ID) ?: 0L
        val content = arguments?.getString(EVENT_CONTENT) ?: ""
        val link = arguments?.getString(EVENT_LINK) ?: ""
        val date = arguments?.getString(EVENT_DATE) ?: ""
        val option = arguments?.getString(EVENT_OPTION) ?: ""
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) ?: false

        binding.content.setText(content)
        binding.link.setText(link)
        binding.data.setText(date)
        binding.option.setText(option)

        /**
        Получаем ViewModel для управления созданием и обновлением событий
         */
        val newEventViewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                addInitializer(
                    NewEventViewModel::class
                ) {
                    NewEventViewModel(
                        repository = DaoSQLiteEventRepository(
                            AppDbEvent.getInstance(requireContext().applicationContext).eventDao
                        ),
                        eventId = eventId
                    )
                }
            }
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty().trimStart().trimEnd()
                val newDate = binding.data.text?.toString().orEmpty().trimStart().trimEnd()
                val newOption = binding.option.text?.toString().orEmpty().trimStart().trimEnd()
                val newLink = binding.link.text?.toString().orEmpty().trimStart().trimEnd()

                if (
                    newContent.isNotEmpty() && newDate.isNotEmpty() && newOption.isNotEmpty() && newLink.isNotEmpty()
                ) {
                    newEventViewModel.save(
                        content = newContent,
                        link = newLink,
                        option = newOption,
                        data = newDate,
                    )
                    findNavController().navigateUp()
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().toast(R.string.error_text_event_is_empty)
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
            if (isUpdate) getString(R.string.update_event_title) else getString(R.string.new_event_title)

        return binding.root
    }
}
