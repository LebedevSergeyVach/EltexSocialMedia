/*
package com.eltex.androidschool.development.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.eltex.androidschool.R
import com.eltex.androidschool.development.activity.NewOrUpdateEventActivity

import com.eltex.androidschool.databinding.MainActivityBinding

import com.eltex.androidschool.adapter.events.EventAdapter
import com.eltex.androidschool.ui.OffsetDecoration
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.development.data.EventDataParcelable
import com.eltex.androidschool.db.events.AppDbEvent
import com.eltex.androidschool.repository.events.DaoSQLiteEventRepository
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.viewmodel.events.EventViewModel
import com.eltex.androidschool.viewmodel.events.EventState

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

*/
/**
 * Основная активность приложения, отображающая список событий.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 * @see EventViewModel ViewModel для управления состоянием событий.
 * @see EventAdapter Адаптер для отображения списка событий.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 *//*

@Suppress("DEPRECATION")
class EventActivity : AppCompatActivity() {
    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    DaoSQLiteEventRepository(
                        AppDbEvent.getInstance(applicationContext).eventDao
                    )
                )
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

        handleIntent(intent)

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

            context = applicationContext
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            val uri = intent.data
            intent.removeExtra(Intent.EXTRA_TEXT)

            if (text != null) {
                createEventFromText(text)
            } else if (uri != null) {
                createEventFromUri(uri)
            }
        } else if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri != null) {
                createEventFromUri(uri)
            }
        }
    }

    private fun createEventFromText(text: String) {
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

    private fun createEventFromUri(uri: Uri) {
        val newEventIntent = Intent(this, NewOrUpdateEventActivity::class.java).apply {
            putExtra(
                "EventDataParcelable",
                EventDataParcelable(
                    content = "",
                    date = "",
                    option = "",
                    link = uri.toString(),
                    eventId = -1L
                )
            )
        }
        newEventContracts.launch(newEventIntent)
    }
}
*/
