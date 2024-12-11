package com.eltex.androidschool.fragments.events

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEventsBinding

import com.eltex.androidschool.adapter.events.EventAdapter
import com.eltex.androidschool.ui.OffsetDecoration

import com.eltex.androidschool.data.events.EventData

import com.eltex.androidschool.repository.events.NetworkEventRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.toast

import com.eltex.androidschool.viewmodel.events.EventState
import com.eltex.androidschool.viewmodel.events.EventViewModel


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
                    NetworkEventRepository()
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
                    viewModel.likeById(event.id, event.likedByMe)
                }

                override fun onShareClicked(event: EventData) {}

                override fun onParticipateClicked(event: EventData) {
                    viewModel.participateById(event.id, event.participatedByMe)
                }

                override fun onDeleteClicked(event: EventData) {
                    viewModel.deleteById(event.id)
                }

                override fun onUpdateClicked(event: EventData) {
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(
                            R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment,
                            bundleOf(
                                NewOrUpdateEventFragment.EVENT_ID to event.id,
                                NewOrUpdateEventFragment.EVENT_CONTENT to event.content,
                                NewOrUpdateEventFragment.EVENT_LINK to event.link,
                                NewOrUpdateEventFragment.EVENT_DATE to event.dataEvent,
                                NewOrUpdateEventFragment.EVENT_OPTION to event.optionConducting,
                                NewOrUpdateEventFragment.IS_UPDATE to true,
                            ),
                            NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build()
                        )
                }
            },
            context = requireContext()
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        binding.swiperRefresh.setOnRefreshListener {
            viewModel.load()
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { eventState: EventState ->
                binding.errorGroup.isVisible = eventState.isEmptyError

                val errorText: CharSequence? =
                    eventState.statusEvent.throwableOrNull?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progressBar.isVisible = eventState.isEmptyLoading

                binding.swiperRefresh.isRefreshing = eventState.isRefreshing

                if (eventState.isRefreshError && errorText == getString(R.string.network_error)) {
                    requireContext().toast(R.string.network_error)

                    viewModel.consumerError()
                } else if (eventState.isRefreshError && errorText == getString(R.string.unknown_error)) {
                    requireContext().toast(R.string.unknown_error)

                    viewModel.consumerError()
                }

                adapter.submitList(eventState.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}