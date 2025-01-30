@file:Suppress("DEPRECATION")

package com.eltex.androidschool.utils

import android.annotation.SuppressLint

import android.content.Context
import android.provider.Settings

import android.os.Vibrator
import android.os.VibrationEffect
import com.eltex.androidschool.BuildConfig

/**
 * Вызывает простую вибрацию на определенное количество миллисекунд.
 *
 * @param milliseconds Длительность вибрации в миллисекундах (по умолчанию 50 мс).
 */
fun Context.singleVibration(
    milliseconds: Long = 50
) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        vibrator.vibrate(milliseconds)
    }
}

/**
 * Вызывает вибрацию с определенной последовательностью.
 *
 * @param firstPauseMilliseconds Пауза перед первой вибрацией в миллисекундах (по умолчанию 0 мс).
 * @param firstVibrationMilliseconds Длительность первой вибрации в миллисекундах (по умолчанию 50 мс).
 * @param secondsPauseMilliseconds Пауза перед второй вибрацией в миллисекундах (по умолчанию 100 мс).
 * @param secondsVibrationMilliseconds Длительность второй вибрации в миллисекундах (по умолчанию 150 мс).
 * @param repetitionOfActions Количество повторений последовательности (по умолчанию -1, что означает бесконечное повторение).
 */
fun Context.doubleVibration(
    firstPauseMilliseconds: Long = 0,
    firstVibrationMilliseconds: Long = 50,
    secondsPauseMilliseconds: Long = 100,
    secondsVibrationMilliseconds: Long = 150,
    repetitionOfActions: Int = -1
) {
    val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val isVibrationEnabledInApp = sharedPreferences.getBoolean("VibrationEnabled", true)

    if (isVibrationEnabledInApp) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(
                firstPauseMilliseconds,
                firstVibrationMilliseconds,
                secondsPauseMilliseconds,
                secondsVibrationMilliseconds
            )
            vibrator.vibrate(
                pattern,
                repetitionOfActions
            )
        }
    }
}

/**
 * Вызывает простую вибрацию на определенное количество миллисекунд,
 * только если вибрация включена в системе и в настройках приложения.
 *
 * @param milliseconds Длительность вибрации в миллисекундах (по умолчанию 50 мс).
 */
@Suppress("DEPRECATION")
fun Context.singleVibrationWithSystemCheck(
    milliseconds: Long = 35L
) {
    val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val isVibrationEnabledInApp = sharedPreferences.getBoolean("VibrationEnabled", true)

    if (isVibrationEnabledInApp) {
        val isVibrationEnabledInSystem = Settings.System.getInt(
            this.contentResolver,
            Settings.System.HAPTIC_FEEDBACK_ENABLED,
            0
        ) == 1

        if (isVibrationEnabledInSystem) {
            if (BuildConfig.DEBUG) {
                Logger.i("Произошла вибрация singleVibrationWithSystemCheck($milliseconds)")
            }

            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(milliseconds)
            }
        }
    }
}

/**
 * Вызывает вибрацию с заданным шаблоном и количеством повторений.
 *
 * @param pattern Массив, содержащий чередующиеся значения пауз и длительностей вибрации.
 *               Например, [100, 200, 300, 400] означает:
 *               - пауза 100 мс,
 *               - вибрация 200 мс,
 *               - пауза 300 мс,
 *               - вибрация 400 мс.
 * @param repeat Количество повторений шаблона. По умолчанию -1, что означает бесконечное повторение.
 */
fun Context.vibrateWithPattern(pattern: LongArray, repeat: Int = -1) {
    val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val isVibrationEnabledInApp = sharedPreferences.getBoolean("VibrationEnabled", true)

    if (isVibrationEnabledInApp) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(pattern, repeat)
        }
    }
}

/**
 * Вызывает вибрацию с заданной длительностью и стандартной амплитудой.
 * Этот метод использует API VibrationEffect, доступный начиная с Android 8.0 (API 26).
 *
 * @param duration Длительность вибрации в миллисекундах.
 */
@SuppressLint("NewApi")
fun Context.vibrateWithEffect(duration: Long) {
    val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val isVibrationEnabledInApp = sharedPreferences.getBoolean("VibrationEnabled", true)

    if (isVibrationEnabledInApp) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }
}

/**
 * Вызывает вибрацию с заданным шаблоном и количеством повторений, используя API VibrationEffect.
 * Этот метод доступен начиная с Android 8.0 (API 26).
 *
 * @param pattern Массив, содержащий чередующиеся значения пауз и длительностей вибрации.
 *               Например, [100, 200, 300, 400] означает:
 *               - пауза 100 мс,
 *               - вибрация 200 мс,
 *               - пауза 300 мс,
 *               - вибрация 400 мс.
 * @param repeat Количество повторений шаблона. По умолчанию -1, что означает бесконечное повторение.
 */
@SuppressLint("NewApi")
fun Context.vibrateWithEffectPattern(pattern: LongArray, repeat: Int = -1) {
    val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val isVibrationEnabledInApp = sharedPreferences.getBoolean("VibrationEnabled", true)

    if (isVibrationEnabledInApp) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createWaveform(pattern, repeat)
            vibrator.vibrate(effect)
        }
    }
}
