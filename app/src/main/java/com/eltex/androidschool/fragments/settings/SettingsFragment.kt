package com.eltex.androidschool.fragments.settings

import android.annotation.SuppressLint

import android.content.Context
import android.content.res.Configuration

import android.graphics.drawable.InsetDrawable

import android.os.Build
import android.os.Bundle

import android.util.TypedValue

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.widget.PopupMenu

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder

import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentSettingsBinding
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.utils.showMaterialDialog
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import dagger.hilt.android.AndroidEntryPoint

import java.util.Locale

/**
 * Фрагмент для настройки приложения.
 *
 * Этот фрагмент позволяет пользователю изменять язык и тему приложения.
 * Он также предоставляет возможность выбора системных настроек для языка и темы.
 *
 * @see Fragment Базовый класс для фрагментов, использующих функции библиотеки поддержки.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        updateButtonTexts(binding = binding)

        initVibrationSwitch(binding = binding)

        binding.chooseButtonSettingsLanguage.setOnClickListener { view: View ->
            showLanguagePopupMenu(view, binding = binding)
        }

        binding.chooseButtonSettingsTheme.setOnClickListener { view: View ->
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

        binding.buttonOpenVersionApplication.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_listAppUpdatesFragment,
                null,
                NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()
            )
        }

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
    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showLanguagePopupMenu(view: View, binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)

        val resources = view.context.resources

        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        ).toInt()

        val popup = androidx.appcompat.widget.PopupMenu(view.context, view).apply {
            inflate(R.menu.language_menu)

            if (menu is MenuBuilder) {
                val menuBuilder = menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)

                for (item in menuBuilder.visibleItems) {
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                        }
                    }
                }
            }

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.language_ru -> setLocale("ru", binding = binding)
                    R.id.language_en -> setLocale("en", binding = binding)
                    R.id.language_system -> setLocale(null, binding = binding)
                }

                true
            }
        }

        popup.show()
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
    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showThemePopupMenu(view: View, binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)

        val resources = view.context.resources

        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        ).toInt()

        val popup = androidx.appcompat.widget.PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_theme)

            if (menu is MenuBuilder) {
                val menuBuilder = menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)

                for (item in menuBuilder.visibleItems) {
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                        }
                    }
                }
            }

            setOnMenuItemClickListener { menuItem: MenuItem ->
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
        }

        popup.show()
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
            val buildVariant: String = if (BuildConfig.DEBUG) {
                "Debug"
            } else {
                "Release"
            }

            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            getString(R.string.app_name) + " " + packageInfo.versionName.toString() + " " + buildVariant
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

        updateVibrationText(isEnabled = isVibrationEnabled, binding = binding, animate = false)

        binding.vibrationOptionSwitch.isChecked = isVibrationEnabled

        binding.vibrationOptionSwitch.setOnCheckedChangeListener { _, isChecked ->
            requireContext().singleVibrationWithSystemCheck(35)

            sharedPreferences.edit().putBoolean("VibrationEnabled", isChecked).apply()

            updateVibrationText(isEnabled = isChecked, binding = binding, animate = true)
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
     * @param animate Нужно ли показать анимацию смена текста (по умолчанию true)
     */
    private fun updateVibrationText(
        isEnabled: Boolean,
        binding: FragmentSettingsBinding,
        animate: Boolean = true
    ) {
        val text = if (isEnabled) {
            getString(R.string.vibration_on)
        } else {
            getString(R.string.vibration_off)
        }

        if (animate) {
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
        } else {
            binding.textSettingsVibration.text = text
        }
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
