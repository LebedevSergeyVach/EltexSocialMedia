package com.eltex.androidschool.fragments.events

import android.graphics.drawable.Drawable

import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly

import androidx.appcompat.widget.Toolbar

import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.data.common.AttachmentTypeFile
import com.eltex.androidschool.databinding.FragmentNewOrUpdateEventBinding
import com.eltex.androidschool.databinding.FragmentNewOrUpdatePostBinding
import com.eltex.androidschool.fragments.common.SettingsBottomSheetFragment
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.showMaterialDialog
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.vibrateWithEffect
import com.eltex.androidschool.utils.helper.ImageHelper
import com.eltex.androidschool.utils.helper.LoggerHelper
import com.eltex.androidschool.data.media.FileModel
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.events.newevent.NewEventState
import com.eltex.androidschool.viewmodel.events.newevent.NewEventViewModel
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostViewModel

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.io.File
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
@AndroidEntryPoint
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

    /**
     * Флаг, указывающий, включено ли сжатие изображений в приложении.
     * По умолчанию установлен в `true`, что означает, что сжатие изображений включено.
     *
     * @property isCompressionEnabled Состояние сжатия изображений (включено/выключено).
     */
    private var isCompressionEnabled = true

    /**
     * Переменная, хранящая выбранную пользователем дату.
     * Если дата не выбрана, значение равно `null`.
     *
     * @property selectedDate Календарная дата, выбранная пользователем.
     * @see Calendar
     */
    private var selectedDate: Calendar? = null

    /**
     * Переменная, хранящая выбранное пользователем время.
     * Если время не выбрано, значение равно `null`.
     *
     * @property selectedTime Календарное время, выбранное пользователем.
     * @see Calendar
     */
    private var selectedTime: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrUpdateEventBinding.inflate(inflater, container, false)

        /**
         * Экземпляр класса `ImageHelper`, используемый для работы с изображениями.
         * Инициализируется с контекстом текущего фрагмента или активности.
         *
         * @property imageHelper Вспомогательный объект для работы с изображениями.
         * @see ImageHelper
         */
        val imageHelper = ImageHelper(requireContext())

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
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) == true

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

        binding.buttonOpenSettings.setOnClickListener {
            showSettingsBottomSheet()
        }

        /**
         * Получаем ViewModel для управления созданием и обновлением событий
         *
         *  @see NewEventViewModel
         */
        val newEventViewModel by viewModels<NewEventViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<NewEventViewModel.ViewModelFactory> { factory ->
                    factory.create(eventId = eventId)
                }
            }
        )

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

        binding.cardDate.setOnClickListener {
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

        binding.cardDate.setOnLongClickListener {
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

        /**
         * URI для временного хранения фотографии, которая будет прикреплена к посту.
         *
         * @see createPhotoUri
         */
        val photoUri: Uri = imageHelper.createPhotoUri()

        /**
         * Контракт для запуска активности съемки фотографии.
         *
         * После успешного завершения съемки фотографии, URI изображения сохраняется в ViewModel.
         *
         * @see ActivityResultContracts.TakePicture
         */
        val takePictureContract: ActivityResultLauncher<Uri> =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                if (success) {
                    if (isCompressionEnabled) {
                        val compressedFile = imageHelper.compressImage(photoUri)
                        compressedFile?.let { file: File ->
                            if (file.exists()) {
                                newEventViewModel.saveAttachmentFileType(
                                    FileModel(
                                        uri = Uri.fromFile(file),
                                        type = AttachmentTypeFile.IMAGE
                                    )
                                )
                            } else {
                                requireContext().singleVibrationWithSystemCheck(35L)

                                requireContext().showMaterialDialogWithTwoButtons(
                                    title = getString(R.string.image_compression_error),
                                    message = getString(R.string.image_compression_error_description),
                                    cancelButtonText = getString(R.string.unplug),
                                    deleteButtonText = getString(R.string.thanks),
                                    onDeleteConfirmed = {
                                        requireContext().singleVibrationWithSystemCheck(35L)

                                        isCompressionEnabled = false
                                        newEventViewModel.saveAttachmentFileType(null)
                                    },
                                )
                            }
                        }
                    } else {
                        newEventViewModel.saveAttachmentFileType(
                            FileModel(
                                uri = photoUri,
                                type = AttachmentTypeFile.IMAGE
                            )
                        )
                    }
                }
            }

        /**
         * Контракт для запуска активности выбора фотографии из галереи.
         *
         * После выбора фотографии, URI изображения сохраняется в ViewModel.
         *
         * @see ActivityResultContracts.PickVisualMedia
         */
        val takePictureGalleryContract: ActivityResultLauncher<PickVisualMediaRequest> =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriOrNull: Uri? ->
                uriOrNull?.let { uri: Uri ->
                    if (isCompressionEnabled) {
                        val compressedFile = imageHelper.compressImage(uri)
                        compressedFile?.let { file: File ->
                            if (file.exists()) {
                                newEventViewModel.saveAttachmentFileType(
                                    FileModel(
                                        uri = Uri.fromFile(file),
                                        type = AttachmentTypeFile.IMAGE
                                    )
                                )
                            } else {
                                requireContext().singleVibrationWithSystemCheck(35L)

                                requireContext().showMaterialDialogWithTwoButtons(
                                    title = getString(R.string.image_compression_error),
                                    message = getString(R.string.image_compression_error_description),
                                    cancelButtonText = getString(R.string.unplug),
                                    deleteButtonText = getString(R.string.thanks),
                                    onDeleteConfirmed = {
                                        requireContext().singleVibrationWithSystemCheck(35L)

                                        isCompressionEnabled = false
                                        newEventViewModel.saveAttachmentFileType(null)
                                    },
                                )
                            }
                        }
                    } else {
                        newEventViewModel.saveAttachmentFileType(
                            FileModel(
                                uri = uri,
                                type = AttachmentTypeFile.IMAGE
                            )
                        )
                    }
                }
            }

        binding.buttonSelectPhoto.setOnClickListener {
            takePictureContract.launch(photoUri)
        }

        binding.buttonSelectPhotoToGallery.setOnClickListener {
            takePictureGalleryContract.launch(
                PickVisualMediaRequest(ImageOnly)
            )
        }

        binding.buttonRemoveImage.setOnClickListener {
            newEventViewModel.saveAttachmentFileType(null)
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
                        date = newDate,
                        contentResolver = requireContext().contentResolver,
                        onProgress = { progress ->
                            binding.progressBar.setProgressCompat(progress, true)
                        },
                    )
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().showTopSnackbar(
                        message = getString(R.string.error_text_event_is_empty),
                        iconRes = R.drawable.ic_cross_24,
                        iconTintRes = R.color.error_color
                    )
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        observeState(
            newEventViewModel = newEventViewModel,
            binding = binding,
            toolbarViewModel = toolbarViewModel,
        )

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

        keyboardScrolling(binding = binding)

        return binding.root
    }

    /**
     * Наблюдает за состоянием ViewModel и обновляет UI в зависимости от изменений.
     *
     * Этот метод подписывается на изменения состояния ViewModel и обновляет UI, например, отображает или скрывает изображение,
     * обрабатывает ошибки и навигацию.
     *
     * @param newEventViewModel ViewModel, за состоянием которой ведется наблюдение.
     * @param binding Привязка данных для доступа к элементам UI.
     * @param imageHelper Вспомогательный класс для работы с изображениями.
     * @param timeFile Временный файл, который может быть удален после завершения операции.
     *
     * @see NewPostViewModel
     * @see FragmentNewOrUpdatePostBinding
     * @see ImageHelper
     * @see File
     */
    private fun observeState(
        newEventViewModel: NewEventViewModel,
        binding: FragmentNewOrUpdateEventBinding,
        toolbarViewModel: ToolBarViewModel,
    ) {
        newEventViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { newEventState: NewEventState ->
                binding.progressBar.isVisible = newEventState.isLoading

                blockingUiWhenLoading(
                    binding = binding,
                    toolBarViewModel = toolbarViewModel,
                    blocking = newEventState.isLoading,
                )

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
                            requireContext().showTopSnackbar(
                                message = getString(R.string.network_error),
                                iconRes = R.drawable.ic_cross_24,
                                iconTintRes = R.color.error_color
                            )

                            newEventViewModel.consumerError()
                        } else if (errorText == getString(R.string.unknown_error)) {
                            requireContext().showTopSnackbar(
                                message = getString(R.string.unknown_error),
                                iconRes = R.drawable.ic_cross_24,
                                iconTintRes = R.color.error_color
                            )

                            newEventViewModel.consumerError()
                        }
                    }

                when (newEventState.file?.type) {
                    AttachmentTypeFile.IMAGE -> {
                        binding.imageContainer.isVisible = true
                        binding.root.isClickable = false

                        val radius =
                            requireContext().resources.getDimensionPixelSize(R.dimen.radius_for_rounding_images)

                        binding.skeletonAttachment.showSkeleton()

                        Glide.with(binding.root)
                            .load(newEventState.file.uri)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.skeletonAttachment.showOriginal()
                                    binding.image.setImageResource(R.drawable.error_placeholder)

                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.skeletonAttachment.showOriginal()

                                    return false
                                }
                            })
                            .transform(RoundedCorners(radius))
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .thumbnail(
                                Glide.with(binding.root)
                                    .load(newEventState.file.uri)
                                    .override(50, 50)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                            )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.error_placeholder)
                            .into(binding.image)
                    }

                    AttachmentTypeFile.VIDEO,
                    AttachmentTypeFile.AUDIO,
                    null -> {
                        binding.skeletonAttachment.showOriginal()
                        binding.imageContainer.isGone = true
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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

            binding.dateText.text = dateTimeString
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
                LoggerHelper.e(e.toString())
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

    /**
     * Блокирует или разблокирует элементы пользовательского интерфейса во время загрузки.
     * Эта функция управляет состоянием элементов UI, таких как текстовое поле, кнопки и панель инструментов,
     *
     * @param binding Привязка данных для доступа к элементам UI фрагмента. Используется для управления состоянием элементов, таких как текстовое поле и кнопки.
     * @param toolBarViewModel ViewModel для управления состоянием панели инструментов. Используется для скрытия или отображения кнопки сохранения.
     * @param blocking Флаг, указывающий, нужно ли заблокировать UI. Если `true`, элементы UI будут заблокированы. Если `false`, элементы UI будут разблокированы.
     *
     * @see FragmentNewOrUpdateEventBinding Привязка данных для фрагмента создания или обновления события.
     * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
     */
    private fun blockingUiWhenLoading(
        binding: FragmentNewOrUpdateEventBinding,
        toolBarViewModel: ToolBarViewModel,
        blocking: Boolean,
    ) {
        binding.cardOption.isEnabled = !blocking
        binding.optionText.isEnabled = !blocking
        binding.optionSwitch.isEnabled = !blocking

        binding.cardDate.isEnabled = !blocking
        binding.dateText.isEnabled = !blocking
        binding.selectDateTimeButton.isEnabled = !blocking

        binding.cardLink.isEnabled = !blocking
        binding.link.isEnabled = !blocking

        binding.cardContent.isEnabled = !blocking
        binding.content.isEnabled = !blocking

        binding.buttonSelectPhoto.isEnabled = !blocking
        binding.buttonSelectPhotoToGallery.isEnabled = !blocking
        binding.buttonOpenSettings.isEnabled = !blocking
        binding.buttonRemoveImage.isEnabled = !blocking

        toolBarViewModel.setSaveVisible(!blocking)
    }

    private fun keyboardScrolling(binding: FragmentNewOrUpdateEventBinding) {
        val scrollView = binding.scrollView
        val editText = binding.content

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, editText.bottom)
                }
            }
        }
    }

    /**
     * Отображает нижний лист (Bottom Sheet) с настройками, позволяя пользователю управлять сжатием изображений.
     * При изменении состояния переключателя сжатия изображений обновляет значение переменной `isCompressionEnabled`.
     *
     * @see SettingsBottomSheetFragment
     * @sample showSettingsBottomSheet()
     */
    private fun showSettingsBottomSheet() {
        val bottomSheet = SettingsBottomSheetFragment(isCompressionEnabled)

        bottomSheet.setOnCompressionToggleListener { isChecked ->
            isCompressionEnabled = isChecked
        }

        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
}
