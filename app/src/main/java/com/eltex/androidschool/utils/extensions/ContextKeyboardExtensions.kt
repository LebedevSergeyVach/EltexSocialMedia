package com.eltex.androidschool.utils.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Метод расширения для Context, проверяющий, активна ли программная клавиатура (метод ввода).
 * ПРИМЕЧАНИЕ: Этот метод проверяет, активен ли в данный момент метод ввода для сфокусированного View.
 * Он НЕ гарантирует, что визуальный интерфейс клавиатуры отображается на экране,
 * но часто коррелирует с ним в типичных сценариях, когда EditText находится в фокусе.
 * Более надежная проверка визуального отображения клавиатуры часто включает наблюдение
 * за Window Insets или изменениями в макете View, что обычно требует доступа к View или Window.
 *
 * @receiver Context Контекст, используемый для доступа к системному сервису.
 * @return true, если метод ввода активен, false в противном случае.
 */
fun Context.isKeyboardVisible(): Boolean {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    // isActive() возвращает true, если активен метод ввода для текущего поля ввода
    // Это наиболее близкий к "видимости" метод, доступный напрямую через Context,
    // но не является 100% гарантией визуального отображения клавиатуры.
    return imm?.isActive == true
}
