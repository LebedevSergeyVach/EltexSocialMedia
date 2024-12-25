package com.eltex.androidschool.fragments.users

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

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

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.FragmentUserBinding
import com.eltex.androidschool.repository.users.NetworkUserRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.users.UserState
import com.eltex.androidschool.viewmodel.users.UserViewModel

/**
 * Фрагмент для отображения информации о пользователе.
 *
 * Этот фрагмент загружает данные пользователя по его идентификатору и отображает их в пользовательском интерфейсе.
 * Если идентификатор не передан, используется идентификатор пользователя по умолчанию из BuildConfig.
 * Также обрабатывается случай отсутствия подключения к интернету и отображается соответствующее сообщение.
 * При отображении ошибки аватар и имя пользователя скрываются.
 *
 * @see UserViewModel ViewModel для управления состоянием пользователей.
 * @see ToolBarViewModel ViewModel для управления состоянием панели инструментов.
 */
class UserFragment : Fragment() {

    private val viewModel by viewModels<UserViewModel> {
        viewModelFactory {
            addInitializer(UserViewModel::class) {
                UserViewModel(
                    NetworkUserRepository()
                )
            }
        }
    }

    companion object {
        const val USER_ID = "USER_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(layoutInflater)

        val userId: Long = arguments?.getLong(USER_ID) ?: BuildConfig.USER_ID

        val toolbarViewModel by activityViewModels<ToolBarViewModel>()

        viewModel.getUserById(userId)

        binding.swiperRefresh.setOnRefreshListener {
            viewModel.getUserById(userId)
        }

        binding.retryButton.setOnClickListener {
            viewModel.getUserById(userId)
        }

        binding.swiperRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.active_element)
        )

        binding.swiperRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(requireContext(), R.color.background_color_of_the_refresh_circle)
        )

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { userState: UserState ->
                binding.progressBar.isVisible = userState.isEmptyLoading
                binding.swiperRefresh.isRefreshing = userState.isRefreshing
                binding.errorGroup.isVisible = userState.isEmptyError

                binding.avatarUser.isVisible = !userState.isEmptyError && !userState.isEmptyLoading
                binding.initial.isVisible = !userState.isEmptyError && !userState.isEmptyLoading
                binding.nameUser.isVisible = !userState.isEmptyError && !userState.isEmptyLoading

                val errorText: CharSequence? =
                    userState.statusUser.throwableOrNull?.getErrorText(requireContext())

                binding.errorText.text = errorText

                if (userState.isRefreshError && errorText == getString(R.string.network_error)) {
                    requireContext().toast(R.string.network_error)

                    viewModel.consumerError()
                } else if (userState.isRefreshError && errorText == getString(R.string.unknown_error)) {
                    requireContext().toast(R.string.unknown_error)

                    viewModel.consumerError()
                }

                userState.users?.firstOrNull()?.let { user ->
                    binding.nameUser.text = user.name
                    binding.initial.text = user.name.take(2)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        if (userId != BuildConfig.USER_ID) {
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
            toolbar.title = getString(R.string.account)
        }

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> Unit
                    }
                }
            }
        )

        return binding.root
    }
}
