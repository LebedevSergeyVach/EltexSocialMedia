package com.eltex.androidschool.fragments.users

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf

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
import androidx.navigation.NavController

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import com.eltex.androidschool.R
import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.databinding.FragmentUserBinding

import com.eltex.androidschool.adapter.events.EventAdapter
import com.eltex.androidschool.adapter.posts.PostAdapter
import com.eltex.androidschool.adapter.users.UserPagerAdapter
import com.eltex.androidschool.effecthandler.posts.PostWallEffectHandler

import com.eltex.androidschool.fragments.events.NewOrUpdateEventFragment
import com.eltex.androidschool.fragments.posts.NewOrUpdatePostFragment
import com.eltex.androidschool.reducer.posts.PostWallReducer
import com.eltex.androidschool.repository.events.NetworkEventRepository
import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.repository.users.NetworkUserRepository

import com.eltex.androidschool.ui.common.OffsetDecoration
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper

import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast

import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.events.EventByIdAuthorForUserModel
import com.eltex.androidschool.viewmodel.events.EventState
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallViewModel
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallStore
import com.eltex.androidschool.viewmodel.user.UserState
import com.eltex.androidschool.viewmodel.user.UserViewModel

import com.google.android.material.tabs.TabLayoutMediator

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

    val toolbarViewModel by activityViewModels<ToolBarViewModel>()

    companion object {
        const val USER_ID = "USER_ID"
        const val IC_PROFILE = "IC_PROFILE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(layoutInflater)

        val userId: Long = arguments?.getLong(USER_ID) ?: BuildConfig.USER_ID
        val icProfile: Boolean = arguments?.getBoolean(IC_PROFILE) ?: true

        val userViewModel by viewModels<UserViewModel> {
            viewModelFactory {
                addInitializer(UserViewModel::class) {
                    UserViewModel(
                        repository = NetworkUserRepository(),
                        userId = userId
                    )
                }
            }
        }

        val postViewModel by viewModels<PostWallViewModel> {
            viewModelFactory {
                addInitializer(PostWallViewModel::class) {
                    PostWallViewModel(
                        postWallStore = PostWallStore(
                            reducer = PostWallReducer(userId = userId),
                            effectHandler = PostWallEffectHandler(
                                repository = NetworkPostRepository(),
                                mapper = PostUiModelMapper()
                            ),
                            initMessages = setOf(PostWallMessage.Refresh),
                            initState = PostWallState()
                        )
                    )
                }
            }
        }

        val eventViewModel by viewModels<EventByIdAuthorForUserModel> {
            viewModelFactory {
                addInitializer(EventByIdAuthorForUserModel::class) {
                    EventByIdAuthorForUserModel(
                        repository = NetworkEventRepository(),
                        userId = userId
                    )
                }
            }
        }

        binding.swiperRefresh.setOnRefreshListener {
            loadingDataFromTheViewModel(
                userId = userId,
                userViewModel = userViewModel,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel
            )
        }

        binding.retryButton.setOnClickListener {
            loadingDataFromTheViewModel(
                userId = userId,
                userViewModel = userViewModel,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel
            )
        }

        binding.swiperRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.active_element)
        )

        binding.swiperRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(requireContext(), R.color.background_color_of_the_refresh_circle)
        )

        binding.viewPagerPostsAndEvents.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset))
        )

        if (userId != BuildConfig.USER_ID) {
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
            toolbar.title = getString(R.string.profile)
        }

        val postAdapter = PostAdapter(
            listener = object : PostAdapter.PostListener {
                override fun onLikeClicked(post: PostUiModel) {
                    postViewModel.accept(message = PostWallMessage.Like(post = post))
                }

                override fun onShareClicked(post: PostUiModel) {}

                override fun onDeleteClicked(post: PostUiModel) {
                    postViewModel.accept(message = PostWallMessage.Delete(post = post))
                }

                override fun onUpdateClicked(post: PostUiModel) {
                    if (icProfile) {
                        navigateToUpdatePost(
                            navController = requireParentFragment().requireParentFragment()
                                .findNavController(),
                            actionId = R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment,
                            post = post
                        )
                    } else {
                        navigateToUpdatePost(
                            navController = requireParentFragment().findNavController(),
                            actionId = R.id.action_userFragment_to_newOrUpdatePostFragment,
                            post = post
                        )
                    }
                }

                override fun onGetUserClicked(post: PostUiModel) {}
            },

            context = requireContext(),
            currentUserId = BuildConfig.USER_ID
        )

        val eventAdapter = EventAdapter(
            object : EventAdapter.EventListener {
                override fun onLikeClicked(event: EventUiModel) {
                    eventViewModel.likeById(event.id, event.likedByMe)
                }

                override fun onShareClicked(event: EventUiModel) {}

                override fun onParticipateClicked(event: EventUiModel) {
                    eventViewModel.participateById(event.id, event.participatedByMe)
                }

                override fun onDeleteClicked(event: EventUiModel) {
                    eventViewModel.deleteById(event.id)
                }

                override fun onUpdateClicked(event: EventUiModel) {
                    if (icProfile) {
                        navigateToUpdateEvent(
                            navController = requireParentFragment().requireParentFragment()
                                .findNavController(),
                            actionId = R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment,
                            event = event
                        )
                    } else {
                        navigateToUpdateEvent(
                            navController = requireParentFragment().findNavController(),
                            actionId = R.id.action_userFragment_to_newOrUpdateEventFragment,
                            event = event
                        )
                    }
                }

                override fun onGetUserClicked(event: EventUiModel) {}
            },

            context = requireContext(),
            currentUserId = BuildConfig.USER_ID
        )

        val offset = resources.getDimensionPixelSize(R.dimen.list_offset)
        val pagerAdapter = UserPagerAdapter(
            postAdapter = postAdapter,
            eventAdapter = eventAdapter,
            offset = offset,
            adapter = postAdapter,
            viewModel = postViewModel
        )

        binding.viewPagerPostsAndEvents.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerPostsAndEvents) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.posts)
                1 -> getString(R.string.events)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()

        userViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { userState: UserState ->
                binding.progressBar.isVisible = userState.isEmptyLoading
                binding.progressLiner.isVisible = userState.isEmptyLoading
                binding.swiperRefresh.isRefreshing = userState.isRefreshing
                binding.errorGroup.isVisible = userState.isEmptyError

                binding.avatarUser.isVisible = !userState.isEmptyError && !userState.isEmptyLoading
                binding.initial.isVisible = !userState.isEmptyError && !userState.isEmptyLoading
                binding.nameUser.isVisible = !userState.isEmptyError && !userState.isEmptyLoading

                binding.tabLayout.isVisible = !userState.isEmptyError && !userState.isEmptyLoading
                binding.viewPagerPostsAndEvents.isVisible =
                    !userState.isEmptyError && !userState.isEmptyLoading

                val errorText: CharSequence? =
                    userState.statusUser.throwableOrNull?.getErrorText(requireContext())

                binding.errorText.text = errorText

                if (userState.isRefreshError && errorText == getString(R.string.network_error)) {
                    requireContext().toast(R.string.network_error)

                    userViewModel.consumerError()
                } else if (userState.isRefreshError && errorText == getString(R.string.unknown_error)) {
                    requireContext().toast(R.string.unknown_error)

                    userViewModel.consumerError()
                }

                userState.users?.firstOrNull()?.let { user ->
                    binding.nameUser.text = user.name
                    binding.initial.text = user.name.take(2)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        postViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { postState: PostWallState ->
                postAdapter.submitList(postState.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        eventViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { eventState: EventState ->
                eventAdapter.submitList(eventState.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSettingsAndAllUsersVisible(
                            userId == BuildConfig.USER_ID
                        )

                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSettingsAndAllUsersVisible(
                            false
                        )

                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> Unit
                    }
                }
            }
        )

        return binding.root
    }

    /**
     * Загружает данные о пользователе, его постах и событиях из ViewModel.
     * Используется для инициализации данных при создании фрагмента или обновлении данных.
     *
     * @param userId Идентификатор пользователя, для которого загружаются данные.
     * @param userViewModel ViewModel связанная с данными о пользователе.
     * @param postViewModel ViewModel для управления постами пользователя.
     * @param eventViewModel ViewModel для управления событиями пользователя.
     * @param causeVibration Вызов вибрации (По умолчанию = true).
     *
     * @see UserViewModel.getUserById
     * @see PostByIdAuthorForUser.loadPostsByAuthor
     * @see EventByIdAuthorForUserModel.loadEventsByAuthor
     */
    private fun loadingDataFromTheViewModel(
        userId: Long,
        userViewModel: UserViewModel,
        postViewModel: PostWallViewModel,
        eventViewModel: EventByIdAuthorForUserModel,
        causeVibration: Boolean = true
    ) {
        if (causeVibration) requireContext().singleVibrationWithSystemCheck(35)

        userViewModel.getUserById(userId)
        postViewModel.accept(message = PostWallMessage.Refresh)
        eventViewModel.loadEventsByAuthor(userId)
    }

    /**
     * Выполняет навигацию к фрагменту редактирования поста.
     *
     * Эта функция упрощает навигацию к экрану редактирования поста, используя переданный `NavController`
     * и идентификатор действия (`actionId`). Она также передает данные поста в целевой фрагмент.
     *
     * @param navController Контроллер навигации, который будет использоваться для перехода.
     * @param actionId Идентификатор действия (action ID) для навигации. Это может быть, например,
     *                 `R.id.action_BottomNavigationFragment_to_newOrUpdatePostFragment`.
     * @param post Объект [PostUiModel], содержащий данные поста, которые будут переданы в целевой фрагмент.
     *
     * @see NavController Контроллер навигации, используемый для перехода между фрагментами.
     * @see bundleOf Функция для создания пакета данных, передаваемых в целевой фрагмент.
     * @see NavOptions.Builder Класс для настройки анимации перехода.
     */
    private fun navigateToUpdatePost(
        navController: NavController,
        actionId: Int,
        post: PostUiModel
    ) {
        navController.navigate(
            actionId,
            bundleOf(
                NewOrUpdatePostFragment.POST_ID to post.id,
                NewOrUpdatePostFragment.POST_CONTENT to post.content,
                NewOrUpdatePostFragment.IS_UPDATE to true,
            ),
            NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
        )
    }

    /**
     * Выполняет навигацию к фрагменту редактирования события.
     *
     * Эта функция упрощает навигацию к экрану редактирования события, используя переданный `NavController`
     * и идентификатор действия (`actionId`). Она также передает данные события в целевой фрагмент.
     *
     * @param navController Контроллер навигации, который будет использоваться для перехода.
     * @param actionId Идентификатор действия (action ID) для навигации. Это может быть, например,
     *                 `R.id.action_BottomNavigationFragment_to_newOrUpdateEventFragment`.
     * @param event Объект [EventUiModel], содержащий данные поста, которые будут переданы в целевой фрагмент.
     *
     * @see NavController Контроллер навигации, используемый для перехода между фрагментами.
     * @see bundleOf Функция для создания пакета данных, передаваемых в целевой фрагмент.
     * @see NavOptions.Builder Класс для настройки анимации перехода.
     */
    private fun navigateToUpdateEvent(
        navController: NavController,
        actionId: Int,
        event: EventUiModel
    ) {
        navController.navigate(
            actionId,
            bundleOf(
                NewOrUpdateEventFragment.EVENT_ID to event.id,
                NewOrUpdateEventFragment.EVENT_CONTENT to event.content,
                NewOrUpdateEventFragment.EVENT_LINK to event.link,
                NewOrUpdateEventFragment.EVENT_DATE to event.dataEvent,
                NewOrUpdateEventFragment.EVENT_OPTION to event.optionConducting,
                NewOrUpdateEventFragment.IS_UPDATE to true,
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
