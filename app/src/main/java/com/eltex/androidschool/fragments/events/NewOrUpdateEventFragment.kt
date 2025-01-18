package com.eltex.androidschool.fragments.events

import android.os.Bundle

import androidx.core.os.bundleOf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.BuildConfig

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Locale

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentNewOrUpdateEventBinding

import com.eltex.androidschool.repository.events.NetworkEventRepository

import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.showMaterialDialog
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.utils.vibrateWithEffect

import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.events.newevent.NewEventState
import com.eltex.androidschool.viewmodel.events.newevent.NewEventViewModel

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Фрагмент для создания или обновления события.
 *
 * Этот фрагмент позволяет пользователю создавать новое событие или обновлять существующее.
 * Он также управляет отображением и скрытием кнопки сохранения в зависимости от состояния фрагмента.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see NewEventViewModel ViewModel для управления созданием и обновлением событий.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class NewOrUpdateEventFragment : Fragment() {

    companion object {
        const val EVENT_ID = "EVENT_ID"
        const val EVENT_CONTENT = "EVENT_CONTENT"
        const val EVENT_LINK = "EVENT_LINK"
        const val EVENT_DATE = "EVENT_DATE"
        const val EVENT_OPTION = "EVENT_OPTION"
        const val IS_UPDATE = "IS_UPDATE"
        const val EVENT_CREATED_OR_UPDATED_KEY = "EVENT_CREATED_OR_UPDATED_KEY"
        private const val ONLINE: String = "ONLINE"
        private const val OFFLINE: String = "OFFLINE"
    }

    private var selectedDate: Calendar? = null
    private var selectedTime: Calendar? = null

    /**
     * Создает и возвращает представление для этого фрагмента.
     *
     * @param inflater Объект, который может преобразовать XML-файл макета в View-объекты.
     * @param container Родительский ViewGroup, в который будет добавлено представление.
     * @param savedInstanceState Сохраненное состояние фрагмента, если оно есть.
     * @return View, представляющий собой корневой элемент макета этого фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrUpdateEventBinding.inflate(inflater, container, false)

        /**
         * Получаем ViewModel для управления состоянием панели инструментов
         *
         * @see ToolBarViewModel
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val eventId = arguments?.getLong(EVENT_ID) ?: 0L
        val content = arguments?.getString(EVENT_CONTENT) ?: ""
        val link = arguments?.getString(EVENT_LINK) ?: ""
        val date = arguments?.getString(EVENT_DATE) ?: ""
        val option = arguments?.getString(EVENT_OPTION) ?: ONLINE
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) ?: false

        binding.content.setText(content)
        binding.link.setText(link)
        binding.optionSwitch.isChecked = option == ONLINE

        val eventWillTake: String = getString(R.string.event_take_will) + " "

        binding.optionText.text =
            if (binding.optionSwitch.isChecked) {
                eventWillTake + getString(R.string.online)
            } else {
                eventWillTake + getString(R.string.offline)
            }

        /**
         * Получаем ViewModel для управления созданием и обновлением событий
         *
         *  @see NewEventViewModel
         */
        val newEventViewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                addInitializer(
                    NewEventViewModel::class
                ) {
                    NewEventViewModel(
                        repository = NetworkEventRepository(),
                        eventId = eventId
                    )
                }
            }
        }

        binding.optionSwitch.setOnCheckedChangeListener { _, isChecked ->
            requireContext().singleVibrationWithSystemCheck(35)

            binding.link.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.link.hint =
                        if (isChecked) {
                            getString(R.string.new_event_link_hint)
                        } else {
                            getString(R.string.new_event_address_hint)
                        }

                    binding.link.animate().alpha(1f).setDuration(200).start()
                }
                .start()

            binding.optionText.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.optionText.text =
                        if (isChecked) {
                            eventWillTake + getString(R.string.online)
                        } else {
                            eventWillTake + getString(R.string.offline)
                        }

                    binding.optionText.animate().alpha(1f).setDuration(200).start()
                }
                .start()
        }

        binding.cardOption.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            binding.optionSwitch.isChecked = !binding.optionSwitch.isChecked
        }

        binding.selectDateTimeButton.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            showDatePicker(binding)
        }

        binding.cardData.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            showDatePicker(binding)
        }

        binding.cardOption.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.option_switch_title),
                message = getString(R.string.option_switch_description),
            )
            true
        }

        binding.optionSwitch.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.option_switch_title),
                message = getString(R.string.option_switch_description),
            )
            true
        }

        binding.cardData.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.date_picker_title),
                message = getString(R.string.date_picker_description),
            )
            true
        }

        binding.selectDateTimeButton.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.date_picker_title),
                message = getString(R.string.date_picker_description),
            )
            true
        }

        if (isUpdate && date.isNotEmpty()) {
            parseDateTime(date)
            updateDateTimeText(binding)
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty().trimStart().trimEnd()
                val newLink = binding.link.text?.toString().orEmpty().trimStart().trimEnd()

                val newDate = formatDateTimeForServer()

                val newOption = if (binding.optionSwitch.isChecked) ONLINE else OFFLINE

                if (
                    newContent.isNotEmpty() && newDate.isNotEmpty() &&
                    newOption.isNotEmpty() && newLink.isNotEmpty()
                ) {
                    newEventViewModel.save(
                        content = newContent,
                        link = newLink,
                        option = newOption,
                        data = newDate,
                    )
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().toast(R.string.error_text_event_is_empty)
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        newEventViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { newEventState: NewEventState ->
                if (newEventState.event != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_CREATED_OR_UPDATED_KEY,
                        bundleOf()
                    )

                    findNavController().navigateUp()
                }

                newEventState.statusEvent.throwableOrNull?.getErrorText(requireContext())
                    ?.let { errorText: CharSequence? ->
                        if (errorText == getString(R.string.network_error)) {
                            requireContext().toast(R.string.network_error)

                            newEventViewModel.consumerError()
                        } else if (errorText == getString(R.string.unknown_error)) {
                            requireContext().toast(R.string.unknown_error)

                            newEventViewModel.consumerError()
                        }
                    }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(true)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> {
                            Unit
                        }
                    }
                }
            }
        )

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)

        toolbar.title =
            if (isUpdate) getString(R.string.update_event_title) else getString(R.string.new_event_title)

        return binding.root
    }

    /**
     * Показывает всплывающее окно для выбора даты.
     *
     * Этот метод создает и отображает MaterialDatePicker, который позволяет пользователю выбрать дату.
     * После выбора даты, метод сохраняет выбранную дату в переменную [selectedDate] и вызывает метод [showTimePicker]
     * для выбора времени.
     *
     * @param binding Объект [FragmentNewOrUpdateEventBinding], используемый для доступа к элементам UI.
     */
    private fun showDatePicker(binding: FragmentNewOrUpdateEventBinding) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date)
            .setTheme(R.style.CustomDatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            selectedDate = calendar

            showTimePicker(binding)
        }

        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    /**
     * Показывает всплывающее окно для выбора времени.
     *
     * Этот метод создает и отображает MaterialTimePicker, который позволяет пользователю выбрать время.
     * После выбора времени, метод сохраняет выбранное время в переменную [selectedTime] и вызывает метод [updateDateTimeText]
     * для обновления текста даты и времени в UI.
     *
     * @param binding Объект [FragmentNewOrUpdateEventBinding], используемый для доступа к элементам UI.
     */
    private fun showTimePicker(binding: FragmentNewOrUpdateEventBinding) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(R.string.select_time)
            .setTheme(R.style.CustomTimePicker)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
                set(Calendar.SECOND, 0)
            }

            updateDateTimeText(binding)
        }

        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    /**
     * Обновляет текст даты и времени в UI.
     *
     * Этот метод объединяет выбранную дату и время в одну строку в формате "yyyy-MM-dd HH:mm:ss"
     * и устанавливает эту строку в TextView [binding].
     *
     * @param binding Объект [FragmentNewOrUpdateEventBinding], используемый для доступа к элементам UI.
     */
    private fun updateDateTimeText(binding: FragmentNewOrUpdateEventBinding) {
        if (selectedDate != null && selectedTime != null) {
            val combinedCalendar = Calendar.getInstance().apply {
                timeInMillis = selectedDate!!.timeInMillis

                set(Calendar.HOUR_OF_DAY, selectedTime!!.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedTime!!.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }

            val dateTimeFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val dateTimeString = dateTimeFormat.format(combinedCalendar.time)

            binding.dataText.text = dateTimeString
        }
    }

    /**
     * Форматирует дату и время для отправки на сервер в формате "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     *
     * @return Строка с датой и временем в формате для сервера.
     */
    private fun formatDateTimeForServer(): String {
        if (selectedDate != null && selectedTime != null) {
            val combinedCalendar = Calendar.getInstance().apply {
                timeInMillis = selectedDate!!.timeInMillis

                set(Calendar.HOUR_OF_DAY, selectedTime!!.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedTime!!.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }

            val instant = combinedCalendar.toInstant().atOffset(ZoneOffset.UTC)
            val dateTimeFormat =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)

            return dateTimeFormat.format(instant)
        }

        return ""
    }

    /**
     * Парсит дату и время из строки в формате "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" и сохраняет их в [selectedDate] и [selectedTime].
     *
     * @param dateTimeString Строка с датой и временем в формате "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     */
    private fun parseDateTime(dateTimeString: String) {
        val dateTimeFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
        try {
            val dateTime = dateTimeFormat.parse(dateTimeString)
            dateTime?.let {
                selectedDate = Calendar.getInstance().apply { time = it }
                selectedTime = Calendar.getInstance().apply { time = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            if (BuildConfig.DEBUG) {
                Logger.e(e.toString())
            }
        }
    }

    /**
     * Показывает всплывающее диалоговое информационное окно.
     *
     * @param title - Заголовок диалога.
     * @param message - Основной текст диалога.
     * @param buttonText - Текст кнопки (по умолчанию "Спасибо").
     */
    private fun displayingDialogWindowWithInformation(
        title: String,
        message: String,
        buttonText: String = getString(R.string.thanks)
    ) {
        requireContext().showMaterialDialog(
            title = title,
            message = message,
            buttonText = buttonText
        )
    }
}
