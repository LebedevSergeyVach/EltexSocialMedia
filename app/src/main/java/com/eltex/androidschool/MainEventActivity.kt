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

import com.eltex.androidschool.databinding.EventCardOption1Binding

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import com.eltex.androidschool.utils.toast

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                addInitializer(EventViewModel::class) {
                    EventViewModel(InMemoryEventRepository())
                }
            }
        }

        val binding = EventCardOption1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.state
            .onEach { eventState ->
                bindingEvent(binding, eventState.event)
            }
            .launchIn(lifecycleScope)


        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.participate.setOnClickListener {
            viewModel.participate()
        }

        applyInsets()
    }

    @SuppressLint("SetTextI18n")
    private fun bindingEvent(
        binding: EventCardOption1Binding, event: Event
    ) {
        binding.author.text = event.author
        binding.initial.text = event.author.take(1)
        binding.published.text = event.published
        binding.optionConducting.text = event.optionConducting
        binding.dataEvent.text = event.dataEvent
        binding.content.text = event.content
        binding.link.text = event.link

        binding.menu.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented, false)
        }

        binding.like.setIconResource(
            if (event.likeByMe) {
                R.drawable.ic_favorite_24
            } else {
                R.drawable.ic_favorite_border_24
            }
        )

        binding.like.text = if (event.likeByMe) {
            1
        } else {
            0
        }.toString()

        binding.participate.setIconResource(
            if (event.participateByMe) {
                R.drawable.ic_participate_24
            } else {
                R.drawable.ic_participate_border_24
            }
        )

        binding.participate.text = if (event.participateByMe) {
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
