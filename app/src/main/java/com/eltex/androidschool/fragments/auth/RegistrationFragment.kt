package com.eltex.androidschool.fragments.auth

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

import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.data.common.AttachmentTypeFile
import com.eltex.androidschool.databinding.FragmentRegistrationBinding
import com.eltex.androidschool.fragments.common.ToolbarFragment
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorTextRegistration
import com.eltex.androidschool.viewmodel.auth.registration.RegistrationState
import com.eltex.androidschool.viewmodel.auth.registration.RegistrationViewModel
import com.eltex.androidschool.data.media.FileModel
import com.eltex.androidschool.utils.extensions.showTopSnackbar

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.io.File

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val takePictureGalleryContract: ActivityResultLauncher<PickVisualMediaRequest> =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                uri?.let {
                    viewModel.saveAttachmentFileType(
                        FileModel(
                            uri = uri,
                            type = AttachmentTypeFile.IMAGE
                        )
                    )
                }
            }

        binding.imageUserSelectAvatar.setOnClickListener {
            takePictureGalleryContract.launch(
                PickVisualMediaRequest(ImageOnly)
            )
        }

        monitoringButtonStatus(binding = binding)

        // Настройка autofill hints для полей ввода
        setupAutofillHints(binding)

        binding.buttonRegistrationAccount.setOnClickListener {
            val login = binding.textLoginUser.text?.toString().orEmpty().trimStart().trimEnd()
            val username = binding.textNameUser.text?.toString().orEmpty().trimStart().trimEnd()
            val password = binding.textPasswordUser.text?.toString().orEmpty().trimStart().trimEnd()

            if (containsForbiddenWords(login) || containsForbiddenWords(username)) {
                requireContext().showTopSnackbar(
                    getString(R.string.you_cant_use_admin_in_login_or_username),
                    iconRes = R.drawable.ic_cross_24,
                    iconTintRes = R.color.error_color
                )
            } else {
                viewModel.register(
                    login = login,
                    username = username,
                    password = password,
                    contentResolver = requireContext().contentResolver,
                    onProgress = { progress ->
                        binding.progressBar.setProgressCompat(progress, true)
                    }
                )
            }
        }

        binding.buttonToAuthorizationAccount.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                )
                replace(R.id.container, AuthorizationFragment())
                setReorderingAllowed(true)
                addToBackStack(null)
                remove(this@RegistrationFragment)
            }
        }

        viewModelStateLifecycle(binding = binding)

        return binding.root
    }

    /**
     * Наблюдает за состоянием ViewModel и обновляет UI в зависимости от состояния.
     *
     * @param binding Привязка к макету фрагмента.
     */
    private fun viewModelStateLifecycle(binding: FragmentRegistrationBinding) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state: RegistrationState ->
                binding.progressBar.isVisible = state.isLoading

                binding.buttonRegistrationAccount.isEnabled = state.isButtonEnabled
                binding.buttonRegistrationAccount.alpha = if (state.isButtonEnabled) 1f else 0.5f

                blockingUiWhenLoading(binding = binding, blocking = state.isLoading)

                if (state.isSuccess) {
                    requireActivity().supportFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        replace(R.id.container, ToolbarFragment())
                        setReorderingAllowed(true)
                        remove(this@RegistrationFragment)
                    }
                }

                state.statusRegistration.throwableOrNull?.getErrorTextRegistration(
                    requireContext()
                )
                    ?.let { errorText: CharSequence ->
                        requireContext().showTopSnackbar(
                            message = errorText.toString(),
                            iconRes = R.drawable.ic_cross_24,
                            iconTintRes = R.color.error_color
                        )

                        viewModel.consumerError()
                    }

                when (state.file?.type) {
                    AttachmentTypeFile.IMAGE -> {
                        binding.imagePhoto.isVisible = false
                        binding.root.isClickable = false

                        binding.skeletonImageAvatar.showSkeleton()

                        Glide.with(binding.root)
                            .load(state.file.uri)
                            .circleCrop()
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.skeletonImageAvatar.showOriginal()
                                    binding.imageUserSelectAvatar.setImageResource(R.drawable.error_placeholder)
                                    binding.imagePhoto.isVisible = false

                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.skeletonImageAvatar.showOriginal()
                                    binding.imagePhoto.isVisible = false

                                    return false
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .thumbnail(
                                Glide.with(binding.root)
                                    .load(state.file.uri)
                                    .override(50, 50)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .circleCrop()
                            )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.error_placeholder)
                            .into(binding.imageUserSelectAvatar)
                    }

                    AttachmentTypeFile.VIDEO,
                    AttachmentTypeFile.AUDIO,
                    null -> {
                        binding.skeletonImageAvatar.showOriginal()
                        binding.imagePhoto.isVisible = true
                    }
                }

                state.statusRegistration
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Мониторит изменения в полях ввода и обновляет состояние кнопки.
     *
     * @param binding Привязка к макету фрагмента.
     */
    private fun monitoringButtonStatus(binding: FragmentRegistrationBinding) {
        binding.textLoginUser.doAfterTextChanged { text ->
            val login = text?.toString() ?: ""
            val username = binding.textNameUser.text?.toString() ?: ""
            val password = binding.textPasswordUser.text?.toString() ?: ""

            viewModel.updateButtonState(
                login = login,
                username = username,
                password = password,
            )
        }

        binding.textNameUser.doAfterTextChanged { text ->
            val login = binding.textLoginUser.text?.toString() ?: ""
            val username = text?.toString() ?: ""
            val password = binding.textPasswordUser.text?.toString() ?: ""

            viewModel.updateButtonState(
                login = login,
                username = username,
                password = password,
            )
        }

        binding.textPasswordUser.doAfterTextChanged { text ->
            val login = binding.textLoginUser.text?.toString() ?: ""
            val username = binding.textNameUser.text?.toString() ?: ""
            val password = text?.toString() ?: ""

            viewModel.updateButtonState(
                login = login,
                username = username,
                password = password,
            )
        }
    }

    /**
     * Проверяет, содержит ли строка запрещенные слова ("admin" или "админ") в любом регистре.
     *
     * @param input Строка для проверки.
     * @return `true`, если строка содержит запрещенные слова, иначе `false`.
     */
    private fun containsForbiddenWords(input: String): Boolean {
        val forbiddenWords = listOf("admin", "админ")

        return forbiddenWords.any { word ->
            input.contains(word, ignoreCase = true)
        }
    }

    /**
     * Блокирует или разблокирует UI в зависимости от состояния загрузки.
     *
     * @param binding Привязка к макету фрагмента.
     * @param blocking Флаг, указывающий, нужно ли блокировать UI.
     */
    private fun blockingUiWhenLoading(
        binding: FragmentRegistrationBinding,
        blocking: Boolean,
    ) {
        binding.textLoginUser.isEnabled = !blocking
        binding.textNameUser.isEnabled = !blocking
        binding.textPasswordUser.isEnabled = !blocking
        binding.buttonToAuthorizationAccount.isEnabled = !blocking
        binding.buttonRegistrationAccount.isEnabled = !blocking
        binding.imageUserSelectAvatar.isEnabled = !blocking
    }

    /**
     * Создает URI для временного файла изображения.
     *
     * @return URI созданного файла.
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

    /**
     * Настраивает подсказки для автозаполнения полей ввода
     */
    private fun setupAutofillHints(binding: FragmentRegistrationBinding) {
        // Для поля логина
        binding.textLoginUser.apply {
            setAutofillHints(View.AUTOFILL_HINT_USERNAME)
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }

        // Для поля имени пользователя
        binding.textNameUser.apply {
            setAutofillHints(View.AUTOFILL_HINT_NAME)
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }

        // Для поля пароля
        binding.textPasswordUser.apply {
            setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }
    }
}
