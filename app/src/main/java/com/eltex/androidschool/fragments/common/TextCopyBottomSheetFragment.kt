package com.eltex.androidschool.fragments.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomSheetCopyTextBinding
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.extensions.toast

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TextCopyBottomSheetFragment(
    private val textCopy: String,
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomSheetCopyTextBinding.inflate(inflater, container, false)

        binding.textCopy.text = textCopy

        binding.buttonCloseFragmentSheet.setOnClickListener {
            dismiss()
        }

        binding.cardCopyText.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            copyTextToClipboard(text = textCopy)
            dismiss()
        }

        binding.cardShareText.setOnClickListener {
            requireContext().singleVibrationWithSystemCheck(35L)
            shareText(text = textCopy)
            dismiss()
        }

        return binding.root
    }

    private fun copyTextToClipboard(text: String) {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("Copied text", text))

        requireContext().toast(R.string.text_is_copied)
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
}
