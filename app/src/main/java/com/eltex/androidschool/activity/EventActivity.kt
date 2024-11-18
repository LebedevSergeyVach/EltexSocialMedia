package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.MainActivityBinding

import com.eltex.androidschool.adapter.EventAdapter
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.data.Event
import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.viewmodel.EventViewModel
import com.eltex.androidschool.viewmodel.EventState

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Основная активность приложения, отображающая список событий.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 * @see EventViewModel ViewModel для управления состоянием событий.
 * @see EventAdapter Адаптер для отображения списка событий.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
@Suppress("DEPRECATION")
class EventActivity : AppCompatActivity() {
    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(InMemoryEventRepository())
            }
        }
    }

    private val newEventContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<EventData>("eventData")
                ?.let { eventData: EventData ->
                    viewModel.addEvent(
                        eventData.content,
                        eventData.link,
                        eventData.option,
                        eventData.date
                    )
                }
        }

    private val editEventContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<EventData>("eventData")
                ?.let { eventData: EventData ->
                    viewModel.updateById(
                        eventData.eventId,
                        eventData.content,
                        eventData.link,
                        eventData.option,
                        eventData.date
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
                val newEventIntent = Intent(this, NewOrUpdateEventActivity::class.java).apply {
                    putExtra("eventData", EventData(text, "", "", "", -1L))
                }
                newEventContracts.launch(newEventIntent)
            }
        }

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = EventAdapter(
            object : EventAdapter.EventListener {
                override fun onLikeClicked(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClicked(event: Event) {}

                override fun onParticipateClicked(event: Event) {
                    viewModel.participateById(event.id)
                }

                override fun onDeleteClicked(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onUpdateClicked(event: Event) {
                    val intent =
                        Intent(this@EventActivity, NewOrUpdateEventActivity::class.java).apply {
                            putExtra(
                                "eventData",
                                EventData(
                                    event.content,
                                    event.dataEvent,
                                    event.optionConducting,
                                    event.link,
                                    event.id,
                                ),
                            )
                        }

                    editEventContracts.launch(intent)
                }
            },

            context = this
        )

        binding.list.adapter = adapter

        binding.newPost.setOnClickListener {
            newEventContracts.launch(Intent(this, NewOrUpdateEventActivity::class.java))
        }

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        viewModel.state
            .onEach { eventState: EventState ->
                adapter.submitList(eventState.events)
            }
            .launchIn(lifecycleScope)

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))
    }
}
