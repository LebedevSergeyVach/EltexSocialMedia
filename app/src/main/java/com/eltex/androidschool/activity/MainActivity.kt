package com.eltex.androidschool.activity

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.eltex.androidschool.R
import com.eltex.androidschool.ui.common.EdgeToEdgeHelper
import com.eltex.androidschool.utils.common.LocaleContextWrapper

import dagger.hilt.android.AndroidEntryPoint

import java.util.Locale

/**
 * Главная активность приложения.
 *
 * Эта активность является точкой входа в приложение и управляет основным интерфейсом.
 * Она также восстанавливает настройки языка и темы приложения при запуске.
 *
 * @see AppCompatActivity Базовый класс для активностей, использующих функции библиотеки поддержки.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

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
     * Устанавливает локаль (язык) для приложения.
     *
     * Эта функция изменяет локаль приложения на основе переданного кода языка. Если код языка не указан,
     * используется локаль по умолчанию для устройства. После установки локали обновляется конфигурация ресурсов приложения.
     *
     * @param languageCode Код языка (например, "ru" для русского или "en" для английского). Если null, используется локаль по умолчанию.
     *
     * @see Locale Класс, представляющий локаль (язык и регион).
     * @see Configuration Класс для управления конфигурацией ресурсов приложения.
     * @see Resources.updateConfiguration Метод для обновления конфигурации ресурсов.
     *
     * @property languageCode Код языка, который будет использоваться для установки локали.
     */
    @Suppress("DEPRECATION")
    private fun setLocale(languageCode: String?) {
        val locale: Locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            Resources.getSystem().configuration.locales[0]
        }

        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    /**
     * Привязывает новый контекст к активности с учетом выбранной локали.
     *
     * Эта функция переопределяет метод `attachBaseContext` для установки локали на основе сохраненных настроек.
     * Локаль извлекается из SharedPreferences и применяется к контексту активности.
     *
     * @param newBase Новый контекст, который будет привязан к активности.
     *
     * @see Context Базовый класс для контекста приложения.
     * @see SharedPreferences Хранилище для сохранения настроек приложения.
     * @see LocaleContextWrapper Класс-обертка для применения локали к контексту.
     */
//    override fun attachBaseContext(newBase: Context) {
//        val sharedPreferences = newBase.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
//        val languageCode = sharedPreferences.getString("Language", null)
//
//        val locale: Locale = if (languageCode != null) {
//            Locale(languageCode)
//        } else {
//            resources.configuration.locales[0]
//        }
//
//        super.attachBaseContext(LocaleContextWrapper.wrap(newBase, locale))
//    }
}
