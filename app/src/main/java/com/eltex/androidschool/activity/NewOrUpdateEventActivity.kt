package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.ActivityNewOrUpdateEventBinding
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast

class NewOrUpdateEventActivity : AppCompatActivity() {
    private var eventId: Long = -1L
    private lateinit var titleToolbar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))

        val binding = ActivityNewOrUpdateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleToolbar = getString(R.string.new_event_title)

        val content = intent.getStringExtra(Intent.EXTRA_TEXT)
        val date = intent.getStringExtra("date")
        val option = intent.getStringExtra("option")
        val link = intent.getStringExtra("link")
        eventId = intent.getLongExtra("eventId", -1L)

        if (content != null) {
            titleToolbar = getString(R.string.update_event_title)
            binding.content.setText(content)
            binding.data.setText(date)
            binding.option.setText(option)
            binding.link.setText(link)
        }

        binding.toolbar.title = titleToolbar

        binding.toolbar.menu.findItem(R.id.save_post).setOnMenuItemClickListener {
            val newContent = binding.content.text?.toString().orEmpty()
            val newDate = binding.data.text?.toString().orEmpty()
            val newOption = binding.option.text?.toString().orEmpty()
            val newLink = binding.link.text?.toString().orEmpty()

            if (
                newContent.isNotEmpty() && newDate.isNotEmpty() && newOption.isNotEmpty() && newLink.isNotEmpty()
            ) {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, newContent)
                    putExtra("date", newDate)
                    putExtra("option", newOption)
                    putExtra("link", newLink)
                    putExtra("eventId", eventId)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                toast(R.string.error_text_event_is_empty)
            }

            true
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
