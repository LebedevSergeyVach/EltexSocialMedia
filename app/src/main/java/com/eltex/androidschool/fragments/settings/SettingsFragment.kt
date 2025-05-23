package com.eltex.androidschool.fragments.settings

import android.annotation.SuppressLint

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentSettingsBinding
import com.eltex.androidschool.utils.helper.LoggerHelper
import com.eltex.androidschool.utils.extensions.showMaterialDialog
import com.eltex.androidschool.utils.extensions.showMaterialDialogWithTwoButtons
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck
import com.eltex.androidschool.viewmodel.common.ToolBarViewModel

import dagger.hilt.android.AndroidEntryPoint

import java.io.File
import java.util.Locale
import androidx.core.content.edit
import com.eltex.androidschool.utils.extensions.showTopSnackbar

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

    private val toolbarViewModel by activityViewModels<ToolBarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        updateButtonTexts(binding = binding)
        initVibrationSwitch(binding = binding)
        updateCacheSize(binding = binding, anime = false)

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

        binding.buttonOpenVersionApplication.text = getAppVersionName(requireContext())

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

        binding.buttonSettingsClearCache.setOnClickListener {
            showingShowDeleteConfirmationDialogWithClearingCache(binding = binding)
        }

        binding.buttonSettingsClearCache.setOnLongClickListener {
            displayingDisplayingDialogWindowWithInformationWithInformationAboutCache()

            true
        }

        binding.cardSettingsCache.setOnClickListener {
            showingShowDeleteConfirmationDialogWithClearingCache(binding = binding)
        }

        binding.cardSettingsCache.setOnLongClickListener {
            displayingDisplayingDialogWindowWithInformationWithInformationAboutCache()

            true
        }

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setRulesVisible(true)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setRulesVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> Unit
                    }
                }
            }
        )

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
            inflate(R.menu.menu_language)

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
        val locale: Locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            Resources.getSystem().configuration.locales[0]
        }

        Locale.setDefault(locale)

        val resources = requireContext().resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        val sharedPreferences =
            requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        sharedPreferences.edit { putString("Language", languageCode) }

        if (BuildConfig.DEBUG) {
            LoggerHelper.d("System locale: ${Locale.getDefault()}")
            LoggerHelper.d("Updated configuration locale: ${resources.configuration.locale}")
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

        sharedPreferences.edit { putInt("Theme", themeMode) }

        updateButtonTexts(binding = binding)

        requireActivity().recreate()
    }

    /**
     * Возвращает имя версии приложения.
     *
     * @param context Контекст приложения.
     * @return Имя версии приложения (например, "Academy Eltex v1.0.0").
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

        updateVibrationText(isEnabled = isVibrationEnabled, binding = binding, animate = false)

        binding.vibrationOptionSwitch.isChecked = isVibrationEnabled

        binding.vibrationOptionSwitch.setOnCheckedChangeListener { _, isChecked ->
            requireContext().singleVibrationWithSystemCheck(35)

            sharedPreferences.edit { putBoolean("VibrationEnabled", isChecked) }

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
            buttonText = buttonText,
        )
    }

    /**
     * Отображает диалоговое окно подтверждения удаления кэша приложения.
     *
     * Вызывает тактильную вибрацию на устройстве и показывает диалог с заголовком и сообщением,
     * информирующим пользователя о размере кэша приложения и его очистке.
     *
     * @param binding Объект привязки фрагмента, используемый для доступа к представлениям.
     *
     * @see showDeleteConfirmationDialog
     * @see clearCacheApplication
     *
     * @throws IllegalArgumentException Если binding является null.
     */
    private fun showingShowDeleteConfirmationDialogWithClearingCache(binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)

        showDeleteConfirmationDialog(
            title = getString(R.string.data_and_cache_app),
            message = buildString {
                append(getString(R.string.application_cache_size_app))
                append(": ")
                append(getCacheSize(requireContext()))
                append("\n")
                append(getString(R.string.data_and_cache_description))
            },
            textButtonCancel = getString(R.string.thanks),
            textButtonDelete = getString(R.string.clear_cache),
        ) {
            clearCacheApplication(binding)
        }
    }

    /**
     * Отображает диалоговое окно с информацией о размере кэша приложения.
     *
     * Вызывает диалог с заголовком и сообщением, информирующим пользователя о размере кэша
     * приложения и его очистке. Не требует подтверждения от пользователя.
     *
     * @see displayingDialogWindowWithInformation()
     *
     * @throws IllegalArgumentException Если binding является null.
     */
    private fun displayingDisplayingDialogWindowWithInformationWithInformationAboutCache() {
        displayingDialogWindowWithInformation(
            title = getString(R.string.data_and_cache_app),
            message = buildString {
                append(getString(R.string.application_cache_size_app))
                append(": ")
                append(getCacheSize(requireContext()))
                append("\n")
                append(getString(R.string.data_and_cache_description))
            },
        )
    }

    /**
     * Показывает диалоговое окно с подтверждением очистки кеща.
     *
     * Эта функция отображает Material 3 диалог с двумя кнопками: "Спасибо" и "Очистить кэш".
     * Кнопка "Отмена" закрывает диалог без выполнения каких-либо действий, а кнопка "Удалить"
     * вызывает переданный коллбэк `onDeleteConfirmed`, который выполняет удаление.
     *
     * @param title Заголовок диалога. Обычно это текст, который кратко описывает действие, например, "Удаление поста".
     * @param message Основной текст диалога. Это более подробное описание действия, например, "Вы уверены, что хотите удалить этот пост?".
     * @param onDeleteConfirmed Коллбэк, который вызывается при нажатии на кнопку "Удалить". Этот коллбэк должен содержать логику удаления.
     *
     * @see Context.showMaterialDialogWithTwoButtons Функция, которая используется для отображения диалога с двумя кнопками.
     */
    private fun showDeleteConfirmationDialog(
        title: String,
        message: String,
        textButtonCancel: String,
        textButtonDelete: String,
        onDeleteConfirmed: () -> Unit,
    ) {
        requireContext().showMaterialDialogWithTwoButtons(
            title = title,
            message = message,
            cancelButtonText = textButtonCancel,
            deleteButtonText = textButtonDelete,
            onDeleteConfirmed = onDeleteConfirmed,
        )
    }

    /**
     * Возвращает размер кэша приложения в удобочитаемом формате (например, "1.23 MB").
     *
     * @param context Контекст приложения.
     * @return Строка с размером кэша.
     */
    private fun getCacheSize(context: Context): String {
        val cacheDir: File = context.cacheDir
        val size: Long

        fun calculateSize(file: File): Long {
            if (file.isDirectory) {
                return file.listFiles()?.sumOf { fileCache: File ->
                    calculateSize(fileCache)
                } ?: 0
            }

            return file.length()
        }

        size = calculateSize(cacheDir)

        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "%.2f KB".format(size / 1024.0)
            else -> "%.2f MB".format(size / (1024.0 * 1024.0))
        }
    }

    /**
     * Очищает кэш приложения.
     *
     * @param context Контекст приложения.
     */
    private fun clearCache(context: Context) {
        val cacheDir: File = context.cacheDir

        fun deleteFiles(file: File) {
            if (file.isDirectory) {
                file.listFiles()?.forEach { fileCache: File ->
                    deleteFiles(fileCache)
                }
            }

            file.delete()
        }

        deleteFiles(cacheDir)
    }

    /**
     * Обновляет текст с размером кэша.
     *
     * @param binding Binding для макета фрагмента настроек.
     * @param animate Нужно ли показать анимацию смена текста (по умолчанию true)
     */
    private fun updateCacheSize(
        binding: FragmentSettingsBinding,
        anime: Boolean = true
    ) {
        val textCache = buildString {
            append(getString(R.string.application_cache_size))
            append(": ")
            append(getCacheSize(requireContext()))
        }

        if (anime) {
            binding.textSettingsCache.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.textSettingsCache.text = textCache
                    binding.textSettingsCache.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        } else {
            binding.textSettingsCache.text = textCache
        }
    }

    /**
     * Запуск очистки кеша приложеиния.
     *
     * @param binding Binding для макета фрагмента настроек.
     */
    private fun clearCacheApplication(binding: FragmentSettingsBinding) {
        requireContext().singleVibrationWithSystemCheck(35L)
        clearCache(requireContext())
        updateCacheSize(binding = binding, anime = true)
        requireContext().showTopSnackbar(
            message = getString(R.string.cleared_cache),
            iconRes = R.drawable.ic_check_24,
        )
    }
}
