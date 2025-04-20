package com.eltex.androidschool.fragments.events

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.events.EventAdapterDifferentTypesView
import com.eltex.androidschool.databinding.FragmentEventsBinding
import com.eltex.androidschool.fragments.users.AccountFragment
import com.eltex.androidschool.ui.offset.OffsetDecoration
import com.eltex.androidschool.ui.events.EventPagingMapper
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.viewmodel.auth.user.AccountViewModel
import com.eltex.androidschool.viewmodel.common.SharedViewModel
import com.eltex.androidschool.viewmodel.events.events.EventMessage
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventViewModel

import com.google.android.material.bottomnavigation.BottomNavigationView

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

/**
 * Фрагмент, отображающий список событий.
 *
 * Этот фрагмент отвечает за отображение списка событий, а также за обработку событий, связанных с событиями,
 * таких как создание, редактирование и удаление событий.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see EventViewModel ViewModel для управления состоянием событий.
 * @see EventAdapterDifferentTypesView Адаптер для отображения списка событий.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val accountViewModel: AccountViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var currentTabPosition = 0 // Установите правильную позицию таба

    /**
     * ViewModel для управления состоянием событий.
     *
     * @see EventViewModel
     */
    private val viewModel by viewModels<EventViewModel>()

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

        val adapter: EventAdapterDifferentTypesView = createEventAdapterDifferentTypesView(
            accountViewModel = accountViewModel,
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        binding.list.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val itemsCount = adapter.itemCount
                    val adapterPosition = binding.list.getChildAdapterPosition(view)

                    if (itemsCount - 5 <= adapterPosition) {
                        viewModel.accept(message = EventMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )

        setupOptimizedScrollListener(binding.list)

        binding.retryButton.setOnClickListener {
            viewModel.accept(message = EventMessage.Refresh)
        }

        binding.swiperRefresh.setOnRefreshListener {
            viewModel.accept(message = EventMessage.Refresh)
            requireContext().singleVibrationWithSystemCheck(35)
        }

        binding.swiperRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.active_element)
        )

        binding.swiperRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(requireContext(), R.color.background_color_of_the_refresh_circle)
        )

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdateEventFragment.EVENT_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            scrollToTopAndRefresh(binding = binding)
        }

        eventViewModelState(binding = binding, adapter = adapter)

        return binding.root
    }

    /**
     * Создает и возвращает экземпляр [EventAdapterDifferentTypesView], который используется для отображения списка событий.
     * Адаптер настраивается с помощью [EventListener], который обрабатывает различные действия пользователя,
     * такие как лайк, удаление, обновление и переход к профилю пользователя.
     *
     * @return [EventAdapterDifferentTypesView] - адаптер для отображения постов.
     *
     * @see [EventAdapterDifferentTypesView] - класс адаптера, который управляет отображением списка событий.
     * @see EventAdapterDifferentTypesView.EventListener - интерфейс, который определяет методы для обработки действий пользователя.
     */
    private fun createEventAdapterDifferentTypesView(
        accountViewModel: AccountViewModel,
    ): EventAdapterDifferentTypesView = EventAdapterDifferentTypesView(
        object : EventAdapterDifferentTypesView.EventListener {
            override fun onLikeClicked(event: EventUiModel) {
                viewModel.accept(message = EventMessage.Like(event = event))
            }

            override fun onParticipateClicked(event: EventUiModel) {
                viewModel.accept(message = EventMessage.Participation(event = event))
            }

            override fun onShareClicked(event: EventUiModel) {}

            override fun onDeleteClicked(event: EventUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_event_title),
                    message = getString(R.string.delete_event_message)
                ) {
                    viewModel.accept(message = EventMessage.Delete(event = event))
                }
            }

            override fun onUpdateClicked(event: EventUiModel) {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(
                        R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment,
                        bundleOf(
                            NewOrUpdateEventFragment.EVENT_ID to event.id,
                            NewOrUpdateEventFragment.EVENT_CONTENT to event.content,
                            NewOrUpdateEventFragment.EVENT_LINK to event.link,
                            NewOrUpdateEventFragment.EVENT_DATE to event.dateEvent,
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

            override fun onGetUserClicked(event: EventUiModel) {
                if (event.authorId == accountViewModel.userId) {
                    val bottomNav = requireParentFragment().requireParentFragment()
                        .requireView().findViewById<BottomNavigationView>(R.id.bottomNavigation)

                    bottomNav.selectedItemId = R.id.accountFragment

                    findNavController().navigate(
                        R.id.accountFragment,
                        null,
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    )
                } else {
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(
                            R.id.action_BottomNavigationFragment_to_userFragment,
                            bundleOf(
                                AccountFragment.USER_ID to event.authorId,
                                AccountFragment.IC_PROFILE to false
                            ),
                            NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build()
                        )
                }
            }

            override fun onGetEventDetailsClicked(event: EventUiModel) {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(
                        R.id.action_BottomNavigationFragment_to_fragmentEventDetails,
                        bundleOf(
                            FragmentEventDetails.EVENT_ID to event.id,
                            FragmentEventDetails.ACCOUNT_ID to accountViewModel.userId,
                        ),
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    )
            }

            override fun onRetryPageClicked() {
                viewModel.accept(EventMessage.Retry)
            }
        },

        context = requireContext(),
        currentUserId = accountViewModel.userId,
    )

    /**
     * Настраивает наблюдение за состоянием [ViewModel] и обновляет UI в соответствии с текущим состоянием.
     * Метод связывает [EventState] с элементами UI, такими [как] ProgressBar, [SwipeRefreshLayout] и [RecyclerView].
     *
     * @param binding [FragmentEventsBinding] - объект binding, который предоставляет доступ к элементам UI.
     * @param adapter [EventAdapterDifferentTypesView] - адаптер, который управляет отображением списка событий.
     *
     * @see [EventState] - класс, который представляет состояние экрана с постами.
     * @see [EventMessage] - класс, который представляет сообщения, отправляемые во ViewModel.
     * @see [EventPagingMapper] - класс, который преобразует состояние в список данных для адаптера
     */
    private fun eventViewModelState(
        binding: FragmentEventsBinding,
        adapter: EventAdapterDifferentTypesView,
    ) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { eventState: EventState ->
                binding.errorGroup.isVisible = eventState.isEmptyError

                val errorText: CharSequence? =
                    eventState.emptyError?.getErrorText(requireContext())

                binding.errorText.text = errorText

                binding.progressBar.isVisible = eventState.isEmptyLoading

                binding.swiperRefresh.isRefreshing = eventState.isRefreshing

                if (eventState.singleError != null) {
                    val singleErrorText: CharSequence =
                        eventState.singleError.getErrorText(requireContext())

                    requireContext().showTopSnackbar(
                        message = singleErrorText.toString(),
                        iconRes = R.drawable.ic_cross_24,
                        iconTintRes = R.color.error_color
                    )

                    viewModel.accept(message = EventMessage.HandleError)
                }

                adapter.submitList(
                    EventPagingMapper.map(
                        state = eventState,
                        context = requireContext()
                    )
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Прокручивает RecyclerView на самый верх.
     */
    private fun scrollToTop(binding: FragmentEventsBinding) {
        binding.list.smoothScrollToPosition(0)
    }

    /**
     * Прокручивает RecyclerView на самый верх и обновляет данные.
     */
    private fun scrollToTopAndRefresh(binding: FragmentEventsBinding) {
        viewModel.accept(message = EventMessage.Refresh)
        binding.list.smoothScrollToPosition(0)
    }

    /**
     * Показывает диалоговое окно с подтверждением удаления.
     *
     * Эта функция отображает Material 3 диалог с двумя кнопками: "Отмена" и "Удалить".
     * Кнопка "Отмена" закрывает диалог без выполнения каких-либо действий, а кнопка "Удалить"
     * вызывает переданный коллбэк `onDeleteConfirmed`, который выполняет удаление.
     *
     * @param title Заголовок диалога. Обычно это текст, который кратко описывает действие, например, "Удаление поста".
     * @param message Основной текст диалога. Это более подробное описание действия, например, "Вы уверены, что хотите удалить этот пост?".
     * @param onDeleteConfirmed Коллбэк, который вызывается при нажатии на кнопку "Удалить". Этот коллбэк должен содержать логику удаления.
     *
     * @see Context.showMaterialDialogWithTwoButtons Функция, которая используется для отображения диалога с двумя кнопками.
     */
    private fun showDeleteConfirmationDialog(
        title: String,
        message: String,
        onDeleteConfirmed: () -> Unit
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }

    private fun setupOptimizedScrollListener(recyclerView: RecyclerView) {
        var lastScrollState = 0 // 0-неизвестно, 1-вниз, 2-вверх
        val scrollThreshold = 8

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    dy > scrollThreshold && lastScrollState != 1 -> {
                        lastScrollState = 1
                        sharedViewModel.setFabVisibility(false, currentTabPosition)
                    }

                    dy < -scrollThreshold && lastScrollState != 2 -> {
                        lastScrollState = 2
                        sharedViewModel.setFabVisibility(true, currentTabPosition)
                    }
                }
            }
        })
    }
}
