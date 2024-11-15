package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.ActivityNewPostBinding

import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))

        val binding = ActivityNewPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.menu.findItem(R.id.save_post).setOnMenuItemClickListener {
            val content = binding.content.text?.toString().orEmpty()

            if (content.isNotEmpty()) {
                setResult(RESULT_OK, Intent().putExtra(Intent.EXTRA_TEXT, content))
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
