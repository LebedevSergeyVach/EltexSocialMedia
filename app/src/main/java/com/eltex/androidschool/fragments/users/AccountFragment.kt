package com.eltex.androidschool.fragments.users

import android.graphics.drawable.Drawable

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

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

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.events.EventAdapter
import com.eltex.androidschool.adapter.job.JobAdapter
import com.eltex.androidschool.adapter.posts.PostAdapter
import com.eltex.androidschool.adapter.users.UserPagerAdapter
import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.databinding.FragmentAccountBinding
import com.eltex.androidschool.fragments.comments.CommentsBottomSheetFragment
import com.eltex.androidschool.fragments.events.NewOrUpdateEventFragment
import com.eltex.androidschool.fragments.jobs.NewOrUpdateJobFragment
import com.eltex.androidschool.fragments.posts.NewOrUpdatePostFragment
import com.eltex.androidschool.ui.offset.OffsetDecoration
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.jobs.JobUiModel
import com.eltex.androidschool.ui.posts.PostPagingMapper
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.common.initialsOfUsername
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.toast
import com.eltex.androidschool.viewmodel.auth.user.AccountViewModel
import com.eltex.androidschool.viewmodel.common.SharedViewModel
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel
import com.eltex.androidschool.viewmodel.events.eventwall.EventWallState
import com.eltex.androidschool.viewmodel.events.eventwall.EventWallViewModel
import com.eltex.androidschool.viewmodel.jobs.jobswall.JobState
import com.eltex.androidschool.viewmodel.jobs.jobswall.JobViewModel
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallViewModel
import com.eltex.androidschool.viewmodel.user.UserState
import com.eltex.androidschool.viewmodel.user.UserViewModel

import com.google.android.material.tabs.TabLayoutMediator

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Фрагмент, отображающий информацию о пользователе, включая его посты, события и места работы.
 * Этот фрагмент использует ViewPager2 для отображения вкладок с постами, событиями и местами работы.
 * Также предоставляет возможность обновления, удаления и создания новых постов, событий и мест работы.
 * Если идентификатор не передан, используется идентификатор пользователя по умолчанию из BuildConfig.
 *
 * @see Fragment Базовый класс для фрагментов в Android.
 * @see ViewPager2 Компонент для отображения вкладок с постами, событиями и местами работы.
 * @see RecyclerView Компонент для отображения списка постов, событий и мест работы.
 * @see UserViewModel ViewModel для управления данными о пользователе.
 * @see PostWallViewModel ViewModel для управления постами пользователя.
 * @see EventWallViewModel ViewModel для управления событиями пользователя.
 * @see JobViewModel ViewModel для управления местами работы пользователя.
 */
@AndroidEntryPoint
class AccountFragment : Fragment() {

    /**
     * Экземпляр ViewModel для работы с данными аккаунта пользователя.
     * Инициализируется с использованием делегата `by viewModels()`, который предоставляет экземпляр ViewModel,
     * связанный с текущим фрагментом или активностью.
     *
     * @property accountViewModel ViewModel для работы с данными аккаунта.
     * @see AccountViewModel
     * @see viewModels
     */
    private val accountViewModel: AccountViewModel by viewModels()

    /**
     * ViewModel для управления состоянием панели инструментов (Toolbar).
     *
     * Этот ViewModel используется для управления видимостью и состоянием элементов Toolbar в зависимости от текущего фрагмента.
     * Он предоставляет методы для скрытия или отображения кнопок настроек и списка пользователей.
     *
     * @see ToolBarViewModel ViewModel, который управляет состоянием Toolbar.
     * @see activityViewModels Делегат для получения ViewModel, привязанного к активности.
     */
    private val toolbarViewModel by activityViewModels<ToolBarViewModel>()

    /**
     * ViewModel для обмена данными между фрагментами.
     *
     * Этот ViewModel используется для хранения и передачи данных между фрагментами, которые не имеют прямого
     * взаимодействия друг с другом. В частности, он используется для отслеживания текущей вкладки в `ViewPager2`
     * и передачи этой информации в `BottomNavigationFragment` для управления поведением кнопки создания нового поста или события.
     *
     * @see SharedViewModel ViewModel, который хранит текущую вкладку в `ViewPager2`.
     * @see activityViewModels Делегат для получения ViewModel, привязанного к активности.
     */
    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        const val USER_ID = "USER_ID"
        const val IC_PROFILE = "IC_PROFILE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAccountBinding.inflate(layoutInflater)

        val accountUserId = accountViewModel.userId

        val userId: Long = arguments?.getLong(USER_ID) ?: accountUserId
        val icProfile: Boolean = arguments?.getBoolean(IC_PROFILE) != false

        /**
         * ViewModel для управления данными о пользователе.
         * Создается с использованием фабрики, которая принимает идентификатор пользователя.
         *
         * @see UserViewModel ViewModel, который управляет данными о пользователе.
         * @see viewModels Делегат для получения ViewModel, привязанного к фрагменту.
         */
        val userViewModel by viewModels<UserViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<UserViewModel.ViewModelFactory> { factory ->
                    factory.create(userId = userId)
                }
            }
        )

        /**
         * ViewModel для управления постами пользователя.
         * Создается с использованием фабрики, которая принимает идентификатор пользователя.
         *
         * @see PostWallViewModel ViewModel, который управляет постами пользователя.
         * @see viewModels Делегат для получения ViewModel, привязанного к фрагменту.
         */
        val postViewModel: PostWallViewModel by viewModels(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<PostWallViewModel.ViewModelFactory> { factory ->
                    factory.create(userId = userId)
                }
            }
        )

        /**
         * ViewModel для управления событиями пользователя.
         * Создается с использованием фабрики, которая принимает идентификатор пользователя.
         *
         * @see EventWallViewModel ViewModel, который управляет событиями пользователя.
         * @see viewModels Делегат для получения ViewModel, привязанного к фрагменту.
         */
        val eventViewModel by viewModels<EventWallViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<EventWallViewModel.ViewModelFactory> { factory ->
                    factory.create(userId = userId)
                }
            }
        )

        /**
         * ViewModel для управления местами работы пользователя.
         * Создается с использованием фабрики, которая принимает идентификатор пользователя.
         *
         * @see JobViewModel ViewModel для управления местами работы пользователя.
         * @see viewModels Делегат для получения ViewModel, привязанного к фрагменту.
         */
        val jobViewModel by viewModels<JobViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<JobViewModel.ViewModelFactory> { factory ->
                    factory.create(userId = userId)
                }
            }
        )

        binding.swiperRefresh.setOnRefreshListener {
            loadingDataFromTheViewModel(
                userId = userId,
                userViewModel = userViewModel,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel,
                jobViewModel = jobViewModel
            )
        }

        binding.retryButton.setOnClickListener {
            loadingDataFromTheViewModel(
                userId = userId,
                userViewModel = userViewModel,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel,
                jobViewModel = jobViewModel
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

        /**
         * Создает адаптер для отображения постов.
         *
         * @param postViewModel ViewModel для управления постами.
         * @param icProfile Флаг, указывающий, является ли текущий пользователь профилем текущего пользователя.
         * @return Адаптер для отображения постов.
         *
         * @see PostAdapter Адаптер для отображения постов.
         */
        val postAdapter = createPostAdapter(
            postViewModel = postViewModel,
            icProfile = icProfile,
            accountUserId = accountUserId,
            userId = userId,
        )

        /**
         * Создает адаптер для отображения событий.
         *
         * @param eventViewModel ViewModel для управления событиями.
         * @param icProfile Флаг, указывающий, является ли текущий пользователь профилем текущего пользователя.
         * @return Адаптер для отображения событий.
         *
         * @see EventAdapter Адаптер для отображения событий.
         */
        val eventAdapter = createEventAdapter(
            eventViewModel = eventViewModel, icProfile = icProfile, accountUserId = accountUserId,
        )

        /**
         * Создает адаптер для отображения мест работы.
         *
         * @param jobViewModel ViewModel для управления местами работы.
         * @param userId Идентификатор пользователя.
         * @return Адаптер для отображения мест работы.
         *
         * @see JobAdapter Адаптер для отображения мест работы.
         */
        val jobAdapter = createJobAdapter(
            jobViewModel = jobViewModel, userId = userId, accountUserId = accountUserId,
        )

        val offset: Int = resources.getDimensionPixelSize(R.dimen.list_offset)

        /**
         * Адаптер для ViewPager2, который объединяет адаптеры постов, событий и мест работы.
         *
         * @param postAdapter Адаптер для отображения постов.
         * @param eventAdapter Адаптер для отображения событий.
         * @param jobAdapter Адаптер для отображения мест работы.
         * @param offset Отступ для элементов в ViewPager2.
         * @param viewModel ViewModel для управления постами.
         *
         * @see UserPagerAdapter Адаптер для ViewPager2.
         */
        val pagerAdapter = UserPagerAdapter(
            postAdapter = postAdapter,
            eventAdapter = eventAdapter,
            jobAdapter = jobAdapter,
            offset = offset,
            viewModel = postViewModel,
            sharedViewModel = sharedViewModel,
        )

        binding.viewPagerPostsAndEvents.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerPostsAndEvents) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.posts)
                1 -> getString(R.string.events)
                2 -> getString(R.string.jobs)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()

        registerBackPressedCallback(binding = binding)

        binding.viewPagerPostsAndEvents.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    sharedViewModel.currentTab.value = position
                }
            }
        )

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdatePostFragment.POST_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            scrollToTopAndRefresh(
                binding = binding,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel,
                jobViewModel = jobViewModel,
                userId = userId
            )
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdateEventFragment.EVENT_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            scrollToTopAndRefresh(
                binding = binding,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel,
                jobViewModel = jobViewModel,
                userId = userId
            )
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewOrUpdateJobFragment.JOB_CREATED_OR_UPDATED_KEY, viewLifecycleOwner
        ) { _, _ ->
            scrollToTopAndRefresh(
                binding = binding,
                postViewModel = postViewModel,
                eventViewModel = eventViewModel,
                jobViewModel = jobViewModel,
                userId = userId
            )
        }

        userViewModelState(
            userViewModel = userViewModel,
            binding = binding,
            userId = userId,
            accountUserId = accountUserId,
        )

        postViewModelState(
            postViewModel = postViewModel, postAdapter = postAdapter
        )

        eventViewModelState(
            eventViewModel = eventViewModel, eventAdapter = eventAdapter
        )

        jobViewModelState(
            jobViewModel = jobViewModel, jobAdapter = jobAdapter
        )

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSettingsAndAllUsersLogoutVisible(
                            userId == accountUserId
                        )

                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSettingsAndAllUsersLogoutVisible(
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
     * Создает адаптер для отображения постов.
     *
     * @param postViewModel ViewModel для управления постами.
     * @param icProfile Флаг, указывающий, является ли текущий пользователь профилем текущего пользователя.
     * @return Адаптер для отображения постов.
     *
     * @see PostAdapter Адаптер для отображения постов.
     */
    private fun createPostAdapter(
        postViewModel: PostWallViewModel,
        icProfile: Boolean,
        accountUserId: Long,
        userId: Long,
    ) = PostAdapter(
        listener = object : PostAdapter.PostListener {
            override fun onLikeClicked(post: PostUiModel) {
                postViewModel.accept(message = PostWallMessage.Like(post = post))
            }

            override fun onShareClicked(post: PostUiModel) {}

            override fun onDeleteClicked(post: PostUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_post_title),
                    message = getString(R.string.delete_post_message)
                ) {
                    postViewModel.accept(message = PostWallMessage.Delete(post = post))
                }
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

            override fun onCommentsClicked(post: PostUiModel) {
                val commentsBottomSheetFragment = CommentsBottomSheetFragment(
                    postId = post.id,
                    accountUserId = accountUserId,
                    isAccount = accountUserId == userId,
                    isProfile = true,
                )

                commentsBottomSheetFragment.show(
                    parentFragmentManager,
                    commentsBottomSheetFragment.tag
                )
            }

            override fun onRetryPageClicked() {
                postViewModel.accept(PostWallMessage.Retry)
            }
        },

        context = requireContext(),
        currentUserId = accountUserId,
    )

    /**
     * Создает адаптер для отображения событий.
     *
     * @param eventViewModel ViewModel для управления событиями.
     * @param icProfile Флаг, указывающий, является ли текущий пользователь профилем текущего пользователя.
     * @return Адаптер для отображения событий.
     *
     * @see EventAdapter Адаптер для отображения событий.
     */
    private fun createEventAdapter(
        eventViewModel: EventWallViewModel,
        icProfile: Boolean,
        accountUserId: Long,
    ) = EventAdapter(
        object : EventAdapter.EventListener {
            override fun onLikeClicked(event: EventUiModel) {
                eventViewModel.likeById(event.id, event.likedByMe)
            }

            override fun onShareClicked(event: EventUiModel) {}

            override fun onParticipateClicked(event: EventUiModel) {
                eventViewModel.participateById(event.id, event.participatedByMe)
            }

            override fun onDeleteClicked(event: EventUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_event_title),
                    message = getString(R.string.delete_event_message)
                ) {
                    eventViewModel.deleteById(event.id)
                }
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
        currentUserId = accountUserId,
    )

    /**
     * Создает адаптер для отображения мест работы.
     *
     * @param jobViewModel ViewModel для управления местами работы.
     * @param userId Идентификатор пользователя.
     * @return Адаптер для отображения мест работы.
     *
     * @see JobAdapter Адаптер для отображения мест работы.
     */
    private fun createJobAdapter(
        jobViewModel: JobViewModel,
        userId: Long,
        accountUserId: Long,
    ) = JobAdapter(
        object : JobAdapter.JobListener {
            override fun onDeleteClicked(job: JobUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_job_title),
                    message = getString(R.string.delete_job_message)
                ) {
                    jobViewModel.deleteById(jobId = job.id)
                }
            }

            override fun onUpdateClicked(job: JobUiModel) {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(
                        R.id.action_BottomNavigationFragment_to_newOrUpdateJobFragment,
                        bundleOf(
                            NewOrUpdateJobFragment.JOB_ID to job.id,
                            NewOrUpdateJobFragment.JOB_NAME to job.name,
                            NewOrUpdateJobFragment.JOB_POSITION to job.position,
                            NewOrUpdateJobFragment.JOB_START to job.start,
                            NewOrUpdateJobFragment.JOB_FINISH to job.finish,
                            NewOrUpdateJobFragment.JOB_LINK to job.link,
                            NewOrUpdateJobFragment.IS_UPDATE to true,
                        ),
                        NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    )
            }
        },

        context = requireContext(),
        currentUserId = accountUserId,
        authorId = userId,
    )

    /**
     * Наблюдает за состоянием ViewModel пользователя и обновляет UI в зависимости от текущего состояния.
     * Этот метод связывает состояние ViewModel с элементами интерфейса, такими как ProgressBar, SwipeRefreshLayout,
     * TextView для ошибок, а также отображает данные пользователя (аватар, имя и т.д.).
     *
     * @param userViewModel Экземпляр [UserViewModel], который предоставляет состояние пользователя.
     * @param binding Экземпляр [FragmentAccountBinding], используемый для доступа к элементам интерфейса.
     * @param userId Идентификатор пользователя, используемый для проверки и отображения данных.
     *
     * @see UserState Состояние пользователя, которое содержит данные о загрузке, ошибках и информации о пользователе.
     * @see FragmentAccountBinding Связывает элементы интерфейса с кодом.
     * @see UserViewModel ViewModel, управляющая состоянием пользователя.
     *
     * @throws NullPointerException Если контекст или ресурсы недоступны.
     *
     * @property userState Текущее состояние пользователя, которое может быть:
     * - [UserState.isEmptyLoading] — состояние загрузки.
     * - [UserState.isRefreshing] — состояние обновления.
     * - [UserState.isEmptyError] — состояние ошибки.
     * - [UserState.users] — данные пользователя.
     */
    private fun userViewModelState(
        userViewModel: UserViewModel,
        binding: FragmentAccountBinding,
        userId: Long,
        accountUserId: Long,
    ) {
        userViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { userState: UserState ->
                binding.progressBar.isVisible = userState.isEmptyLoading
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

                userState.users?.firstOrNull()?.let { user: UserData ->
                    binding.nameUser.text = user.name

                    binding.skeletonAttachment.showSkeleton()

                    if (!user.avatar.isNullOrEmpty()) {
                        Glide.with(binding.root)
                            .load(user.avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    showPlaceholder(binding, user)

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
                                    binding.initial.isVisible = false

                                    return false
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .error(R.drawable.ic_404_24)
                            .thumbnail(
                                Glide.with(binding.root)
                                    .load(user.avatar)
                                    .override(50, 50)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                            )
                            .into(binding.avatarUser)
                    } else {
                        showPlaceholder(binding, user)
                    }

                    if (userId != accountUserId) {
                        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)

                        toolbar.title = user.login
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Наблюдает за состоянием ViewModel событий и обновляет адаптер событий в зависимости от текущего состояния.
     * Этот метод связывает состояние ViewModel с адаптером событий, чтобы отображать список событий на экране.
     *
     * @param eventViewModel Экземпляр [EventWallViewModel], который предоставляет состояние событий.
     * @param eventAdapter Экземпляр [EventAdapter], используемый для отображения списка событий.
     *
     * @see EventWallState Состояние событий, которое содержит список событий.
     * @see EventWallViewModel ViewModel, управляющая состоянием событий.
     * @see EventAdapter Адаптер для отображения списка событий.
     */
    private fun eventViewModelState(
        eventViewModel: EventWallViewModel,
        eventAdapter: EventAdapter
    ) {
        eventViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { eventState: EventWallState ->
                eventAdapter.submitList(eventState.events)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Наблюдает за состоянием ViewModel постов и обновляет адаптер постов в зависимости от текущего состояния.
     * Этот метод связывает состояние ViewModel с адаптером постов, чтобы отображать список постов на экране.
     * Используется маппер [PostPagingMapper] для преобразования состояния в список постов.
     *
     * @param postViewModel Экземпляр [PostWallViewModel], который предоставляет состояние постов.
     * @param postAdapter Экземпляр [PostAdapter], используемый для отображения списка постов.
     *
     * @see PostWallState Состояние постов, которое содержит данные о постах.
     * @see PostWallViewModel ViewModel, управляющая состоянием постов.
     * @see PostAdapter Адаптер для отображения списка постов.
     * @see PostPagingMapper Маппер для преобразования состояния в список постов.
     */
    private fun postViewModelState(
        postViewModel: PostWallViewModel,
        postAdapter: PostAdapter,
    ) {
        postViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { postState: PostWallState ->
                postAdapter.submitList(
                    PostPagingMapper.map(state = postState)
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Наблюдает за состоянием ViewModel вакансий и обновляет адаптер вакансий в зависимости от текущего состояния.
     * Этот метод связывает состояние ViewModel с адаптером вакансий, чтобы отображать список вакансий на экране.
     *
     * @param jobViewModel Экземпляр [JobViewModel], который предоставляет состояние вакансий.
     * @param jobAdapter Экземпляр [JobAdapter], используемый для отображения списка вакансий.
     *
     * @see JobState Состояние вакансий, которое содержит список вакансий.
     * @see JobViewModel ViewModel, управляющая состоянием вакансий.
     * @see JobAdapter Адаптер для отображения списка вакансий.
     */
    private fun jobViewModelState(
        jobViewModel: JobViewModel,
        jobAdapter: JobAdapter
    ) {
        jobViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { stateJob: JobState ->
                jobAdapter.submitList(stateJob.jobs)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * Загружает данные о пользователе, его постах, событиях и местах работы из ViewModel.
     * Используется для инициализации данных при создании фрагмента или обновлении данных.
     *
     * @param userId Идентификатор пользователя, для которого загружаются данные.
     * @param userViewModel ViewModel, связанная с данными о пользователе.
     * @param postViewModel ViewModel для управления постами пользователя.
     * @param eventViewModel ViewModel для управления событиями пользователя.
     * @param jobViewModel ViewModel для управления местами работы пользователя.
     * @param causeVibration Вызов вибрации (по умолчанию = true).
     *
     * @see UserViewModel.getUserById
     * @see PostWallViewModel.accept
     * @see EventWallViewModel.loadEventsByAuthor
     * @see JobViewModel.load
     */
    private fun loadingDataFromTheViewModel(
        userId: Long,
        userViewModel: UserViewModel,
        postViewModel: PostWallViewModel,
        eventViewModel: EventWallViewModel,
        jobViewModel: JobViewModel,
        causeVibration: Boolean = true
    ) {
        if (causeVibration) requireContext().singleVibrationWithSystemCheck(35)

        userViewModel.getUserById(userId = userId)
        postViewModel.accept(message = PostWallMessage.Refresh)
        eventViewModel.loadEventsByAuthor(authorId = userId)
        jobViewModel.getJobsByUserId(userId = userId)
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
                NewOrUpdateEventFragment.EVENT_DATE to event.dateEvent,
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

    /**
     * Прокручивает RecyclerView на самый верх и обновляет данные в зависимости от текущей вкладки.
     *
     * Эта функция обновляет список постов или событий в зависимости от текущей вкладки в `ViewPager2`
     * и прокручивает соответствующий `RecyclerView` на самый верх. Если выбрана вкладка с постами,
     * обновляется список постов, а если выбрана вкладка с событиями — обновляется список событий.
     *
     * @param binding Привязка для макета фрагмента, содержащего `ViewPager2` и `RecyclerView`.
     * @param postViewModel ViewModel для управления состоянием постов.
     * @param eventViewModel ViewModel для управления состоянием событий.
     * @param userId Идентификатор пользователя, для которого загружаются данные.
     *
     * @see FragmentAccountBinding Привязка для макета фрагмента.
     * @see PostWallViewModel ViewModel для управления постами.
     * @see EventWallViewModel ViewModel для управления событиями.
     * @see RecyclerView Компонент для отображения списка постов или событий.
     * @see ViewPager2 Компонент для отображения вкладок с постами и событиями.
     *
     * @throws IllegalStateException Может быть выброшено, если `ViewPager2` не содержит ожидаемых вкладок.
     */
    private fun scrollToTopAndRefresh(
        binding: FragmentAccountBinding,
        postViewModel: PostWallViewModel,
        eventViewModel: EventWallViewModel,
        jobViewModel: JobViewModel,
        userId: Long
    ) {
        binding.viewPagerPostsAndEvents.currentItem.let { currentItem ->
            when (currentItem) {
                0 -> {
                    postViewModel.accept(message = PostWallMessage.Refresh)

                    val postRecyclerView = binding.viewPagerPostsAndEvents
                        .getChildAt(0)?.findViewById<RecyclerView>(R.id.postsRecyclerView)

                    postRecyclerView?.smoothScrollToPosition(0)
                }

                1 -> {
                    eventViewModel.loadEventsByAuthor(authorId = userId)

                    val eventRecyclerView = binding.viewPagerPostsAndEvents
                        .getChildAt(1)?.findViewById<RecyclerView>(R.id.eventsRecyclerView)

                    eventRecyclerView?.smoothScrollToPosition(0)
                }

                2 -> {
                    jobViewModel.getJobsByUserId(userId = userId)

                    val jobRecyclerView = binding.viewPagerPostsAndEvents
                        .getChildAt(2)?.findViewById<RecyclerView>(R.id.jobsRecyclerView)

                    jobRecyclerView?.smoothScrollToPosition(0)
                }

                else -> {}
            }
        }
    }

    /**
     * Показывает диалоговое окно с подтверждением удаления.
     *
     * Эта функция отображает Material 3 диалог с двумя кнопками: "Отмена" и "Удалить".
     * Кнопка "Отмена" закрывает диалог без выполнения каких-либо действий, а кнопка "Удалить"
     * вызывает переданный коллбэк `onDeleteConfirmed`, который выполняет удаление.
     *
     * @param title Заголовок диалога. Обычно это текст, который кратко описывает действие, например, "Удаление поста".
     * @param message Основной текст диалога. Это более подробное описание действия, например, "Вы уверены, что хотите удалить этот пост?".
     * @param onDeleteConfirmed Коллбэк, который вызывается при нажатии на кнопку "Удалить". Этот коллбэк должен содержать логику удаления.
     *
     * @see Context.showMaterialDialogWithTwoButtons Функция, которая используется для отображения диалога с двумя кнопками.
     */
    private fun showDeleteConfirmationDialog(
        title: String,
        message: String,
        onDeleteConfirmed: () -> Unit
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }

    /**
     * Регистрирует обработчик нажатия системной кнопки "Назад" для управления поведением ViewPager2.
     *
     * Этот метод создает и регистрирует [OnBackPressedCallback], который перехватывает нажатие кнопки "Назад".
     *
     * @param binding [FragmentAccountBinding] Привязка для макета фрагмента, содержащего ViewPager2.
     *
     * @see OnBackPressedCallback Класс, используемый для перехвата нажатия кнопки "Назад".
     * @see FragmentAccountBinding Привязка для макета фрагмента, содержащего ViewPager2.
     * @see ViewPager2 Компонент для отображения вкладок с постами, событиями и местами работы.
     * @see requireActivity Метод для получения активности, связанной с фрагментом.
     * @see viewLifecycleOwner Владелец жизненного цикла, связанный с View фрагмента.
     *
     * @property callback Обработчик нажатия кнопки "Назад", который управляет поведением ViewPager2.
     */
    private fun registerBackPressedCallback(binding: FragmentAccountBinding) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentItem = binding.viewPagerPostsAndEvents.currentItem

                if (currentItem != 0) {
                    binding.viewPagerPostsAndEvents.setCurrentItem(0, true)
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    /**
     * Отображает плейсхолдер для пользователя, устанавливая изображение аватара,
     * инициализируя текст с инициалами пользователя, устанавливая цвет текста,
     * отображая оригинальное состояние макета и делая инициал видимым.
     *
     * @param binding Объект [FragmentAccountBinding], используемый для доступа
     * к элементам интерфейса.
     * @param user Объект [UserData], содержащий информацию о пользователе.
     * @see initialsOfUsername
     */
    private fun showPlaceholder(
        binding: FragmentAccountBinding,
        user: UserData
    ) {
        binding.skeletonAttachment.showOriginal()

        binding.avatarUser.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.active_element
            )
        )
        binding.initial.text = initialsOfUsername(user.name)
        binding.initial.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.initial.isVisible = true
    }
}
