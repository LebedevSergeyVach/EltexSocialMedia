package com.eltex.androidschool.adapter.users

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.databinding.CardUserBinding

/**
 * Адаптер для отображения списка пользователей в RecyclerView.
 *
 * Этот адаптер управляет списком пользователей и их отображением в RecyclerView. Он также обрабатывает
 * клики на карточки пользователей, вызывая метод `onGetUserClicked` из интерфейса `UserListener`.
 *
 * @param listener Слушатель событий, который будет вызываться при клике на карточку пользователя.
 *
 * @see UserViewHolder ViewHolder, используемый для отображения карточек пользователей.
 * @see UserItemCallback Callback для сравнения элементов списка пользователей.
 * @see UserData Модель данных пользователя.
 */
class UserAdapter(
    private val listener: UserListener
) : ListAdapter<UserData, UserViewHolder>(UserItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с пользователями.
     */
    interface UserListener {

        /**
         * Вызывается при клике на карточку пользователя.
         *
         * @param user Объект `UserData`, представляющий пользователя, на которого был выполнен клик.
         */
        fun onGetUserClicked(user: UserData)
    }

    /**
     * Создает новый ViewHolder для отображения карточки пользователя.
     *
     * @param parent Родительский ViewGroup, в который будет добавлена карточка пользователя.
     * @param viewType Тип View.
     *
     * @return Новый объект `UserViewHolder`.
     *
     * @see UserViewHolder ViewHolder для отображения карточки пользователя.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardUserBinding.inflate(layoutInflater, parent, false)

        val viewHolder = UserViewHolder(
            binding = binding,
            context = parent.context
        )

        binding.cardUser.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.adapterPosition))
        }

        return viewHolder
    }

    /**
     * Привязывает данные пользователя к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(
            user = getItem(position)
        )
    }
}
