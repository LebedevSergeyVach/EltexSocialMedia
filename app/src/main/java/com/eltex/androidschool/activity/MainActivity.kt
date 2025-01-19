package com.eltex.androidschool.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration

import android.os.Bundle

import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.eltex.androidschool.R
import com.eltex.androidschool.fragments.common.ToolbarFragment
import com.eltex.androidschool.ui.common.EdgeToEdgeHelper
import com.eltex.androidschool.utils.LocaleContextWrapper

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

    companion object {
        const val FRAGMENT = "FRAGMENT"
        const val FRAGMENT_TO_OPEN = "FRAGMENT_TO_OPEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("Language", null)
        setLocale(languageCode)

        val themeMode =
            sharedPreferences.getInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        handleShortcut(intent)

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
    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("Language", null)

        val locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            Locale.getDefault()
        }

        super.attachBaseContext(LocaleContextWrapper.wrap(newBase, locale))
    }

    /**
     * Обрабатывает быстрые действия (shortcuts) для открытия определенного фрагмента.
     *
     * Эта функция извлекает имя фрагмента из Intent и создает экземпляр `ToolbarFragment` с переданными аргументами.
     * Затем фрагмент заменяется в контейнере активности.
     *
     * @param intent Intent, содержащий данные о быстром действии. Может быть null.
     *
     * @see Intent Класс для передачи данных между компонентами приложения.
     * @see ToolbarFragment Фрагмент, который будет открыт.
     * @see FragmentTransaction Класс для управления транзакциями фрагментов.
     */
    private fun handleShortcut(intent: Intent?) {
        val fragmentName = intent?.getStringExtra(FRAGMENT)
        val fragment = ToolbarFragment().apply {
            arguments = Bundle().apply {
                putString(FRAGMENT_TO_OPEN, fragmentName)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
