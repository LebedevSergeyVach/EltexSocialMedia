package com.eltex.androidschool.fragments.posts

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.BuildConfig

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentPostsBinding

import com.eltex.androidschool.ui.common.OffsetDecoration

import com.eltex.androidschool.adapter.posts.PostAdapter
import com.eltex.androidschool.effecthandler.posts.PostEffectHandler
import com.eltex.androidschool.fragments.users.UserFragment
import com.eltex.androidschool.reducer.posts.PostReducer

import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.ui.posts.PostPagingMapper

import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper

import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.posts.post.PostMessage

import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStore
import com.eltex.androidschool.viewmodel.posts.post.PostViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

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
class PostsFragment : Fragment() {

    /**
     * ViewModel для управления состоянием постов.
     *
     * @see PostViewModel
     */
    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    postStore = PostStore(
                        reducer = PostReducer(),
                        effectHandler = PostEffectHandler(
                            repository = NetworkPostRepository(),
                            mapper = PostUiModelMapper()
                        ),
                        initMessages = setOf(PostMessage.Refresh),
                        initState = PostState(),
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(layoutInflater, container, false)

        val adapter = PostAdapter(
            object : PostAdapter.PostListener {
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
                    if (post.authorId == BuildConfig.USER_ID) {
                        val bottomNav = requireParentFragment().requireParentFragment()
                            .requireView().findViewById<BottomNavigationView>(R.id.bottomNavigation)

                        bottomNav.selectedItemId = R.id.userFragment

                        findNavController().navigate(
                            R.id.userFragment,
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
                                    UserFragment.USER_ID to post.authorId,
                                    UserFragment.IC_PROFILE to false
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

                override fun onRetryPageClicked() {
                    viewModel.accept(PostMessage.Retry)
                }
            },

            context = requireContext(),
            currentUserId = BuildConfig.USER_ID
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

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
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

                    requireContext().toast(singleErrorText.toString())

                    viewModel.accept(message = PostMessage.HandleError)
                }

                adapter.submitList(
                    PostPagingMapper.map(
                        state = postState,
                        context = requireContext()
                    )
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    /**
     * Прокручивает RecyclerView на самый верх.
     */
    private fun scrollToTo2p(binding: FragmentPostsBinding) {
        binding.list.smoothScrollToPosition(0)
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
        onDeleteConfirmed: () -> Unit
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }
}
