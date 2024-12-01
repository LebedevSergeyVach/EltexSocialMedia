package com.eltex.androidschool.fragments

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEventsBinding

import com.eltex.androidschool.adapter.EventAdapter
import com.eltex.androidschool.adapter.OffsetDecoration

import com.eltex.androidschool.data.EventData

import com.eltex.androidschool.db.AppDbEvent

import com.eltex.androidschool.repository.DaoSQLiteEventRepository

import com.eltex.androidschool.viewmodel.EventState
import com.eltex.androidschool.viewmodel.EventViewModel


/**
 * Фрагмент, отображающий список событий.
 *
 * Этот фрагмент отвечает за отображение списка событий, а также за обработку событий, связанных с событиями,
 * таких как создание, редактирование и удаление событий.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see EventViewModel ViewModel для управления состоянием событий.
 * @see EventAdapter Адаптер для отображения списка событий.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
class EventsFragment : Fragment() {

    /**
     * ViewModel для управления состоянием событий.
     *
     * @see EventViewModel
     */
    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    DaoSQLiteEventRepository(
                        AppDbEvent.getInstance(requireContext()).eventDao
                    )
                )
            }
        }
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
        val binding = FragmentEventsBinding.inflate(layoutInflater, container, false)

        val adapter = EventAdapter(
            object : EventAdapter.EventListener {
                override fun onLikeClicked(event: EventData) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClicked(event: EventData) {}

                override fun onParticipateClicked(event: EventData) {
                    viewModel.participateById(event.id)
                }

                override fun onDeleteClicked(event: EventData) {
                    viewModel.deleteById(event.id)
                }

                override fun onUpdateClicked(event: EventData) {
                    findNavController()
                        .navigate(
                            R.id.action_eventsFragment_to_newOrUpdateEventFragment,
                            bundleOf(
                                NewOrUpdateEventFragment.EVENT_ID to event.id,
                                NewOrUpdateEventFragment.EVENT_CONTENT to event.content,
                                NewOrUpdateEventFragment.EVENT_LINK to event.link,
                                NewOrUpdateEventFragment.EVENT_DATE to event.dataEvent,
                                NewOrUpdateEventFragment.EVENT_OPTION to event.optionConducting
                            )
                        )
                }
            },
            context = requireContext()
        )

        // Устанавливаем адаптер для RecyclerView
        binding.list.adapter = adapter

        // Добавляем декорацию для отступов между элементами
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        // Подписываемся на изменения состояния событий
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { eventState: EventState ->
                adapter.submitList(eventState.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}