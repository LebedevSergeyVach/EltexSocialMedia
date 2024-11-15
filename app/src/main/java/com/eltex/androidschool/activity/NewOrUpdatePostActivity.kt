package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.ActivityNewOrUpdatePostBinding
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast

/**
 * Активность для создания нового поста или редактирования существующего.
 *
 * Эта активность позволяет пользователю вводить текст для нового поста или редактировать текст существующего поста.
 * После ввода текста и нажатия кнопки "Сохранить", активность возвращает результат обратно в вызывающую активность.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 */
class NewOrUpdatePostActivity : AppCompatActivity() {
    private var postId: Long = -1L
    private lateinit var titleToolbar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))

        val binding = ActivityNewOrUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleToolbar = getString(R.string.new_post_title)

        val content = intent.getStringExtra(Intent.EXTRA_TEXT)
        postId = intent.getLongExtra("postId", -1L)

        if (content != null) {
            titleToolbar = getString(R.string.update_post_title)
            binding.content.setText(content)
        }

        binding.toolbar.title = titleToolbar

        binding.toolbar.menu.findItem(R.id.save_post).setOnMenuItemClickListener {
            val newContent = binding.content.text?.toString().orEmpty()

            if (newContent.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, newContent)
                    putExtra("postId", postId)
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
