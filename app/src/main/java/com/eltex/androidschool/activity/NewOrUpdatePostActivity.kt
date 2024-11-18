package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.ActivityNewOrUpdatePostBinding

import com.eltex.androidschool.data.PostDataParcelable
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast

@Suppress("DEPRECATION")
class NewOrUpdatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))

        val binding = ActivityNewOrUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postDataParcelable =
            intent.getParcelableExtra<PostDataParcelable>("PostDataParcelable")

        if (postDataParcelable != null) {
            binding.content.setText(postDataParcelable.content)
            binding.toolbar.title = getString(R.string.update_post_title)
        } else {
            binding.toolbar.title = getString(R.string.new_post_title)
        }

        binding.toolbar.menu.findItem(R.id.save_post).setOnMenuItemClickListener {
            val newContent = binding.content.text?.toString().orEmpty()

            if (newContent.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra(
                        "PostDataParcelable",
                        PostDataParcelable(
                            newContent,
                            postDataParcelable?.postId ?: -1L
                        )
                    )
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                toast(R.string.error_text_post_is_empty)
            }

            true
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
