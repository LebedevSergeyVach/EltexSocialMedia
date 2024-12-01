package com.eltex.androidschool.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R

import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostAdapter

import com.eltex.androidschool.data.PostData

import com.eltex.androidschool.databinding.FragmentPostsBinding

import com.eltex.androidschool.db.AppDbPost

import com.eltex.androidschool.repository.DaoSQLitePostRepository

import com.eltex.androidschool.viewmodel.PostState
import com.eltex.androidschool.viewmodel.PostViewModel

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
                    DaoSQLitePostRepository(
                        AppDbPost.getInstance(requireContext()).postDao
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
        val binding = FragmentPostsBinding.inflate(layoutInflater, container, false)

        // Создаем адаптер для списка постов
        val adapter = PostAdapter(
            object : PostAdapter.PostListener {
                override fun onLikeClicked(post: PostData) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClicked(post: PostData) {}

                override fun onDeleteClicked(post: PostData) {
                    viewModel.deleteById(post.id)
                }

                override fun onUpdateClicked(post: PostData) {
                    findNavController()
                        .navigate(
                            R.id.action_postsFragment_to_newOrUpdatePostFragment,
                            bundleOf(
                                NewOrUpdatePostFragment.POST_ID to post.id,
                                NewOrUpdatePostFragment.POST_CONTENT to post.content,
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

        // Подписываемся на изменения состояния постов
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { postState: PostState ->
                adapter.submitList(postState.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
