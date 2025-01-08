package com.eltex.androidschool.fragments.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentSettingsBinding
import java.util.Locale

/**
 * Фрагмент для настройки приложения.
 *
 * Этот фрагмент позволяет пользователю изменять язык и тему приложения.
 * Он также предоставляет возможность выбора системных настроек для языка и темы.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 */
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    /**
     * Создает и возвращает представление для этого фрагмента.
     *
     * @param inflater Объект, который может преобразовать XML-файл макета в View-объекты.
     * @param container Родительский ViewGroup, в который будет добавлено представление.
     * @param savedInstanceState Сохраненное состояние фрагмента, если оно есть.
     * @return View, представляющий собой корневой элемент макета этого фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Вызывается после создания представления фрагмента.
     *
     * @param view Представление, возвращенное onCreateView.
     * @param savedInstanceState Сохраненное состояние фрагмента, если оно есть.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateButtonTexts()

        binding.chooseButtonSettingsLanguage.setOnClickListener {
            showLanguagePopupMenu(it)
        }

        binding.chooseButtonSettingsTheme.setOnClickListener {
            showThemePopupMenu(it)
        }

        binding.textVersionApplication.text = getAppVersionName(requireContext())
    }

    /**
     * Обновляет текст кнопок в зависимости от текущих настроек языка и темы.
     */
    private fun updateButtonTexts() {
        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("Language", null)

        binding.chooseButtonSettingsLanguage.text = when (languageCode) {
            "ru" -> getString(R.string.russian)
            "en" -> getString(R.string.english)
            else -> getString(R.string.system_language)
        }

        val themeMode =
            sharedPreferences.getInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        binding.chooseButtonSettingsTheme.text = when (themeMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.light_theme)
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.dark_theme)
            else -> getString(R.string.system_theme)
        }
    }

    /**
     * Показывает PopupMenu для выбора языка.
     *
     * @param view View, к которому привязан PopupMenu.
     */
    private fun showLanguagePopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_ru -> setLocale("ru")
                R.id.language_en -> setLocale("en")
                R.id.language_system -> setLocale(null)
            }
            true
        }

        popupMenu.show()
    }

    /**
     * Показывает PopupMenu для выбора темы.
     *
     * @param view View, к которому привязан PopupMenu.
     */
    private fun showThemePopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.theme_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_light -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.theme_dark -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.theme_system -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            true
        }

        popupMenu.show()
    }

    /**
     * Устанавливает язык приложения.
     *
     * @param languageCode Код языка (например, "ru" или "en"). Если null, используется системный язык.
     */
    @Suppress("DEPRECATION")
    private fun setLocale(languageCode: String?) {
        val locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            Locale.getDefault()
        }

        Locale.setDefault(locale)

        val resources = requireContext().resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        sharedPreferences.edit().putString("Language", languageCode).apply()

        updateButtonTexts()

        requireActivity().recreate()
    }

    /**
     * Устанавливает тему приложения.
     *
     * @param themeMode Режим темы (AppCompatDelegate.MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM).
     */
    private fun setTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)

        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        sharedPreferences.edit().putInt("Theme", themeMode).apply()

        updateButtonTexts()

        requireActivity().recreate()
    }

    /**
     * Возвращает имя версии приложения.
     *
     * @param context Контекст приложения.
     * @return Имя версии приложения (например, "1.0.0").
     */
    private fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            getString(R.string.app_name_new_year) + " " + packageInfo.versionName.toString()
        } catch (e: Exception) {
            getString(R.string.unknown_error)
        }
    }
}
