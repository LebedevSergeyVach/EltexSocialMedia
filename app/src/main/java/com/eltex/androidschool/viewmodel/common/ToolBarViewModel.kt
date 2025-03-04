package com.eltex.androidschool.viewmodel.common

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel для управления состоянием панели инструментов.
 *
 * Этот ViewModel отвечает за управление видимостью кнопок сохранения, настроек и списка всех пользователей,
 * а также за обработку событий, связанных с этими кнопками.
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 */
class ToolBarViewModel : ViewModel() {

    /**
     * Состояние видимости кнопки сохранения.
     *
     * @property saveVisible Поток состояния видимости кнопки сохранения.
     */
    private val _saveVisible = MutableStateFlow(false)
    val saveVisible = _saveVisible.asStateFlow()

    /**
     * Состояние клика по кнопке сохранения.
     *
     * @property saveClicked Поток состояния клика по кнопке сохранения.
     */
    private val _saveClicked = MutableStateFlow(false)
    val saveClicked = _saveClicked.asStateFlow()

    /**
     * Состояние видимости кнопки настроек.
     *
     * @property settingsVisible Поток состояния видимости кнопки настроек.
     */
    private val _settingsVisible = MutableStateFlow(false)
    val settingsVisible = _settingsVisible.asStateFlow()

    /**
     * Состояние видимости кнопки списка всех пользователей.
     *
     * @property allUsersVisible Поток состояния видимости кнопки списка всех пользователей.
     */
    private val _allUsersVisible = MutableStateFlow(false)
    val allUsersVisible = _allUsersVisible.asStateFlow()

    /**
     * Состояние видимости кнопки Правила.
     *
     * @property logoutVisible Поток состояния видимости кнопки Правила.
     */
    private val _logoutVisible = MutableStateFlow(false)
    val logoutVisible = _logoutVisible.asStateFlow()

    /**
     * Состояние видимости кнопки выйти из аккаунта.
     *
     * @property logoutVisible Поток состояния видимости кнопки выхода из приложения.
     */
    private val _rulesVisible = MutableStateFlow(false)
    val rulesVisible = _rulesVisible.asStateFlow()

    /**
     * Устанавливает видимость кнопки сохранения.
     *
     * @param visible Видимость кнопки сохранения.
     */
    fun setSaveVisible(visible: Boolean) {
        _saveVisible.value = visible
    }

    /**
     * Обрабатывает клик по кнопке сохранения.
     *
     * @param pending Состояние клика по кнопке сохранения.
     */
    fun onSaveClicked(pending: Boolean) {
        _saveClicked.value = pending
    }

    /**
     * Устанавливает видимость кнопки настроек.
     *
     * @param visible Видимость кнопки настроек.
     */
    fun setSettingsVisible(visible: Boolean) {
        _settingsVisible.value = visible
    }

    /**
     * Устанавливает видимость кнопки списка всех пользователей.
     *
     * @param visible Видимость кнопки списка всех пользователей.
     */
    fun setAllUsersVisible(visible: Boolean) {
        _allUsersVisible.value = visible
    }

    /**
     * Устанавливает видимость кнопок настроек и списка всех пользователей.
     *
     * @param visible Видимость кнопок настроек и списка всех пользователей.
     */
    fun setSettingsAndAllUsersLogoutVisible(visible: Boolean) {
        _allUsersVisible.value = visible
        _settingsVisible.value = visible
        _logoutVisible.value = visible
    }

    /**
     * Устанавливает видимость кнопки Правила.
     *
     * @param visible Видимость кнопки Правила.
     */
    fun setRulesVisible(visible: Boolean) {
        _rulesVisible.value = visible
    }
}
