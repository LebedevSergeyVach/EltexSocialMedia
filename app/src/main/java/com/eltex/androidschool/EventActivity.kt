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

/**
 * Основная активность приложения, отображающая список событий.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 * @see EventViewModel ViewModel для управления состоянием событий.
 * @see EventAdapter Адаптер для отображения списка событий.
 * @see OffsetDecoration Декорация для добавления отступов между элементами RecyclerView.
 */
class EventActivity : AppCompatActivity() {

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
        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                addInitializer(EventViewModel::class) {
                    EventViewModel(InMemoryEventRepository())
                }
            }
        }

        // Создаем и настраиваем binding для макета активности.
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Создаем и настраиваем адаптер для списка событий.
        val adapter = EventAdapter(
            likeClickListener = { event: Event ->
                viewModel.likeById(event.id)
            },
            participateClickListener = { event: Event ->
                viewModel.participateById(event.id)
            }
        )

        binding.root.adapter = adapter

        // Добавляем декорацию для отступов между элементами списка.
        binding.root.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        // Подписываемся на изменения состояния событий и обновляем адаптер.
        viewModel.state
            .onEach { eventState: EventState ->
                adapter.submitList(eventState.events)
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
