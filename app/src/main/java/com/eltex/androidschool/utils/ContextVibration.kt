@file:Suppress("DEPRECATION")

package com.eltex.androidschool.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.os.VibrationEffect

/**
 * Вызывает простую вибрацию на определенное количество миллисекунд.
 *
 * @param milliseconds Длительность вибрации в миллисекундах (по умолчанию 50 мс).
 */
@SuppressLint("MissingPermission")
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

fun Context.vibrateWithPattern(pattern: LongArray, repeat: Int = -1) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        vibrator.vibrate(pattern, repeat)
    }
}

fun Context.vibrateWithEffect(duration: Long) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }
}

fun Context.vibrateWithEffectPattern(pattern: LongArray, repeat: Int = -1) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        val effect = VibrationEffect.createWaveform(pattern, repeat)
        vibrator.vibrate(effect)
    }
}
