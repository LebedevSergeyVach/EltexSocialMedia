package com.eltex.androidschool.adapter.users

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.data.users.UserData

/**
 * Callback для сравнения элементов списка пользователей в RecyclerView.
 *
 * Этот класс используется для определения, являются ли два элемента списка пользователей одинаковыми
 * или их содержимое изменилось. Он необходим для оптимизации работы RecyclerView и предотвращения
 * лишних перерисовок.
 *
 * @see DiffUtil.ItemCallback Базовый класс для реализации сравнения элементов.
 * @see UserData Модель данных пользователя, которая сравнивается в методах.
 */
class UserItemCallback : DiffUtil.ItemCallback<UserData>() {

    /**
     * Определяет, являются ли два объекта `UserData` одинаковыми по идентификатору.
     *
     * @param oldItem Старый объект `UserData`.
     * @param newItem Новый объект `UserData`.
     *
     * @return `true`, если идентификаторы объектов совпадают, иначе `false`.
     */
    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean =
        oldItem.id == newItem.id

    /**
     * Определяет, изменилось ли содержимое объекта `UserData`.
     *
     * @param oldItem Старый объект `UserData`.
     * @param newItem Новый объект `UserData`.
     *
     * @return `true`, если содержимое объектов совпадает, иначе `false`.
     */
    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean =
        oldItem == newItem
}
