package com.eltex.androidschool.viewmodel.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel для обмена данными между фрагментами.
 *
 * Этот ViewModel используется для хранения и передачи данных между фрагментами, которые не имеют прямого
 * взаимодействия друг с другом. В частности, он используется для отслеживания текущей вкладки в `ViewPager2`
 * и передачи этой информации в `BottomNavigationFragment` для управления поведением кнопки создания нового поста или события.
 *
 * @property currentTab LiveData, содержащая текущую выбранную вкладку в `ViewPager2`.
 *                     Значение 0 соответствует вкладке с постами, а значение 1 — вкладке с событиями.
 *                     По умолчанию значение не установлено.
 *
 * @see ViewModel Базовый класс для ViewModel, который сохраняет данные при изменении конфигурации.
 * @see MutableLiveData Класс для хранения данных, которые могут изменяться и наблюдаться.
 *
 * @throws IllegalArgumentException Может быть выброшено, если передано некорректное значение для `currentTab`.
 */
class SharedViewModel : ViewModel() {
    val currentTab = MutableLiveData<Int>()

    private val _fabVisibility = MutableLiveData<FabState>(FabState.Visible)
    val fabVisibility: LiveData<FabState> = _fabVisibility

    private val _currentTabAnimate = MutableLiveData<Int>(0)
    val currentTabAnimate: LiveData<Int> = _currentTabAnimate

    fun setFabVisibility(visible: Boolean, tabPosition: Int) {
        if (_currentTabAnimate.value == tabPosition) {
            _fabVisibility.value = if (visible) FabState.Visible else FabState.Hidden
        }
    }

    fun setCurrentTab(position: Int) {
        _currentTabAnimate.value = position
    }

    sealed class FabState {
        object Visible : FabState()
        object Hidden : FabState()
    }
}
