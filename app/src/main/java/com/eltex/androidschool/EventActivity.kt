package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

import com.eltex.androidschool.databinding.MainActivityBinding

import com.eltex.androidschool.adapter.EventAdapter
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.data.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import com.eltex.androidschool.viewmodel.EventState

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventActivity : AppCompatActivity() {
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

        val binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = EventAdapter(
            likeClickListener = { event: Event ->
                viewModel.likeById(event.id)
            },
            participateClickListener = { event: Event ->
                viewModel.participateById(event.id)
            }
        )

        binding.root.adapter = adapter

        binding.root.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        viewModel.state
            .onEach { eventState: EventState ->
                adapter.submitList(eventState.events)
            }
            .launchIn(lifecycleScope)

        applyInsets()
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}
