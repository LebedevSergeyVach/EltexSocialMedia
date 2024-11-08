package com.eltex.androidschool

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

import com.eltex.androidschool.databinding.PostCardOption1Binding

import com.eltex.androidschool.data.Post
import com.eltex.androidschool.repository.InMemoryPostRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                addInitializer(PostViewModel::class) {
                    PostViewModel(InMemoryPostRepository())
                }
            }
        }

        val binding = PostCardOption1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.state
            .onEach { postState ->
                bindingPost(binding, postState.post)
            }
            .launchIn(lifecycleScope)


        binding.like.setOnClickListener {
            viewModel.like()
        }

        applyInsets()
    }

    @SuppressLint("SetTextI18n")
    private fun bindingPost(
        binding: PostCardOption1Binding, post: Post
    ) {
        binding.author.text = post.author
        binding.content.text = post.content
        binding.published.text = post.published
        binding.initial.text = post.author.take(1)

        binding.menu.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented, false)
        }

        binding.like.setIconResource(
            if (post.likeByMe) {
                R.drawable.ic_favorite_24
            } else {
                R.drawable.ic_favorite_border_24
            }
        )
        binding.like.text = if (post.likeByMe) {
            1
        } else {
            0
        }.toString()
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}
