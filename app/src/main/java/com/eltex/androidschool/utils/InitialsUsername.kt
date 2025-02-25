package com.eltex.androidschool.utils

/**
 * Генерирует инициалы пользователя на основе полного имени.
 *
 * Функция принимает полное имя в виде строки и возвращает инициалы.
 * Если полное имя состоит из двух или более слов, инициалы формируются
 * из первых букв каждого слова. Если полное имя состоит из одного слова,
 * возвращается первая буква этого слова.
 *
 * @param name Полное имя пользователя, состоящее из одного или нескольких слов.
 * @return Инициалы пользователя в виде строки. Если имя состоит из одного слова,
 * возвращается первая буква этого слова. Если имя пустое, возвращается пустая строка.
 */
fun initialsOfUsername(name: String): String {
    val fullName: String = name
    val initials: List<String> = fullName.split(" ")

    return if (initials.size >= 2) {
        buildString {
            append(initials[0].first().uppercaseChar())
            append(initials[1].first().uppercaseChar())
        }
    } else {
        name.take(1)
    }
}
