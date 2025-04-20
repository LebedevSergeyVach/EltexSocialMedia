package com.eltex.androidschool.fragments.posts

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

import com.eltex.androidschool.R
import com.eltex.androidschool.data.common.AttachmentTypeFile
import com.eltex.androidschool.databinding.FragmentNewOrUpdatePostBinding
import com.eltex.androidschool.fragments.common.SettingsBottomSheetFragment
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.vibrateWithEffect
import com.eltex.androidschool.utils.helper.ImageHelper
import com.eltex.androidschool.data.media.FileModel
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostState
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostViewModel

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.io.File

/**
 * Фрагмент для создания или обновления поста.
 *
 * Этот фрагмент позволяет пользователю создавать новый пост или обновлять существующий.
 * Он также управляет отображением и скрытием кнопки сохранения в зависимости от состояния фрагмента.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see NewPostViewModel ViewModel для управления созданием и обновлением постов.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
@AndroidEntryPoint
class NewOrUpdatePostFragment : Fragment() {

    companion object {
        const val POST_ID = "POST_ID"
        const val POST_CONTENT = "POST_CONTENT"
        const val IS_UPDATE = "IS_UPDATE"
        const val POST_CREATED_OR_UPDATED_KEY = "POST_CREATED_OR_UPDATED_KEY"
    }

    /**
     * Флаг, указывающий, включено ли сжатие изображений в приложении.
     * По умолчанию установлен в `true`, что означает, что сжатие изображений включено.
     *
     * @property isCompressionEnabled Состояние сжатия изображений (включено/выключено).
     */
    private var isCompressionEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrUpdatePostBinding.inflate(inflater, container, false)

        /**
         * Экземпляр класса `ImageHelper`, используемый для работы с изображениями.
         * Инициализируется с контекстом текущего фрагмента или активности.
         *
         * @property imageHelper Вспомогательный объект для работы с изображениями.
         * @see ImageHelper
         */
        val imageHelper = ImageHelper(requireContext())

        /**
         * ViewModel для управления состоянием панели инструментов.
         *
         * Используется для отображения и скрытия кнопки сохранения в зависимости от состояния фрагмента.
         *
         * @see ToolBarViewModel
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val postId: Long = arguments?.getLong(POST_ID) ?: 0L
        val content: String = arguments?.getString(POST_CONTENT) ?: ""
        val isUpdate: Boolean = arguments?.getBoolean(IS_UPDATE, false) == true

        binding.content.setText(content)

        binding.buttonOpenSettings.setOnClickListener {
            showSettingsBottomSheet()
        }

        /**
         * ViewModel для управления созданием и обновлением постов.
         *
         * Используется для обработки логики создания и обновления постов, а также для управления состоянием фрагмента.
         *
         * @see NewPostViewModel
         */
        val newPostViewModel by viewModels<NewPostViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<NewPostViewModel.ViewModelFactory> { factory ->
                    factory.create(postId = postId)
                }
            }
        )

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
                                newPostViewModel.saveAttachmentFileType(
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
                                        newPostViewModel.saveAttachmentFileType(null)
                                    },
                                )
                            }
                        }
                    } else {
                        newPostViewModel.saveAttachmentFileType(
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
                                newPostViewModel.saveAttachmentFileType(
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
                                        newPostViewModel.saveAttachmentFileType(null)
                                    },
                                )
                            }
                        }
                    } else {
                        newPostViewModel.saveAttachmentFileType(
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
            newPostViewModel.saveAttachmentFileType(null)
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty().trimStart().trimEnd()

                if (newContent.isNotEmpty()) {
                    newPostViewModel.save(
                        content = newContent,
                        contentResolver = requireContext().contentResolver,
                        onProgress = { progress ->
                            binding.progressBar.setProgressCompat(progress, true)
                        }
                    )
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().showTopSnackbar(
                        message = getString(R.string.error_text_post_is_empty),
                        iconRes = R.drawable.ic_cross_24,
                        iconTintRes = R.color.error_color
                    )
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        observeState(
            newPostVewModel = newPostViewModel,
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
            if (!isUpdate) getString(R.string.new_post_title) else getString(R.string.update_post_title)

        return binding.root
    }

    /**
     * Наблюдает за состоянием ViewModel (`NewPostViewModel`) и обновляет UI в зависимости от изменений состояния.
     * Также управляет временными файлами и отображает ошибки, если они возникают.
     *
     * @param newPostVewModel ViewModel, которая предоставляет состояние для создания или обновления поста.
     * @param binding Привязка данных для фрагмента `FragmentNewOrUpdatePostBinding`.
     * @param imageHelper Вспомогательный класс для работы с изображениями.
     * @param timeFile Временный файл, который может быть удален после завершения операции.
     *
     * @see NewPostViewModel
     * @see FragmentNewOrUpdatePostBinding
     * @see ImageHelper
     * @see File
     */
    private fun observeState(
        newPostVewModel: NewPostViewModel,
        binding: FragmentNewOrUpdatePostBinding,
        toolbarViewModel: ToolBarViewModel,
    ) {
        newPostVewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { newPostState: NewPostState ->
                binding.progressBar.isVisible = newPostState.isLoading

                blockingUiWhenLoading(
                    binding = binding,
                    toolBarViewModel = toolbarViewModel,
                    blocking = newPostState.isLoading,
                )

                if (newPostState.post != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        POST_CREATED_OR_UPDATED_KEY,
                        bundleOf()
                    )

                    findNavController().navigateUp()
                }

                newPostState.statusPost.throwableOrNull?.getErrorText(requireContext())
                    ?.let { errorText: CharSequence? ->
                        if (errorText == getString(R.string.network_error)) {
                            requireContext().showTopSnackbar(
                                message = getString(R.string.network_error),
                                iconRes = R.drawable.ic_cross_24,
                                iconTintRes = R.color.error_color
                            )

                            newPostVewModel.consumerError()
                        } else if (errorText == getString(R.string.unknown_error)) {
                            requireContext().showTopSnackbar(
                                message = getString(R.string.unknown_error),
                                iconRes = R.drawable.ic_cross_24,
                                iconTintRes = R.color.error_color
                            )

                            newPostVewModel.consumerError()
                        }
                    }

                when (newPostState.file?.type) {
                    AttachmentTypeFile.IMAGE -> {
                        binding.imageContainer.isVisible = true
                        binding.root.isClickable = false

                        val radius =
                            requireContext().resources.getDimensionPixelSize(R.dimen.radius_for_rounding_images)

                        binding.skeletonAttachment.showSkeleton()

                        Glide.with(binding.root)
                            .load(newPostState.file.uri)
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
                                    .load(newPostState.file.uri)
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
     * Блокирует или разблокирует элементы пользовательского интерфейса во время загрузки.
     * Эта функция управляет состоянием элементов UI, таких как текстовое поле, кнопки и панель инструментов,
     *
     * @param binding Привязка данных для доступа к элементам UI фрагмента. Используется для управления состоянием элементов, таких как текстовое поле и кнопки.
     * @param toolBarViewModel ViewModel для управления состоянием панели инструментов. Используется для скрытия или отображения кнопки сохранения.
     * @param blocking Флаг, указывающий, нужно ли заблокировать UI. Если `true`, элементы UI будут заблокированы. Если `false`, элементы UI будут разблокированы.
     *
     * @see FragmentNewOrUpdatePostBinding Привязка данных для фрагмента создания или обновления поста.
     * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
     */
    private fun blockingUiWhenLoading(
        binding: FragmentNewOrUpdatePostBinding,
        toolBarViewModel: ToolBarViewModel,
        blocking: Boolean,
    ) {
        binding.content.isEnabled = !blocking
        binding.buttonSelectPhoto.isEnabled = !blocking
        binding.buttonSelectPhotoToGallery.isEnabled = !blocking
        binding.buttonOpenSettings.isEnabled = !blocking
        binding.buttonRemoveImage.isEnabled = !blocking

        toolBarViewModel.setSaveVisible(!blocking)
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
