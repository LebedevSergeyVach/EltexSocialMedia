package com.eltex.androidschool.viewmodel.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel для управления состоянием панели инструментов.
 *
 * Этот ViewModel отвечает за управление видимостью кнопки сохранения и обработку событий, связанных с ней.
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
}
