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
import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.data.EventDataParcelable
import com.eltex.androidschool.repository.LocalPreferencesDataStoreJsonEventRepository
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
                EventViewModel(LocalPreferencesDataStoreJsonEventRepository(applicationContext))
            }
        }
    }

    private val newEventContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<EventDataParcelable>("EventDataParcelable")
                ?.let { eventDataParcelable: EventDataParcelable ->
                    viewModel.addEvent(
                        content = eventDataParcelable.content,
                        link = eventDataParcelable.link,
                        option = eventDataParcelable.option,
                        data = eventDataParcelable.date
                    )
                }
        }

    private val editEventContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
            activityResult.data?.getParcelableExtra<EventDataParcelable>("EventDataParcelable")
                ?.let { eventDataParcelable: EventDataParcelable ->
                    viewModel.updateById(
                        eventId = eventDataParcelable.eventId,
                        content = eventDataParcelable.content,
                        link = eventDataParcelable.link,
                        option = eventDataParcelable.option,
                        data = eventDataParcelable.date
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
                    putExtra(
                        "EventDataParcelable",
                        EventDataParcelable(
                            content = text,
                            date = "",
                            option = "",
                            link = "",
                            eventId = -1L
                        )
                    )
                }
                newEventContracts.launch(newEventIntent)
            }
        }

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = EventAdapter(
            object : EventAdapter.EventListener {
                override fun onLikeClicked(event: EventData) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClicked(event: EventData) {}

                override fun onParticipateClicked(event: EventData) {
                    viewModel.participateById(event.id)
                }

                override fun onDeleteClicked(event: EventData) {
                    viewModel.deleteById(event.id)
                }

                override fun onUpdateClicked(event: EventData) {
                    val intent =
                        Intent(this@EventActivity, NewOrUpdateEventActivity::class.java).apply {
                            putExtra(
                                "EventDataParcelable",
                                EventDataParcelable(
                                    content = event.content,
                                    date = event.dataEvent,
                                    option = event.optionConducting,
                                    link = event.link,
                                    eventId = event.id,
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
