package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.MainActivityBinding

import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.adapter.PostAdapter
import com.eltex.androidschool.data.Post
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.ui.EdgeToEdgeHelper
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
            object : PostAdapter.PostListener {
                override fun onLikeClicled(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClicked(post: Post) {}

                override fun onDeleteClicked(post: Post) {
                    viewModel.deleteById(post.id)
                }
            }
        )

        binding.list.adapter = adapter

        val newPostContracts =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
                activityResult.data?.getStringExtra(Intent.EXTRA_TEXT)?.let { content: String ->
                    viewModel.addPost(content)
                }
            }

        binding.newPost.setOnClickListener {
            newPostContracts.launch(Intent(this, NewPostActivity::class.java))
        }

        // Добавляем декорацию для отступов между элементами списка.
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        // Подписываемся на изменения состояния постов и обновляем адаптер.
        viewModel.state
            .onEach { postState: PostState ->
                adapter.submitList(postState.posts)
            }
            .launchIn(lifecycleScope)

        // Применяем отступы для системных панелей.
        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))
    }
}
