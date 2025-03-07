package com.eltex.androidschool.fragments.common

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.BottomSheetSettingsBinding
import com.eltex.androidschool.utils.extensions.showMaterialDialog
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Фрагмент, отображающий нижний лист (Bottom Sheet) с настройками.
 * Позволяет пользователю включать или отключать сжатие изображений и открывать правила использования приложения.
 *
 * @property isCompressionEnabled Флаг, указывающий, включено ли сжатие изображений.
 */
class SettingsBottomSheetFragment(
    private val isCompressionEnabled: Boolean
) : BottomSheetDialogFragment() {

    /**
     * Колбэк, который вызывается при изменении состояния переключателя сжатия изображений.
     */
    private var onCompressionToggle: ((Boolean) -> Unit)? = null

    /**
     * Создает представление фрагмента. Инициализирует привязку данных и настраивает обработчики событий.
     *
     * @param inflater Инфлейтер для создания представления.
     * @param container Родительский контейнер для представления.
     * @param savedInstanceState Сохраненное состояние фрагмента.
     * @return Корневое представление фрагмента.
     * @see BottomSheetSettingsBinding
     * @sample onCreateView(inflater, container, savedInstanceState)
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetSettingsBinding.inflate(inflater, container, false)

        binding.switchImageCompression.isChecked = isCompressionEnabled

        binding.switchImageCompression.setOnCheckedChangeListener { _, isChecked ->
            requireContext().singleVibrationWithSystemCheck(35L)

            onCompressionToggle?.invoke(isChecked)
        }

        binding.cardImageCompression.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)

            binding.switchImageCompression.toggle()
        }

        binding.cardImageCompression.setOnLongClickListener {
            requireContext().showMaterialDialog(
                title = getString(R.string.compress_images_title),
                message = getString(R.string.compress_images_description),
            )

            true
        }

        binding.switchImageCompression.setOnLongClickListener {
            requireContext().showMaterialDialog(
                title = getString(R.string.compress_images_title),
                message = getString(R.string.compress_images_description),
            )

            true
        }

        binding.buttonOpenRules.setOnClickListener {
            dismiss()

            findNavController().navigate(
                R.id.action_global_rulesFragment
            )
        }

        return binding.root
    }

    /**
     * Устанавливает слушатель для изменения состояния переключателя сжатия изображений.
     *
     * @param listener Колбэк, который будет вызван при изменении состояния переключателя.
     * @sample setOnCompressionToggleListener(listener)
     */
    fun setOnCompressionToggleListener(listener: (Boolean) -> Unit) {
        this.onCompressionToggle = listener
    }
}
