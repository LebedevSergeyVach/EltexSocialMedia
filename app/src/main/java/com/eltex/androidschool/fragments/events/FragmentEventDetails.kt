package com.eltex.androidschool.fragments.events

import android.graphics.drawable.Drawable

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.avatars.AvatarsAdapter
import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.databinding.FragmentEventDetailsBinding
import com.eltex.androidschool.fragments.common.TextCopyBottomSheetFragment
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.offset.OffsetDecorationLikesAvatar
import com.eltex.androidschool.utils.common.initialsOfUsername
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.viewmodel.events.eventdetails.EventDetailsState
import com.eltex.androidschool.viewmodel.events.eventdetails.EventDetailsViewModel

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentEventDetails : Fragment() {

    companion object {
        const val EVENT_ID = "EVENT_ID"
        const val ACCOUNT_ID = "ACCOUNT_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        val eventId: Long = arguments?.getLong(EVENT_ID) ?: -1L
        val accountId: Long = arguments?.getLong(ACCOUNT_ID) ?: -1L

        val viewModel: EventDetailsViewModel by viewModels(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<EventDetailsViewModel.ViewModelFactory> { factory ->
                    factory.create(eventId = eventId)
                }
            }
        )

        binding.retryButton.setOnClickListener {
            viewModel.loadEvent(eventId = eventId)
        }

        val likesAdapter = AvatarsAdapter()
        val participationAdapter = AvatarsAdapter()
        recyclerView(
            likesAdapter = likesAdapter,
            participationAdapter = participationAdapter,
            binding = binding
        )

        observationViewModelState(
            viewModel = viewModel,
            binding = binding,
            accountId = accountId,
            eventId = eventId,
            likesAdapter = likesAdapter,
            participationAdapter = participationAdapter
        )

        return binding.root
    }

    private fun observationViewModelState(
        viewModel: EventDetailsViewModel,
        binding: FragmentEventDetailsBinding,
        accountId: Long,
        eventId: Long,
        likesAdapter: AvatarsAdapter,
        participationAdapter: AvatarsAdapter,
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .onEach { state: EventDetailsState ->
                binding.apply {
                    scrollViewPostDetails.isVisible = !state.isEmptyError && !state.isEmptyLoading
                    skeletonLayoutLoadDataPostDetails.isVisible = state.isEmptyLoading
                    errorGroup.isVisible = state.isEmptyError
                }

                if (state.isEmptyLoading) {
                    binding.skeletonLayoutLoadDataPostDetails.showSkeleton()
                } else {
                    binding.skeletonLayoutLoadDataPostDetails.showOriginal()
                }

                state.statusLoadEvent.throwableOrNull?.let { error: Throwable ->
                    binding.errorText.text = error.getErrorText(requireContext())
                }

                state.event?.let {
                    renderingUserAvatar(event = state.event, binding = binding)
                    renderImageStateAndSkeleton(state = state, binding = binding)
                    renderDataEvent(binding = binding, event = state.event, accountId = accountId)
                    updateLikeByMe(binding = binding, event = state.event)
                    updateParticipationByMe(binding = binding, event = state.event)

                    binding.like.setOnClickListener {
                        requireContext().singleVibrationWithSystemCheck(35L)

                        viewModel.likeById(eventId = eventId, likedByMe = state.event.likedByMe)
                        updateLikeByMe(binding = binding, event = state.event)
                    }

                    binding.participation.setOnClickListener {
                        requireContext().singleVibrationWithSystemCheck(35L)

                        viewModel.participationById(
                            eventId = eventId,
                            participatedByMe = state.event.participatedByMe
                        )
                        updateParticipationByMe(binding = binding, event = state.event)
                    }

                    onUpdateEventClicked(binding = binding, event = state.event)

                    callDisplayTextCopyBottomSheet(binding = binding, event = state.event)

                    if (state.isSuccessLoad) {
                        likesAdapter.submitList(state.event.likesListUsers)
                        participationAdapter.submitList(state.event.participationListUsers)
                    }

                    if (state.isLikePressed) {
                        likesAdapter.submitList(state.event.likesListUsers)
                    }

                    if (state.isParticipatePressed) {
                        participationAdapter.submitList(state.event.participationListUsers)
                    }
                }
            }
            .launchIn(scope = viewLifecycleOwner.lifecycleScope)
    }

    private fun recyclerView(
        binding: FragmentEventDetailsBinding,
        likesAdapter: AvatarsAdapter,
        participationAdapter: AvatarsAdapter,
    ) {
        binding.likesRecyclerView.adapter = likesAdapter
        binding.likesRecyclerView.addItemDecoration(
            OffsetDecorationLikesAvatar(
                offsetLeft = -30,
                orientation = LinearLayout.HORIZONTAL
            )
        )

        binding.participationRecyclerView.adapter = participationAdapter
        binding.participationRecyclerView.addItemDecoration(
            OffsetDecorationLikesAvatar(
                offsetLeft = -30,
                orientation = LinearLayout.HORIZONTAL
            )
        )
    }

    private fun updateLikeByMe(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
    ) {
        binding.like.isSelected = event.likedByMe
    }

    private fun updateParticipationByMe(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
    ) {
        binding.participation.isSelected = event.participatedByMe
    }

    private fun renderDataEvent(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
        accountId: Long,
    ) {
        binding.apply {
            author.text = event.author
            published.text = event.published
            loginAndJob.text = event.authorJob ?: getString(R.string.user)
            content.text = event.content
            optionConducting.text = event.optionConducting
            link.text = event.link
            dateEvent.text = event.dateEvent
            like.text = event.likes.toString()
            participation.text = event.participates.toString()
            buttonUpdate.isVisible = event.authorId == accountId
        }
    }

    private fun onUpdateEventClicked(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
    ) {
        binding.buttonUpdate.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)

            requireParentFragment().findNavController()
                .navigate(
                    R.id.action_fragmentEventDetails_to_newOrUpdateEventFragment,
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
    }

    private fun renderImageStateAndSkeleton(
        state: EventDetailsState,
        binding: FragmentEventDetailsBinding,
    ) {
        binding.skeletonImageAttachment.showSkeleton()

        state.event?.attachment?.let { attachment: Attachment ->
            renderingImageAttachment(
                binding = binding,
                attachment = attachment,
            )
        } ?: {
            binding.skeletonImageAttachment.showOriginal()
            binding.imageAttachment.isVisible = false
        }
    }

    private fun renderingImageAttachment(
        binding: FragmentEventDetailsBinding,
        attachment: Attachment,
    ) {
        Glide.with(binding.root)
            .load(attachment.url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.skeletonImageAttachment.showOriginal()
                    binding.imageAttachment.setImageResource(R.drawable.ic_404_24)

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.skeletonImageAttachment.showOriginal()

                    return false
                }
            })
            .thumbnail(
                Glide.with(binding.root)
                    .load(attachment.url)
                    .override(50, 50)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .error(R.drawable.ic_404_24)
            .into(binding.imageAttachment)
    }

    private fun renderingUserAvatar(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
    ) {
        showPlaceholder(event = event, binding = binding)
        binding.skeletonLayoutAvatar.showSkeleton()

        if (!event.authorAvatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(event.authorAvatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showPlaceholder(event = event, binding = binding)

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.skeletonLayoutAvatar.showOriginal()
                        binding.initial.isVisible = false

                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .error(R.drawable.error_placeholder)
                .thumbnail(
                    Glide.with(binding.root)
                        .load(event.authorAvatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            showPlaceholder(event = event, binding = binding)
        }
    }

    private fun showPlaceholder(binding: FragmentEventDetailsBinding, event: EventUiModel) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = initialsOfUsername(name = event.author)
        binding.initial.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.skeletonLayoutAvatar.showOriginal()
        binding.initial.isVisible = true
    }

    private fun callDisplayTextCopyBottomSheet(
        binding: FragmentEventDetailsBinding,
        event: EventUiModel,
    ) {
        binding.imageAttachment.setOnClickListener {
            displayTextCopyBottomSheet(event = event)
        }

        binding.content.setOnClickListener {
            displayTextCopyBottomSheet(event = event)
        }

        binding.dateEvent.setOnClickListener {
            displayTextCopyBottomSheet(event = event)
        }

        binding.published.setOnClickListener {
            displayTextCopyBottomSheet(event = event)
        }

        binding.optionConducting.setOnClickListener {
            displayTextCopyBottomSheet(event = event)
        }

        binding.info.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            displayTextCopyBottomSheet(event = event)
        }
    }

    private fun displayTextCopyBottomSheet(event: EventUiModel) {
        val textCopyBottomSheetFragment = TextCopyBottomSheetFragment(
            textCopy = buildString {
                append(event.author)
                append("\n\n")
                append(event.optionConducting)
                append("\n")
                append(event.dateEvent)
                append("\n\n")
                append(event.content)
                append("\n\n")
                append(event.link)
            },
            imageUrl = event.attachment?.url
        )

        textCopyBottomSheetFragment.show(
            parentFragmentManager,
            textCopyBottomSheetFragment.tag
        )
    }
}
