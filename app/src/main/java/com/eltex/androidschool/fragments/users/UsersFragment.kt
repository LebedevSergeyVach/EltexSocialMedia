package com.eltex.androidschool.fragments.users

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentUsersBinding
import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.adapter.users.UserAdapter
import com.eltex.androidschool.repository.users.NetworkUserRepository
import com.eltex.androidschool.ui.offset.OffsetDecoration
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.viewmodel.users.UsersState
import com.eltex.androidschool.viewmodel.users.UsersViewModel
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.toast
import com.eltex.androidschool.viewmodel.auth.user.AccountViewModel

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Фрагмент для отображения списка пользователей.
 *
 * Этот фрагмент отвечает за отображение списка пользователей, а также за обработку событий,
 * таких как переход к профилю пользователя при клике на карточку.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 * @see UsersViewModel ViewModel для управления состоянием списка пользователей.
 * @see UserAdapter Адаптер для отображения списка пользователей.
 */
@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val accountViewModel: AccountViewModel by viewModels()

    /**
     * ViewModel для управления состоянием списка пользователей.
     *
     * Эта переменная инициализирует `UsersViewModel` с использованием `viewModelFactory`, чтобы передать
     * зависимости (например, `NetworkUserRepository`) в конструктор ViewModel. ViewModel управляет
     * состоянием списка пользователей и загружает данные из репозитория.
     *
     * @see UsersViewModel ViewModel для управления состоянием списка пользователей.
     * @see NetworkUserRepository Репозиторий для получения данных о пользователях.
     * @see viewModelFactory Фабрика для создания ViewModel с зависимостями.
     */
    private val viewModel by viewModels<UsersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(layoutInflater, container, false)

        /**
         * Адаптер для отображения списка пользователей в RecyclerView.
         *
         * Этот адаптер управляет отображением списка пользователей и обрабатывает клики на карточки пользователей.
         * При клике на карточку пользователя происходит переход к его профилю, если это не текущий пользователь.
         * Если это текущий пользователь, происходит возврат на предыдущий экран.
         *
         * @see UserAdapter Адаптер для отображения списка пользователей.
         * @see UserAdapter.UserListener Интерфейс для обработки кликов на карточки пользователей.
         * @see UserData Модель данных пользователя.
         * @see findNavController Навигационный контроллер для управления переходами между фрагментами.
         */
        val adapter = UserAdapter(
            object : UserAdapter.UserListener {
                override fun onGetUserClicked(user: UserData) {
                    if (user.id == accountViewModel.userId) {
                        findNavController().popBackStack()
                    } else {
                        requireParentFragment().findNavController()
                            .navigate(
                                R.id.action_usersFragment_to_userFragment,
                                bundleOf(
                                    AccountFragment.USER_ID to user.id,
                                    AccountFragment.IC_PROFILE to false
                                ),
                                NavOptions.Builder()
                                    .setEnterAnim(R.anim.slide_in_right)
                                    .setExitAnim(R.anim.slide_out_left)
                                    .setPopEnterAnim(R.anim.slide_in_left)
                                    .setPopExitAnim(R.anim.slide_out_right)
                                    .build()
                            )
                    }
                }
            }
        )

        binding.list.adapter = adapter

        binding.list.addItemDecoration(
            OffsetDecoration(offset = resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        binding.swiperRefresh.setOnRefreshListener {
            viewModel.load()
            requireContext().singleVibrationWithSystemCheck(35)
        }

        binding.swiperRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.active_element)
        )

        binding.swiperRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(requireContext(), R.color.background_color_of_the_refresh_circle)
        )

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { stateUsers: UsersState ->
                binding.errorGroup.isVisible = stateUsers.isEmptyError

                val errorText: CharSequence? =
                    stateUsers.statusUsers.throwableOrNull?.getErrorText(requireContext())

                binding.errorText.text = errorText

                binding.progressBar.isVisible = stateUsers.isEmptyLoading

                binding.swiperRefresh.isRefreshing = stateUsers.isRefreshing

                if (stateUsers.isRefreshError && errorText == getString(R.string.network_error)) {
                    requireContext().toast(R.string.network_error)

                    viewModel.consumerError()
                } else if (stateUsers.isRefreshError && errorText == getString(R.string.unknown_error)) {
                    requireContext().toast(R.string.unknown_error)

                    viewModel.consumerError()
                }

                adapter.submitList(stateUsers.users)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
