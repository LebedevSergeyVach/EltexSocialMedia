package com.eltex.androidschool.fragments.auth

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentAuthorizationBinding
import com.eltex.androidschool.fragments.common.ToolbarFragment
import com.eltex.androidschool.utils.getErrorTextAuthorization
import com.eltex.androidschool.utils.toast
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

        monitoringButtonStatus(binding = binding)

        binding.buttonAuthorizationAccount.setOnClickListener {
            val login = binding.textLoginUser.text?.toString().orEmpty().trimStart().trimEnd()
            val password = binding.textPasswordUser.text?.toString().orEmpty().trimStart().trimEnd()

            viewModel.login(login = login, password = password)
        }

        binding.buttonToRegistrationAccount.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                replace(R.id.container, RegistrationFragment())
                setReorderingAllowed(true)
                remove(this@AuthorizationFragment)
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

        return binding.root
    }

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
                        remove(this@AuthorizationFragment)
                    }
                }

                state.statusAuthorization.throwableOrNull?.getErrorTextAuthorization(requireContext())
                    ?.let { errorText: CharSequence ->
                        requireContext().toast(errorText.toString())

                        viewModel.consumerError()
                    }

                state.statusAuthorization
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun monitoringButtonStatus(binding: FragmentAuthorizationBinding) {
        binding.textLoginUser.doAfterTextChanged { text ->
            val login = text?.toString() ?: ""
            val password = binding.textPasswordUser.text?.toString() ?: ""

            viewModel.updateButtonState(login = login, password = password)
        }

        binding.textPasswordUser.doAfterTextChanged { text ->
            val login = binding.textLoginUser.text?.toString() ?: ""
            val password = text?.toString() ?: ""

            viewModel.updateButtonState(login = login, password = password)
        }
    }

    private fun blockingUiWhenLoading(
        binding: FragmentAuthorizationBinding,
        blocking: Boolean,
    ) {
        binding.textLoginUser.isEnabled = !blocking
        binding.textPasswordUser.isEnabled = !blocking
        binding.buttonAuthorizationAccount.isEnabled != blocking
        binding.buttonToRegistrationAccount.isEnabled = !blocking
    }
}
