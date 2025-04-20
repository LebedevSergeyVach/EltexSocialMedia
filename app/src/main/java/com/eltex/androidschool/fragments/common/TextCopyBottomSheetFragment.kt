package com.eltex.androidschool.fragments.common

import android.app.DownloadManager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

import android.net.Uri

import android.os.Bundle
import android.os.Environment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.webkit.URLUtil

import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomSheetCopyTextBinding
import com.eltex.androidschool.utils.extensions.showTopSnackbar
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.toast

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TextCopyBottomSheetFragment(
    private val textCopy: String,
    private val imageUrl: String? = null,
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomSheetCopyTextBinding.inflate(inflater, container, false)

        binding.apply {
            binding.cardDownloadImage.isVisible = !imageUrl.isNullOrEmpty()
        }

        binding.textCopy.text = textCopy

        binding.buttonCloseFragmentSheet.setOnClickListener {
            dismiss()
        }

        binding.cardCopyText.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            copyTextToClipboard(text = textCopy)
            requireContext().showTopSnackbar(
                message = getString(R.string.text_is_copied),
                iconRes = R.drawable.ic_copy_24,
            )
            dismiss()
        }

        binding.cardShareText.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            shareText(text = textCopy)
            requireContext().showTopSnackbar(
                message = getString(R.string.shared),
                iconRes = R.drawable.ic_share_24,
            )
            dismiss()
        }

        binding.cardDownloadImage.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)

            imageUrl?.let { url ->
                try {
                    requireContext().showTopSnackbar(
                        message = getString(R.string.download_start),
                        iconRes = R.drawable.ic_download_24,
                    )
                    downloadImage(url)
                } catch (e: Exception) {
                    e.printStackTrace()
                    requireContext().showTopSnackbar(
                        message = getString(R.string.image_upload_error),
                        iconRes = R.drawable.ic_cross_24,
                        iconTintRes = R.color.error_color
                    )
                }
            }
                ?: requireContext().showTopSnackbar(
                    message = getString(R.string.image_upload_error),
                    iconRes = R.drawable.ic_cross_24,
                    iconTintRes = R.color.error_color
                )

            dismiss()
        }

        return binding.root
    }

    private fun copyTextToClipboard(text: String) {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("Copied text", text))
    }

    private fun shareText(text: String) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(
                    Intent.EXTRA_TEXT,
                    text
                )
                .setType("text/plain"),
            null
        )

        runCatching {
            requireContext().startActivity(intent)
        }.onFailure {
            requireContext().toast(R.string.app_not_found)
        }
    }

    private fun downloadImage(url: String) {
        val downloadManager: DownloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val imageUri: Uri = url.toUri()

        val filename: String = URLUtil.guessFileName(url, null, null)
            .takeIf { name: String? ->
                !name.isNullOrEmpty()
            } ?: (getString(R.string.app_name) + "_image_${System.currentTimeMillis()}.jpg")

        val request: DownloadManager.Request = DownloadManager.Request(imageUri)
            .setTitle(getString(R.string.app_name_icon) + " " + getString(R.string.uploading_an_image))
            .setDescription(getString(R.string.being_downloaded_please_wait))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                /* dirType = */ Environment.DIRECTORY_PICTURES,
                /* subPath = */ getString(R.string.app_name_path) + "/$filename"
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadManager.enqueue(request)
    }
}
