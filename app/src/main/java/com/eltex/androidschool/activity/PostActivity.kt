package com.eltex.androidschool

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels

import com.eltex.androidschool.databinding.MainActivityBinding

import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostAdapter
import com.eltex.androidschool.data.Post
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.viewmodel.PostState
import com.eltex.androidschool.viewmodel.PostViewModel

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Основная активность приложения, отображающая список постов.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 * @see PostViewModel ViewModel для управления состоянием постов.
 * @see PostAdapter Адаптер для отображения списка постов.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
class PostActivity : AppCompatActivity() {

    /**
     * Вызывается при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Включаем режим "от края до края" для активности.
        enableEdgeToEdge()

        // Создаем ViewModel с использованием фабрики.
        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                addInitializer(PostViewModel::class) {
                    PostViewModel(InMemoryPostRepository())
                }
            }
        }

        // Создаем и настраиваем binding для макета активности.
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Создаем и настраиваем адаптер для списка постов.
        val adapter = PostAdapter(
            likeClickListener = { post: Post ->
                viewModel.likeById(post.id)
            },
            shareClickListener = {}
        )

        binding.root.adapter = adapter

        // Добавляем декорацию для отступов между элементами списка.
        binding.root.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        // Подписываемся на изменения состояния постов и обновляем адаптер.
        viewModel.state
            .onEach { postState: PostState ->
                adapter.submitList(postState.posts)
            }
            .launchIn(lifecycleScope)

        // Применяем отступы для системных панелей.
        applyInsets()
    }

    /**
     * Применяет отступы для системных панелей (навигации, статуса).
     *
     * @see ViewCompat.setOnApplyWindowInsetsListener Устанавливает слушатель для применения отступов.
     */
    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}
