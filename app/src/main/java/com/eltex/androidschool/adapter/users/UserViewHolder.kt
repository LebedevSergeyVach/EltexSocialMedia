package com.eltex.androidschool.adapter.users

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.databinding.CardUserBinding

/**
 * ViewHolder для отображения данных пользователя в RecyclerView.
 *
 * Этот класс отвечает за привязку данных пользователя (имя, логин и инициал) к элементам пользовательского интерфейса
 * в карточке пользователя. Он используется в сочетании с `UserAdapter` для отображения списка пользователей.
 *
 * @param binding Binding для макета карточки пользователя (CardUserBinding).
 * @param context Контекст приложения, необходимый для создания ViewHolder.
 *
 * @see UserAdapter Адаптер, который использует этот ViewHolder для отображения списка пользователей.
 * @see UserData Модель данных пользователя, которая передается в метод `bindUser`.
 */
class UserViewHolder(
    private val binding: CardUserBinding,
    private val context: Context
) : ViewHolder(binding.root) {

    /**
     * Привязывает данные пользователя к элементам пользовательского интерфейса.
     *
     * @param user Объект `UserData`, содержащий данные пользователя (имя, логин и инициал).
     *
     * @see UserData Модель данных пользователя.
     */
    fun bindUser(user: UserData) {
        binding.name.text = user.name
        binding.login.text = user.login
        binding.initial.text = user.name.take(1)
    }
}
