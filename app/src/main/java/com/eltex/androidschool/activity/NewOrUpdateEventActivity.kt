package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.ActivityNewOrUpdateEventBinding

import com.eltex.androidschool.data.EventDataParcelable
import com.eltex.androidschool.ui.EdgeToEdgeHelper

import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.utils.vibrateWithEffect

@Suppress("DEPRECATION")
class NewOrUpdateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))

        val binding = ActivityNewOrUpdateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventDataParcelable =
            intent.getParcelableExtra<EventDataParcelable>("EventDataParcelable")

        if (eventDataParcelable != null) {
            binding.content.setText(eventDataParcelable.content)
            binding.data.setText(eventDataParcelable.date)
            binding.option.setText(eventDataParcelable.option)
            binding.link.setText(eventDataParcelable.link)
            binding.toolbar.title = getString(R.string.update_event_title)
        } else {
            binding.toolbar.title = getString(R.string.new_event_title)
        }

        binding.toolbar.menu.findItem(R.id.save_post).setOnMenuItemClickListener {
            val newContent = binding.content.text?.toString().orEmpty()
            val newDate = binding.data.text?.toString().orEmpty()
            val newOption = binding.option.text?.toString().orEmpty()
            val newLink = binding.link.text?.toString().orEmpty()

            if (
                newContent.isNotEmpty() && newDate.isNotEmpty() && newOption.isNotEmpty() && newLink.isNotEmpty()
            ) {
                val resultIntent = Intent().apply {
                    putExtra(
                        "EventDataParcelable",
                        EventDataParcelable(
                            newContent,
                            newDate,
                            newOption,
                            newLink,
                            eventDataParcelable?.eventId ?: -1L
                        )
                    )
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                applicationContext.vibrateWithEffect(100L)
                toast(R.string.error_text_event_is_empty)
            }

            true
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
