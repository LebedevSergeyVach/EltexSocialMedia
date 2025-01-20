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
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentSettingsBinding
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.utils.showMaterialDialog
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck

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
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        updateButtonTexts(binding = binding)

        binding.chooseButtonSettingsLanguage.setOnClickListener { view: View ->
            showLanguagePopupMenu(view, binding = binding)
        }

        binding.cardSettingsLanguage.setOnClickListener { view: View ->
            showLanguagePopupMenu(view, binding = binding)
        }

        binding.chooseButtonSettingsTheme.setOnClickListener { view: View ->
            showThemePopupMenu(view, binding = binding)
        }

        binding.cardSettingsTheme.setOnClickListener { view: View ->
            showThemePopupMenu(view, binding = binding)
        }

        binding.cardSettingsLanguage.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.language_in_application_title),
                message = getString(R.string.language_in_application_description)
            )

            true
        }

        binding.chooseButtonSettingsLanguage.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.language_in_application_title),
                message = getString(R.string.language_in_application_description)
            )

            true
        }

        binding.chooseButtonSettingsTheme.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.theme_in_application_title),
                message = getString(R.string.theme_in_application_description)
            )

            true
        }

        binding.cardSettingsTheme.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.theme_in_application_title),
                message = getString(R.string.theme_in_application_description)
            )

            true
        }

        binding.cardSettingsVibration.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.vibration_in_application_title),
                message = getString(R.string.vibration_in_application_description)
            )

            true
        }

        binding.vibrationOptionSwitch.setOnLongClickListener {
            displayingDialogWindowWithInformation(
                title = getString(R.string.vibration_in_application_title),
                message = getString(R.string.vibration_in_application_description)
            )

            true
        }

        binding.textVersionApplication.text = getAppVersionName(requireContext())

        initVibrationSwitch(binding = binding)

        return binding.root
    }

    /**
     * Обновляет текст кнопок в зависимости от текущих настроек языка и темы.
     *
     * @param binding Binding для макета фрагмента настроек.
     *
     * @see SharedPreferences Хранилище для сохранения настроек приложения.
     */
    private fun updateButtonTexts(binding: FragmentSettingsBinding) {
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
     * @param binding Binding для макета фрагмента настроек.
     *
     * @see PopupMenu Меню для выбора языка.
     * @see setLocale Метод для установки выбранного языка.
     */
    private fun showLanguagePopupMenu(view: View, binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)

        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_ru -> setLocale("ru", binding = binding)
                R.id.language_en -> setLocale("en", binding = binding)
                R.id.language_system -> setLocale(null, binding = binding)
            }
            true
        }

        popupMenu.show()
    }

    /**
     * Показывает PopupMenu для выбора темы.
     *
     * @param view View, к которому привязан PopupMenu.
     * @param binding Binding для макета фрагмента настроек.
     *
     * @see PopupMenu Меню для выбора темы.
     * @see setTheme Метод для установки выбранной темы.
     */
    private fun showThemePopupMenu(view: View, binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)

        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.menu_theme, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_light -> setTheme(
                    AppCompatDelegate.MODE_NIGHT_NO, binding = binding
                )

                R.id.theme_dark -> setTheme(
                    AppCompatDelegate.MODE_NIGHT_YES, binding = binding
                )

                R.id.theme_system -> setTheme(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                    binding = binding
                )
            }
            true
        }

        popupMenu.show()
    }

    /**
     * Устанавливает язык приложения.
     *
     * @param languageCode Код языка (например, "ru" или "en"). Если null, используется системный язык.
     * @param binding Binding для макета фрагмента настроек.
     *
     * @see Locale Класс для работы с локализацией.
     * @see Configuration Класс для управления конфигурацией ресурсов.
     */
    @Suppress("DEPRECATION")
    private fun setLocale(languageCode: String?, binding: FragmentSettingsBinding) {
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

        if (BuildConfig.DEBUG) {
            Logger.d("System locale: ${Locale.getDefault()}")
            Logger.d("Updated configuration locale: ${resources.configuration.locale}")
        }

        updateButtonTexts(binding = binding)

        requireActivity().recreate()
    }

    /**
     * Устанавливает тему приложения.
     *
     * @param themeMode Режим темы (AppCompatDelegate.MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM).
     * @param binding Binding для макета фрагмента настроек.``
     *
     * @see AppCompatDelegate Класс для управления темой приложения.
     */
    private fun setTheme(themeMode: Int, binding: FragmentSettingsBinding) {
        AppCompatDelegate.setDefaultNightMode(themeMode)

        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        sharedPreferences.edit().putInt("Theme", themeMode).apply()

        updateButtonTexts(binding = binding)

        requireActivity().recreate()
    }

    /**
     * Возвращает имя версии приложения.
     *
     * @param context Контекст приложения.
     * @return Имя версии приложения (например, "1.0.0").
     *
     * @throws Exception Может быть выброшено, если не удалось получить информацию о версии.
     */
    private fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            getString(R.string.app_name) + " " + packageInfo.versionName.toString()
        } catch (e: Exception) {
            getString(R.string.unknown_error)
        }
    }

    /**
     * Инициализирует переключатель вибрации.
     *
     * @param binding Binding для макета фрагмента настроек.
     *
     * @see SharedPreferences Хранилище для сохранения настроек вибрации.
     */
    private fun initVibrationSwitch(binding: FragmentSettingsBinding) {
        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isVibrationEnabled = sharedPreferences.getBoolean("VibrationEnabled", true)

        binding.vibrationOptionSwitch.isChecked = isVibrationEnabled
        updateVibrationText(isVibrationEnabled, binding = binding)

        binding.vibrationOptionSwitch.setOnCheckedChangeListener { _, isChecked ->
            requireContext().singleVibrationWithSystemCheck(35)

            sharedPreferences.edit().putBoolean("VibrationEnabled", isChecked).apply()
            updateVibrationText(isChecked, binding = binding)
        }

        binding.cardSettingsVibration.setOnClickListener {
            binding.vibrationOptionSwitch.isChecked = !binding.vibrationOptionSwitch.isChecked
        }
    }

    /**
     * Обновляет текст, отображающий состояние вибрации.
     *
     * @param isEnabled Состояние вибрации (включена/выключена).
     * @param binding Binding для макета фрагмента настроек.
     */
    private fun updateVibrationText(isEnabled: Boolean, binding: FragmentSettingsBinding) {
        val text = if (isEnabled) {
            getString(R.string.vibration_on)
        } else {
            getString(R.string.vibration_off)
        }

        binding.textSettingsVibration.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                binding.textSettingsVibration.text = text
                binding.textSettingsVibration.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    /**
     * Показывает всплывающее диалоговое информационное окно.
     *
     * @param title - Заголовок диалога.
     * @param message - Основной текст диалога.
     * @param buttonText - Текст кнопки (по умолчанию "Спасибо").
     */
    private fun displayingDialogWindowWithInformation(
        title: String,
        message: String,
        buttonText: String = getString(R.string.thanks)
    ) {
        requireContext().showMaterialDialog(
            title = title,
            message = message,
            buttonText = buttonText
        )
    }
}
