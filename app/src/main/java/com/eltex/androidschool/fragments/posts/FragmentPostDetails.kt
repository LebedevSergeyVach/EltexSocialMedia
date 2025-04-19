package com.eltex.androidschool.fragments.posts

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
import com.eltex.androidschool.databinding.FragmentPostDetailsBinding
import com.eltex.androidschool.fragments.comments.CommentsBottomSheetFragment
import com.eltex.androidschool.fragments.common.TextCopyBottomSheetFragment
import com.eltex.androidschool.ui.offset.OffsetDecorationLikesAvatar
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.common.initialsOfUsername
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.viewmodel.posts.postdetails.PostDetailsState
import com.eltex.androidschool.viewmodel.posts.postdetails.PostDetailsViewModel

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentPostDetails : Fragment() {

    companion object {
        const val POST_ID = "POST_ID"
        const val ACCOUNT_ID = "ACCOUNT_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        val postId: Long = arguments?.getLong(POST_ID) ?: -1L
        val accountId: Long = arguments?.getLong(ACCOUNT_ID) ?: -1L

        val viewModel: PostDetailsViewModel by viewModels(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<PostDetailsViewModel.ViewModelFactory> { factory ->
                    factory.create(postId = postId)
                }
            }
        )

        binding.retryButton.setOnClickListener {
            viewModel.loadPost(postId = postId)
        }

        val likesAdapter = AvatarsAdapter()
        recyclerView(binding = binding, likesAdapter = likesAdapter)

        observationViewModelState(
            viewModel = viewModel,
            binding = binding,
            accountId = accountId,
            postId = postId,
            likesAdapter = likesAdapter,
        )

        return binding.root
    }

    private fun observationViewModelState(
        viewModel: PostDetailsViewModel,
        binding: FragmentPostDetailsBinding,
        accountId: Long,
        postId: Long,
        likesAdapter: AvatarsAdapter
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .onEach { state: PostDetailsState ->
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

                state.statusLoadPost.throwableOrNull?.let { error: Throwable ->
                    binding.errorText.text = error.getErrorText(requireContext())
                }

                state.post?.let {
                    renderingUserAvatar(post = state.post, binding = binding)
                    renderImageStateAndSkeleton(state = state, binding = binding)
                    renderDataPost(binding = binding, post = state.post, accountId = accountId)
                    updateLikeByMe(binding = binding, post = state.post)

                    binding.like.setOnClickListener {
                        requireContext().singleVibrationWithSystemCheck(35L)

                        viewModel.likeById(postId = postId, likedByMe = state.post.likedByMe)
                        updateLikeByMe(binding = binding, post = state.post)
                    }

                    onUpdatePostClicked(binding = binding, post = state.post)

                    callDisplayTextCopyBottomSheet(binding = binding, post = state.post)
                    displayCommentBottomSheet(
                        binding = binding,
                        post = state.post,
                        accountId = accountId
                    )

                    likesAdapter.submitList(state.post.likesListUsers)
                }
            }
            .launchIn(scope = viewLifecycleOwner.lifecycleScope)
    }

    private fun recyclerView(
        binding: FragmentPostDetailsBinding,
        likesAdapter: AvatarsAdapter
    ) {
        binding.likesRecyclerView.adapter = likesAdapter
        binding.likesRecyclerView.addItemDecoration(
            OffsetDecorationLikesAvatar(
                offsetLeft = -30,
                orientation = LinearLayout.HORIZONTAL
            )
        )
    }

    private fun updateLikeByMe(
        binding: FragmentPostDetailsBinding,
        post: PostUiModel
    ) {
        binding.like.isSelected = post.likedByMe
    }

    private fun renderDataPost(
        binding: FragmentPostDetailsBinding,
        post: PostUiModel,
        accountId: Long,
    ) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            loginAndJob.text = post.authorJob ?: getString(R.string.user)
            content.text = post.content
            like.text = post.likes.toString()
            buttonUpdate.isVisible = post.authorId == accountId
        }
    }

    private fun onUpdatePostClicked(
        binding: FragmentPostDetailsBinding,
        post: PostUiModel
    ) {
        binding.buttonUpdate.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)

            requireParentFragment().findNavController()
                .navigate(
                    R.id.action_fragmentPostDetails_to_newOrUpdatePostFragment,
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
    }

    private fun renderImageStateAndSkeleton(
        state: PostDetailsState,
        binding: FragmentPostDetailsBinding
    ) {
        binding.skeletonImageAttachment.showSkeleton()

        state.post?.attachment?.let { attachment: Attachment ->
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
        binding: FragmentPostDetailsBinding,
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
        post: PostUiModel,
        binding: FragmentPostDetailsBinding
    ) {
        showPlaceholder(post = post, binding = binding)
        binding.skeletonLayoutAvatar.showSkeleton()

        if (!post.authorAvatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(post.authorAvatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showPlaceholder(post = post, binding = binding)

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
                        .load(post.authorAvatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            showPlaceholder(post = post, binding = binding)
        }
    }

    private fun showPlaceholder(binding: FragmentPostDetailsBinding, post: PostUiModel) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = initialsOfUsername(name = post.author)
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
        binding: FragmentPostDetailsBinding,
        post: PostUiModel,
    ) {
        binding.content.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            displayTextCopyBottomSheet(post = post)
        }

        binding.published.setOnClickListener {
            displayTextCopyBottomSheet(post = post)
        }

        binding.info.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            displayTextCopyBottomSheet(post = post)
        }
    }

    private fun displayTextCopyBottomSheet(post: PostUiModel) {
        val textCopyBottomSheetFragment = TextCopyBottomSheetFragment(
            textCopy = buildString {
                append(post.author)
                append("\n\n")
                append(post.content)
            }
        )

        textCopyBottomSheetFragment.show(
            parentFragmentManager,
            textCopyBottomSheetFragment.tag
        )
    }

    private fun displayCommentBottomSheet(
        binding: FragmentPostDetailsBinding,
        post: PostUiModel,
        accountId: Long,
    ) {
        binding.comments.setOnClickListener {
            val commentsBottomSheetFragment = CommentsBottomSheetFragment(
                postId = post.id,
                accountUserId = accountId,
                isPostDetails = true
            )

            commentsBottomSheetFragment.show(
                parentFragmentManager,
                commentsBottomSheetFragment.tag
            )
        }
    }
}
