package com.eltex.androidschool.fragments.auth

import android.animation.ValueAnimator
import android.os.Bundle

import android.text.Editable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.autofill.AutofillManager

import androidx.activity.OnBackPressedCallback

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams

import androidx.core.widget.doAfterTextChanged

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentAuthorizationBinding
import com.eltex.androidschool.fragments.common.ToolbarFragment
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorTextAuthorization
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.viewmodel.auth.authorizations.AuthorizationState
import com.eltex.androidschool.viewmodel.auth.authorizations.AuthorizationViewModel

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        // Инициализируем значения размерностей в пикселях
        val marginBottomKeyboardHiddenPx: Int =
            resources.getDimensionPixelSize(R.dimen.margin_bottom_keyboard_hidden)
        val marginBottomKeyboardShownPx: Int =
            resources.getDimensionPixelSize(R.dimen.margin_bottom_keyboard_shown_auth)

        // Инициализация AutofillManager
        val autofillManager = requireContext().getSystemService(AutofillManager::class.java)

        // Настройка autofill hints для полей ввода
        setupAutofillHints(binding)

        monitoringButtonStatus(binding = binding)

        binding.buttonAuthorizationAccount.setOnClickListener {
            val login = binding.textLoginUser.text?.toString().orEmpty().trimStart().trimEnd()
            val password = binding.textPasswordUser.text?.toString().orEmpty().trimStart().trimEnd()

            viewModel.login(login = login, password = password)

            // Сообщаем AutofillManager о завершении заполнения
            if (autofillManager.isEnabled) {
                autofillManager.commit()
            }
        }

        binding.buttonToRegistrationAccount.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
//                replace(R.id.container, RegistrationFragment())
//                setReorderingAllowed(true)
//                setPrimaryNavigationFragment(this@AuthorizationFragment)
//                remove(this@AuthorizationFragment)

                replace(R.id.container, RegistrationFragment())
                setReorderingAllowed(true)
                // Вместо remove, добавляем текущий фрагмент (AuthorizationFragment) в стек возврата
                addToBackStack(null) // Использование null в качестве имени стека возврата
            }
        }

        viewModelStateLifecycle(binding = binding)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        keyboardTrackingLogic(
            binding = binding,
            marginBottomKeyboardShownPx = marginBottomKeyboardShownPx,
            marginBottomKeyboardHiddenPx = marginBottomKeyboardHiddenPx
        )

        return binding.root
    }

    /**
     * Наблюдает за состоянием ViewModel и обновляет UI в зависимости от состояния.
     *
     * @param binding Привязка к макету фрагмента.
     */
    private fun viewModelStateLifecycle(binding: FragmentAuthorizationBinding) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state: AuthorizationState ->
                binding.buttonAuthorizationAccount.isEnabled = state.isButtonEnabled
                binding.buttonAuthorizationAccount.alpha = if (state.isButtonEnabled) 1f else 0.5f

                binding.progressBar.isVisible = state.isLoading
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
                        addToBackStack(null)
                        remove(this@AuthorizationFragment)
                    }
                }

                state.statusAuthorization.throwableOrNull?.getErrorTextAuthorization(requireContext())
                    ?.let { errorText: CharSequence ->
                        requireContext().showTopSnackbar(
                            message = errorText.toString(),
                            iconRes = R.drawable.ic_cross_24,
                            iconTintRes = R.color.error_color
                        )

                        viewModel.consumerError()
                    }

                state.statusAuthorization
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun keyboardTrackingLogic(
        binding: FragmentAuthorizationBinding,
        marginBottomKeyboardShownPx: Int,
        marginBottomKeyboardHiddenPx: Int
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Высота клавиатуры - учитываем системные бары, которые могут быть перекрыты клавиатурой
            val keyboardHeight = imeInsets.bottom - systemBarsInsets.bottom

            val targetMarginBottom = if (keyboardHeight > 0) {
                marginBottomKeyboardShownPx
            } else {
                marginBottomKeyboardHiddenPx
            }

            val currentMarginBottom = (binding.cardDataEntryUser.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0

            if (currentMarginBottom != targetMarginBottom) {
                ValueAnimator.ofInt(currentMarginBottom, targetMarginBottom).apply {
                    addUpdateListener { animator ->
                        val animatedValue = animator.animatedValue as Int
                        binding.cardDataEntryUser.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = animatedValue
                        }
                    }
                    duration = 300 // Adjust the duration as needed (in milliseconds)
                    interpolator = AccelerateDecelerateInterpolator() // Optional: Add an interpolator
                    start()
                }
            }

            // Важно: вернуть Insets, чтобы другие View в иерархии могли их обработать
            insets
        }
    }

    /**
     * Мониторит изменения в полях ввода и обновляет состояние кнопки.
     *
     * @param binding Привязка к макету фрагмента.
     */
    private fun monitoringButtonStatus(binding: FragmentAuthorizationBinding) {
        binding.textLoginUser.doAfterTextChanged { text: Editable? ->
            val login = text?.toString() ?: ""
            val password = binding.textPasswordUser.text?.toString() ?: ""

            viewModel.updateButtonState(login = login, password = password)
        }

        binding.textPasswordUser.doAfterTextChanged { text: Editable? ->
            val login = binding.textLoginUser.text?.toString() ?: ""
            val password = text?.toString() ?: ""

            viewModel.updateButtonState(login = login, password = password)
        }
    }

    /**
     * Блокирует или разблокирует UI в зависимости от состояния загрузки.
     *
     * @param binding Привязка к макету фрагмента.
     * @param blocking Флаг, указывающий, нужно ли блокировать UI.
     */
    private fun blockingUiWhenLoading(
        binding: FragmentAuthorizationBinding,
        blocking: Boolean,
    ) {
        binding.textLoginUser.isEnabled = !blocking
        binding.textPasswordUser.isEnabled = !blocking
        binding.buttonAuthorizationAccount.isEnabled != blocking
        binding.buttonToRegistrationAccount.isEnabled = !blocking
    }


    /**
     * Настраивает подсказки для автозаполнения полей ввода
     */
    private fun setupAutofillHints(binding: FragmentAuthorizationBinding) {
        // Для поля логина
        binding.textLoginUser.apply {
            setAutofillHints(View.AUTOFILL_HINT_USERNAME)
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }

        // Для поля пароля
        binding.textPasswordUser.apply {
            setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }
    }
}
