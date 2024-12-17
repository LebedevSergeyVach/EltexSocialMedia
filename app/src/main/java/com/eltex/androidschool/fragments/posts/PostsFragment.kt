package com.eltex.androidschool.fragments.posts

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentPostsBinding

import com.eltex.androidschool.ui.common.OffsetDecoration

import com.eltex.androidschool.adapter.posts.PostAdapter

import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.ui.posts.PostUiModel

import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.toast

import com.eltex.androidschool.viewmodel.posts.PostState
import com.eltex.androidschool.viewmodel.posts.PostViewModel

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
                    NetworkPostRepository()
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
        val binding = FragmentPostsBinding.inflate(layoutInflater, container, false)

        val adapter = PostAdapter(
            object : PostAdapter.PostListener {
                override fun onLikeClicked(post: PostUiModel) {
                    viewModel.likeById(post.id, post.likedByMe)
                }

                override fun onShareClicked(post: PostUiModel) {}

                override fun onDeleteClicked(post: PostUiModel) {
                    viewModel.deleteById(post.id)
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

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdatePostFragment.POST_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            viewModel.load()
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { postState: PostState ->
                binding.errorGroup.isVisible = postState.isEmptyError

                val errorText: CharSequence? =
                    postState.statusPost.throwableOrNull?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progressBar.isVisible = postState.isEmptyLoading

                binding.swiperRefresh.isRefreshing = postState.isRefreshing

                if (postState.isRefreshError && errorText == getString(R.string.network_error)) {
                    requireContext().toast(R.string.network_error)

                    viewModel.consumerError()
                } else if (postState.isRefreshError && errorText == getString(R.string.unknown_error)) {
                    requireContext().toast(R.string.unknown_error)

                    viewModel.consumerError()
                }

                adapter.submitList(postState.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
