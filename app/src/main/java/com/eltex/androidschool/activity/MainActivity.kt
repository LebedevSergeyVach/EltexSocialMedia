package com.eltex.androidschool.activity

import android.content.Context
import android.content.res.Configuration

import android.os.Bundle

import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.eltex.androidschool.R
import com.eltex.androidschool.ui.common.EdgeToEdgeHelper

import java.util.Locale

/**
 * Главная активность приложения.
 *
 * Эта активность является точкой входа в приложение и управляет основным интерфейсом.
 * Она также восстанавливает настройки языка и темы приложения при запуске.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 */
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    /**
     * Вызывается при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности, если оно есть.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("Language", null)
        setLocale(languageCode)

        val themeMode =
            sharedPreferences.getInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        enableEdgeToEdge()
        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))
    }

    /**
     * Устанавливает язык приложения.
     *
     * @param languageCode Код языка (например, "ru" или "en"). Если null, используется системный язык.
     */
    private fun setLocale(languageCode: String?) {
        val locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            Locale.getDefault()
        }
        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
