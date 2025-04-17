package com.eltex.androidschool.fragments.comments

import android.annotation.SuppressLint

import android.os.Bundle
import android.text.Editable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged

import androidx.fragment.app.viewModels

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.comments.CommentsAdapter
import com.eltex.androidschool.databinding.FragmentCommentsBinding
import com.eltex.androidschool.fragments.users.AccountFragment
import com.eltex.androidschool.ui.comments.CommentUiModel
import com.eltex.androidschool.ui.offset.OffsetDecorationComments
import com.eltex.androidschool.utils.extensions.ErrorUtils.getErrorText
import com.eltex.androidschool.utils.extensions.createSkeletonConfig
import com.eltex.androidschool.utils.extensions.getCommentsText
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.toast
import com.eltex.androidschool.utils.extensions.vibrateWithEffect
import com.eltex.androidschool.utils.helper.LoggerHelper
import com.eltex.androidschool.viewmodel.comments.CommentsState
import com.eltex.androidschool.viewmodel.comments.CommentsViewModel

import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CommentsBottomSheetFragment(
    private val postId: Long,
    private val accountUserId: Long,
    private val isAccount: Boolean,
    private val isProfile: Boolean,
) : BottomSheetDialogFragment() {

    val viewModel: CommentsViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<CommentsViewModel.ViewModelFactory> { factory ->
                factory.create(postId = postId)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCommentsBinding.inflate(inflater, container, false)

        val adapter: CommentsAdapter = createCommentsAdapter()

        listManagementInRecyclerView(binding = binding, adapter = adapter)

        val skeleton: Skeleton =
            binding.list.applySkeleton(
                listItemLayoutResId = R.layout.item_skeleton_comment,
                config = requireContext().createSkeletonConfig(
                    shimmerDurationInMillis = 1000,
                    shimmerAngle = 20,
                    maskCornerRadius = 50f
                ),
            )

        observingStateOfViewModel(binding = binding, adapter = adapter, skeleton = skeleton)

        sendNewComment(binding = binding, adapter = adapter)

        return binding.root
    }

    private fun createCommentsAdapter(): CommentsAdapter = CommentsAdapter(
        listener = object : CommentsAdapter.CommentListener {
            override fun onLikeClicked(comment: CommentUiModel) {
                viewModel.likeCommentById(
                    postId = postId,
                    commentId = comment.id,
                    likedByMe = comment.likedByMe
                )
            }

            override fun onDeleteClicked(comment: CommentUiModel) {
                showDeleteConfirmationDialog(
                    title = getString(R.string.delete_comment_title),
                    message = getString(R.string.delete_comment_message),
                ) {
                    viewModel.deleteCommentById(
                        postId = postId,
                        commentId = comment.id
                    )
                }
            }

            override fun onGetUserClicked(comment: CommentUiModel) {
                if (BuildConfig.DEBUG)
                    LoggerHelper.i("postId = $postId, accountUserId = $accountUserId, isProfile = $isProfile")

                if (!isProfile) {
                    dismiss()

                    if (comment.authorId == accountUserId) {
                        val bottomNav = requireParentFragment().requireParentFragment()
                            .requireView().findViewById<BottomNavigationView>(R.id.bottomNavigation)

                        bottomNav.selectedItemId = R.id.accountFragment

                        findNavController().navigate(
                            R.id.accountFragment,
                            null,
                            NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build()
                        )
                    } else {
                        requireParentFragment().requireParentFragment().findNavController()
                            .navigate(
                                R.id.action_BottomNavigationFragment_to_userFragment,
                                bundleOf(
                                    AccountFragment.USER_ID to comment.authorId,
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
                } else {
                    if (isAccount && comment.authorId == accountUserId) {
                        dismiss()
                    }
                }
            }
        },

        context = requireContext(),
        currentUserId = accountUserId,
    )

    private fun listManagementInRecyclerView(
        binding: FragmentCommentsBinding,
        adapter: CommentsAdapter
    ) {
        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecorationComments(
                offset = resources.getDimensionPixelSize(R.dimen.list_offset_comment)
            )
        )

        binding.retryButton.setOnClickListener {
            viewModel.load(postId = postId)
        }
    }

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

    fun observingStateOfViewModel(
        binding: FragmentCommentsBinding,
        adapter: CommentsAdapter,
        skeleton: Skeleton,
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .onEach { stateComments: CommentsState ->
                binding.apply {
                    errorGroup.isVisible = stateComments.isEmptyError
                    cardNewComment.isVisible = !stateComments.isEmptyError
                    emptyComments.isVisible =
                        stateComments.isEmptySuccess || stateComments.isEmptyIdle
                    list.isVisible = !stateComments.isEmptySuccess && !stateComments.isEmptyError
                    textCommentsSize.isVisible =
                        !stateComments.isEmptySuccess && !stateComments.isEmptyError && !stateComments.isEmptyLoading
                    buttonSendComment.isEnabled = stateComments.isButtonEnabled
                    buttonSendComment.animate()
                        .alpha(if (stateComments.isButtonEnabled) 1f else 0.5f)
                        .setDuration(200)
                        .start()
                }

                binding.textCommentsSize.text =
                    (stateComments.comments?.size ?: 0).getCommentsText(requireContext())

                stateComments.statusComments.throwableOrNull?.let { error: Throwable ->
                    binding.errorText.text = error.getErrorText(requireContext())

                    if (stateComments.isRefreshError) {
                        requireContext().toast(binding.errorText.text.toString())
                        viewModel.consumerError()
                    }
                }

                when {
                    stateComments.isEmptyLoading -> {
                        skeleton.showSkeleton()
                    }

                    stateComments.isFirstSecaucus -> {
                        skeleton.showOriginal()
                    }

                    stateComments.isEmptySuccess -> {
                        skeleton.showOriginal()
                    }
                }

                binding.list.post {
                    adapter.submitList(stateComments.comments) {
                        binding.list.invalidateItemDecorations()
                    }
                }
            }
            .launchIn(scope = viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun sendNewComment(
        binding: FragmentCommentsBinding,
        adapter: CommentsAdapter
    ) {
        binding.editTextContentNewComment.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                expandBottomSheet()
            }
        }

        binding.editTextContentNewComment.setOnTouchListener { _, _ ->
            expandBottomSheet()
            false
        }

        binding.editTextContentNewComment.doAfterTextChanged { text: Editable? ->
            viewModel.updateButtonState(comment = text?.toString() ?: "")
        }

        binding.buttonSendComment.setOnClickListener {
            val newComment =
                binding.editTextContentNewComment.text?.toString().orEmpty().trimStart().trimEnd()

            if (newComment.isNotEmpty()) {
                viewModel.sendCommentByIdPost(postId = postId, content = newComment)
                binding.editTextContentNewComment.text?.clear()

                binding.list.postDelayed({
                    if (adapter.itemCount > 0) {
                        binding.list.smoothScrollToPosition(adapter.itemCount - 1)
                    }
                }, 100)
            } else {
                requireContext().vibrateWithEffect(100L)
                requireContext().toast(R.string.error_text_event_is_empty)
            }
        }
    }

    private fun expandBottomSheet() {
        (dialog as? BottomSheetDialog)?.let { dialog: BottomSheetDialog ->
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
