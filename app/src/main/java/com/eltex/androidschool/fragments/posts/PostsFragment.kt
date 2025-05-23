package com.eltex.androidschool.fragments.posts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

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
import com.eltex.androidschool.adapter.posts.PostAdapter
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.fragments.comments.CommentsBottomSheetFragment
import com.eltex.androidschool.fragments.users.AccountFragment
import com.eltex.androidschool.ui.offset.OffsetDecoration
import com.eltex.androidschool.ui.posts.PostPagingMapper
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.viewmodel.auth.user.AccountViewModel
import com.eltex.androidschool.viewmodel.common.SharedViewModel
import com.eltex.androidschool.viewmodel.posts.post.PostMessage
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostViewModel

import com.google.android.material.bottomnavigation.BottomNavigationView

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

/**
 * Фрагмент, отображающий список постов.
 *
 * Этот фрагмент отвечает за отображение списка постов, а также за обработку событий, связанных с постами,
 * таких как создание, редактирование и удаление постов.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see PostViewModel ViewModel для управления состоянием постов.
 * @see PostAdapter Адаптер для отображения списка постов.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val accountViewModel: AccountViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var currentTabPosition = 0 // Установите правильную позицию таба

    /**
     * ViewModel для управления состоянием постов.
     *
     * @see PostViewModel
     */
    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(layoutInflater, container, false)

        val adapter: PostAdapter = createPostAdapter(
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
                        viewModel.accept(message = PostMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )

        setupOptimizedScrollListener(binding.list)

        binding.retryButton.setOnClickListener {
            viewModel.accept(message = PostMessage.Refresh)
        }

        binding.swiperRefresh.setOnRefreshListener {
            viewModel.accept(message = PostMessage.Refresh)
            requireContext().singleVibrationWithSystemCheck(35)
        }

        binding.swiperRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.active_element)
        )

        binding.swiperRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(requireContext(), R.color.background_color_of_the_refresh_circle)
        )

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdatePostFragment.POST_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            scrollToTopAndRefresh(binding = binding)
        }

        postViewModelState(binding = binding, adapter = adapter)

        return binding.root
    }

    /**
     * Создает и возвращает экземпляр [PostAdapter], который используется для отображения списка постов.
     * Адаптер настраивается с помощью [PostListener], который обрабатывает различные действия пользователя,
     * такие как лайк, удаление, обновление и переход к профилю пользователя.
     *
     * @return [PostAdapter] - адаптер для отображения постов.
     *
     * @see [PostAdapter] - класс адаптера, который управляет отображением списка постов.
     * @see PostAdapter.PostListener - интерфейс, который определяет методы для обработки действий пользователя.
     */
    private fun createPostAdapter(
        accountViewModel: AccountViewModel,
    ): PostAdapter = PostAdapter(
        listener = object : PostAdapter.PostListener {
            override fun onLikeClicked(post: PostUiModel) {
                viewModel.accept(message = PostMessage.Like(post = post))
            }

            override fun onShareClicked(post: PostUiModel) {}

            override fun onDeleteClicked(post: PostUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_post_title),
                    message = getString(R.string.delete_post_message)
                ) {
                    viewModel.accept(message = PostMessage.Delete(post = post))
                }
            }

            override fun onUpdateClicked(post: PostUiModel) {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(
                        R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment,
                        bundleOf(
                            NewOrUpdatePostFragment.POST_ID to post.id,
                            NewOrUpdatePostFragment.POST_CONTENT to post.content,
                            NewOrUpdatePostFragment.IS_UPDATE to true,
                        ),
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    )
            }

            override fun onGetUserClicked(post: PostUiModel) {
                if (post.authorId == accountViewModel.userId) {
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
                                AccountFragment.USER_ID to post.authorId,
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

            override fun onCommentsClicked(post: PostUiModel) {
                val commentsBottomSheetFragment = CommentsBottomSheetFragment(
                    postId = post.id,
                    accountUserId = accountViewModel.userId,
                    isAccount = false,
                    isProfile = false,
                )

                commentsBottomSheetFragment.show(
                    parentFragmentManager,
                    commentsBottomSheetFragment.tag
                )
            }

            override fun onGetPostDetailsClicked(post: PostUiModel) {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(
                        R.id.action_BottomNavigationFragment_to_fragmentPostDetails,
                        bundleOf(
                            FragmentPostDetails.POST_ID to post.id,
                            FragmentPostDetails.ACCOUNT_ID to accountViewModel.userId,
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
                viewModel.accept(PostMessage.Retry)
            }
        },

        context = requireContext(),
        currentUserId = accountViewModel.userId,
    )

    /**
     * Настраивает наблюдение за состоянием [ViewModel] и обновляет UI в соответствии с текущим состоянием.
     * Метод связывает [PostState] с элементами UI, такими как [ProgressBar], [SwipeRefreshLayout] и [RecyclerView].
     *
     * @param binding [FragmentPostsBinding] - объект binding, который предоставляет доступ к элементам UI.
     * @param adapter [PostAdapter] - адаптер, который управляет отображением списка постов.
     *
     * @see [PostState] - класс, который представляет состояние экрана с постами.
     * @see [PostMessage] - класс, который представляет сообщения, отправляемые во ViewModel.
     * @see [PostPagingMapper] - класс, который преобразует состояние в список данных для адаптера
     */
    private fun postViewModelState(
        binding: FragmentPostsBinding,
        adapter: PostAdapter,
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .onEach { postState: PostState ->
                binding.errorGroup.isVisible = postState.isEmptyError

                val errorText: CharSequence? =
                    postState.emptyError?.getErrorText(requireContext())

                binding.errorText.text = errorText

                binding.progressBar.isVisible = postState.isEmptyLoading

                binding.swiperRefresh.isRefreshing = postState.isRefreshing

                if (postState.singleError != null) {
                    val singleErrorText =
                        postState.singleError.getErrorText(requireContext())

                    requireContext().showTopSnackbar(
                        message = singleErrorText.toString(),
                        iconRes = R.drawable.ic_cross_24,
                        iconTintRes = R.color.error_color
                    )

                    viewModel.accept(message = PostMessage.HandleError)
                }

                adapter.submitList(
                    PostPagingMapper.map(
                        state = postState,
                        context = requireContext()
                    )
                )
            }
            .launchIn(scope = viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Прокручивает RecyclerView на самый верх и обновляет данные.
     */
    private fun scrollToTopAndRefresh(binding: FragmentPostsBinding) {
        viewModel.accept(message = PostMessage.Refresh)
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
        onDeleteConfirmed: () -> Unit,
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            onDeleteConfirmed = onDeleteConfirmed,
        )
    }

    private fun transparency() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.setBackgroundColor(Color.TRANSPARENT)

        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setBackgroundColor(Color.TRANSPARENT)
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
