package com.eltex.androidschool.fragments.jobs

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar

import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.R
import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.databinding.FragmentNewOrUpdateJobBinding
import com.eltex.androidschool.repository.jobs.NetworkJobRepository
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.showMaterialDialog
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.jobs.newjob.NewJobState
import com.eltex.androidschool.viewmodel.jobs.newjob.NewJobViewModel

import com.google.android.material.datepicker.MaterialDatePicker

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Фрагмент для создания или обновления информации о месте работы.
 *
 * Этот фрагмент позволяет пользователю создавать новое место работы или обновлять существующее.
 * Он также управляет отображением и скрытием кнопки сохранения в зависимости от состояния фрагмента.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see NewJobViewModel ViewModel для управления созданием и обновлением мест работы.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class NewOrUpdateJobFragment : Fragment() {

    companion object {
        const val JOB_ID = "JOB_ID"
        const val JOB_NAME = "JOB_NAME"
        const val JOB_POSITION = "JOB_POSITION"
        const val JOB_LINK = "JOB_LINK"
        const val JOB_START = "JOB_START"
        const val JOB_FINISH = "JOB_FINISH"
        const val IS_UPDATE = "IS_UPDATE"
        const val JOB_CREATED_OR_UPDATED_KEY = "JOB_CREATED_OR_UPDATED_KEY"
    }

    private var selectedStartDate: Calendar? = null
    private var selectedFinishDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrUpdateJobBinding.inflate(inflater, container, false)

        val jobId = arguments?.getLong(JOB_ID) ?: 0L
        val companyName = arguments?.getString(JOB_NAME) ?: ""
        val position = arguments?.getString(JOB_POSITION) ?: ""
        val link = arguments?.getString(JOB_LINK) ?: ""
        val start = arguments?.getString(JOB_START) ?: ""
        val finish = arguments?.getString(JOB_FINISH) ?: ""
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) ?: false

        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val newJobViewModel by viewModels<NewJobViewModel> {
            viewModelFactory {
                addInitializer(NewJobViewModel::class) {
                    NewJobViewModel(
                        repository = NetworkJobRepository(),
                        jobId = jobId
                    )
                }
            }
        }

        binding.textCompanyName.editText?.setText(companyName)
        binding.textPosition.editText?.setText(position)
        binding.textLink.editText?.setText(link)

        binding.buttonSelectDateWorkingPeriod.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            showDateRangePicker(binding)
        }

        binding.buttonSelectDateWorkingPeriod.setOnLongClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            displayingDialogWindowWithInformation(
                title = getString(R.string.working_period_card_title),
                message = getString(R.string.working_period_card_description)
            )

            true
        }

        binding.dataEntryCard.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.data_entry_card_title),
                message = getString(R.string.data_entry_card_description)
            )
            true
        }

        binding.cardWorkingPeriod.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.working_period_card_title),
                message = getString(R.string.working_period_card_description)
            )
            true
        }

        binding.cardWorkingPeriod.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35)

            showDateRangePicker(binding)
        }

        if (isUpdate && start.isNotEmpty() && finish.isNotEmpty()) {
            parseDateTime(start, finish)
            updateWorkingPeriodText(binding)
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newCompanyName =
                    binding.textCompanyName.editText?.text?.toString().orEmpty().trim()
                val newPosition = binding.textPosition.editText?.text?.toString().orEmpty().trim()
                val newLink = binding.textLink.editText?.text?.toString().orEmpty().trim()

                val newStartDate = formatDateTimeForServer(selectedStartDate)
                val newFinishDate = formatDateTimeForServer(selectedFinishDate)

                if (
                    newCompanyName.isNotEmpty() && newPosition.isNotEmpty() && newLink.isNotEmpty() && newStartDate.isNotEmpty() && newFinishDate.isNotEmpty()
                ) {
                    newJobViewModel.save(
                        name = newCompanyName,
                        position = newPosition,
                        link = newLink,
                        start = newStartDate,
                        finish = newFinishDate
                    )
                } else {
                    requireContext().toast(R.string.error_text_event_is_empty)
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        newJobViewModel.state
            .onEach { stateNewJob: NewJobState ->
                if (stateNewJob.job != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        JOB_CREATED_OR_UPDATED_KEY,
                        bundleOf()
                    )

                    findNavController().navigateUp()
                }

                stateNewJob.statusJob.throwableOrNull?.getErrorText(requireContext())
                    ?.let { errorText: CharSequence? ->
                        if (errorText == getString(R.string.network_error)) {
                            requireContext().toast(R.string.network_error)

                            newJobViewModel.consumerError()
                        } else if (errorText == getString(R.string.unknown_error)) {
                            requireContext().toast(R.string.unknown_error)

                            newJobViewModel.consumerError()
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

        toolbar.title = if (arguments?.getBoolean(IS_UPDATE, false) == true) {
            getString(R.string.update_job_data)
        } else {
            getString(R.string.new_job)
        }

        return binding.root
    }

    /**
     * Показывает всплывающее диалоговое информационное окно.
     *
     * @param title Заголовок диалога.
     * @param message Основной текст диалога.
     * @param buttonText Текст кнопки (по умолчанию "Спасибо").
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

    /**
     * Показывает диалоговое окно для выбора диапазона дат (start и finish).
     *
     * @param binding Привязка для макета фрагмента, используемая для обновления текста периода работы.
     */
    private fun showDateRangePicker(binding: FragmentNewOrUpdateJobBinding) {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(getString(R.string.select_working_period))
            .setTheme(R.style.CustomDatePicker)
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
            val startDate = Calendar.getInstance().apply {
                timeInMillis = dateRange.first
            }
            val finishDate = Calendar.getInstance().apply {
                timeInMillis = dateRange.second
            }

            selectedStartDate = startDate
            selectedFinishDate = finishDate

            updateWorkingPeriodText(binding)
        }

        dateRangePicker.show(childFragmentManager, "DATE_RANGE_PICKER")
    }

    /**
     * Обновляет текст периода работы в формате "start - finish".
     *
     * @param binding Привязка для макета фрагмента, используемая для обновления текста.
     */
    private fun updateWorkingPeriodText(binding: FragmentNewOrUpdateJobBinding) {
        if (selectedStartDate != null && selectedFinishDate != null) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val startDate = dateFormat.format(selectedStartDate!!.time)
            val finishDate = dateFormat.format(selectedFinishDate!!.time)

            binding.textWorkingPeriod.text = buildString {
                append(startDate)
                append(" - ")
                append(finishDate)
            }
        }
    }

    /**
     * Форматирует дату для отправки на сервер в формате "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     *
     * @param calendar Календарь, содержащий дату для форматирования.
     * @return Строка с датой в формате для сервера или пустая строка, если calendar равен null.
     */
    private fun formatDateTimeForServer(calendar: Calendar?): String {
        return calendar?.let { calendarFormat: Calendar ->
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(calendarFormat.time)
        } ?: ""
    }

    /**
     * Парсит даты start и finish из строки в формате "dd.MM.yy".
     *
     * @param start Строка с датой начала работы.
     * @param finish Строка с датой окончания работы.
     */
    private fun parseDateTime(start: String, finish: String) {
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        try {
            val startDate = dateFormat.parse(start)
            val finishDate = dateFormat.parse(finish)

            startDate?.let {
                selectedStartDate = Calendar.getInstance().apply { time = it }
            }
            finishDate?.let {
                selectedFinishDate = Calendar.getInstance().apply { time = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            if (BuildConfig.DEBUG) {
                Logger.e(e.toString())
            }
        }
    }
}
