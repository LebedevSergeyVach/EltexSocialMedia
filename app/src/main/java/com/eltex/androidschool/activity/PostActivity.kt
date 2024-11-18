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
import com.eltex.androidschool.data.PostDataParcelable
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.viewmodel.PostState
import com.eltex.androidschool.viewmodel.PostViewModel

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Основная активность приложения, отображающая список постов.
 *
 * Эта активность отвечает за отображение списка постов, а также за обработку событий, связанных с постами,
 * таких как создание, редактирование и удаление постов.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 * @see PostViewModel ViewModel для управления состоянием постов.
 * @see PostAdapter Адаптер для отображения списка постов.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
@Suppress("DEPRECATION")
class PostActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(InMemoryPostRepository())
            }
        }
    }

    private val newPostContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<PostDataParcelable>("PostDataParcelable")
                ?.let { postDataParcelable: PostDataParcelable ->
                    viewModel.addPost(
                        content = postDataParcelable.content,
                    )
                }
        }

    private val editPostContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<PostDataParcelable>("PostDataParcelable")
                ?.let { postDataParcelable: PostDataParcelable ->
                    viewModel.updateById(
                        postId = postDataParcelable.postId,
                        content = postDataParcelable.content
                    )
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (intent.action == Intent.ACTION_SEND) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            intent.removeExtra(Intent.EXTRA_TEXT)
            if (text != null) {
                val newPostIntent = Intent(this, NewOrUpdatePostActivity::class.java).apply {
                    putExtra(
                        "PostDataParcelable",
                        PostDataParcelable(
                            content = text,
                            postId = -1L
                        )
                    )
                }
                newPostContracts.launch(newPostIntent)
            }
        }

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(
            object : PostAdapter.PostListener {
                override fun onLikeClicked(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClicked(post: Post) {}

                override fun onDeleteClicked(post: Post) {
                    viewModel.deleteById(post.id)
                }

                override fun onUpdateClicked(post: Post) {
                    val intent =
                        Intent(this@PostActivity, NewOrUpdatePostActivity::class.java).apply {
                            putExtra(
                                "PostDataParcelable",
                                PostDataParcelable(
                                    content = post.content,
                                    postId = post.id
                                )
                            )
                        }

                    editPostContracts.launch(intent)
                }
            },

            context = this
        )

        binding.list.adapter = adapter

        binding.newPost.setOnClickListener {
            newPostContracts.launch(Intent(this, NewOrUpdatePostActivity::class.java))
        }

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        viewModel.state
            .onEach { postState: PostState ->
                adapter.submitList(postState.posts)
            }
            .launchIn(lifecycleScope)

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))
    }
}
