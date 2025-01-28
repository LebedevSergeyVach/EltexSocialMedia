package com.eltex.androidschool.fragments.posts

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
import androidx.core.content.FileProvider
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
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.data.common.AttachmentTypeFile
import com.eltex.androidschool.databinding.FragmentNewOrUpdatePostBinding
import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.utils.vibrateWithEffect
import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostState
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostViewModel
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel

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
class NewOrUpdatePostFragment : Fragment() {

    companion object {
        const val POST_ID = "POST_ID"
        const val POST_CONTENT = "POST_CONTENT"
        const val IS_UPDATE = "IS_UPDATE"
        const val POST_CREATED_OR_UPDATED_KEY = "POST_CREATED_OR_UPDATED_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewOrUpdatePostBinding.inflate(layoutInflater)

        /**
         * ViewModel для управления состоянием панели инструментов.
         *
         * Используется для отображения и скрытия кнопки сохранения в зависимости от состояния фрагмента.
         *
         * @see ToolBarViewModel
         */
        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        val postId = arguments?.getLong(POST_ID) ?: 0L
        val content = arguments?.getString(POST_CONTENT) ?: ""
        val isUpdate = arguments?.getBoolean(IS_UPDATE, false) ?: false

        binding.progressBar.isVisible = false
        binding.content.setText(content)

        /**
         * ViewModel для управления созданием и обновлением постов.
         *
         * Используется для обработки логики создания и обновления постов, а также для управления состоянием фрагмента.
         *
         * @see NewPostViewModel
         */
        val newPostVewModel by viewModels<NewPostViewModel> {
            viewModelFactory {
                addInitializer(
                    NewPostViewModel::class
                ) {
                    NewPostViewModel(
                        repository = NetworkPostRepository(),
                        postId = postId
                    )
                }
            }
        }

        /**
         * URI для временного хранения фотографии, которая будет прикреплена к посту.
         *
         * @see createPhotoUri
         */
        val photoUri: Uri = createPhotoUri()

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
                    newPostVewModel.saveAttachmentFileType(
                        FileModel(
                            uri = photoUri,
                            type = AttachmentTypeFile.IMAGE
                        )
                    )
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
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                uri?.let {
                    newPostVewModel.saveAttachmentFileType(
                        FileModel(
                            uri = uri,
                            type = AttachmentTypeFile.IMAGE
                        )
                    )
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
            newPostVewModel.saveAttachmentFileType(null)
        }

        toolbarViewModel.saveClicked.filter { display: Boolean -> display }
            .onEach {
                val newContent = binding.content.text?.toString().orEmpty().trimStart().trimEnd()

                if (newContent.isNotEmpty()) {
                    binding.progressBar.isVisible = true

                    newPostVewModel.save(content = newContent, context = requireContext())
                } else {
                    requireContext().vibrateWithEffect(100L)
                    requireContext().toast(R.string.error_text_post_is_empty)
                }

                toolbarViewModel.onSaveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        observeState(
            newPostVewModel = newPostVewModel,
            binding = binding
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
            if (isUpdate) getString(R.string.update_post_title) else getString(R.string.new_post_title)

        return binding.root
    }

    /**
     * Наблюдает за состоянием ViewModel и обновляет UI в зависимости от изменений.
     *
     * Этот метод подписывается на изменения состояния ViewModel и обновляет UI, например, отображает или скрывает изображение,
     * обрабатывает ошибки и навигацию.
     *
     * @param newPostVewModel ViewModel, за состоянием которой ведется наблюдение.
     * @param binding Привязка данных для доступа к элементам UI.
     */
    private fun observeState(
        newPostVewModel: NewPostViewModel,
        binding: FragmentNewOrUpdatePostBinding,
    ) {
        newPostVewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { newPostState: NewPostState ->
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
                            requireContext().toast(R.string.network_error)

                            newPostVewModel.consumerError()
                        } else if (errorText == getString(R.string.unknown_error)) {
                            requireContext().toast(R.string.unknown_error)

                            newPostVewModel.consumerError()
                        }
                    }

                when (newPostState.file?.type) {
                    AttachmentTypeFile.IMAGE -> {
                        binding.imageContainer.isVisible = true
                        binding.image.setImageURI(newPostState.file.uri)
                    }

                    AttachmentTypeFile.VIDEO,
                    AttachmentTypeFile.AUDIO,
                    null -> binding.imageContainer.isGone = true
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Создает URI для временного хранения фотографии.
     *
     * Этот метод создает файл в кэше приложения и возвращает URI для этого файла.
     *
     * @return URI для временного файла фотографии.
     * @see FileProvider
     */
    private fun createPhotoUri(): Uri {
        val directory: File = requireContext().cacheDir.resolve("file_picker").apply {
            mkdir()
        }

        val file: File = directory.resolve("image")

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            file
        )
    }
}
