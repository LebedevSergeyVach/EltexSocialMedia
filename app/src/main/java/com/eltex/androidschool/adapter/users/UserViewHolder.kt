package com.eltex.androidschool.adapter.users

import android.content.Context

import android.graphics.drawable.Drawable

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.R
import com.eltex.androidschool.data.users.UserData
import com.eltex.androidschool.databinding.CardUserBinding
import com.eltex.androidschool.utils.common.initialsOfUsername

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

        showPlaceholder(user = user)
        binding.skeletonLayout.showSkeleton()

        if (!user.avatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(user.avatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showPlaceholder(user = user)

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.skeletonLayout.showOriginal()
                        binding.initial.isVisible = false

                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .thumbnail(
                    Glide.with(binding.root)
                        .load(user.avatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            showPlaceholder(user = user)
        }
    }

    /**
     * Отображает плейсхолдер для пользователя, устанавливая изображение аватара,
     * инициализируя текст с инициалами пользователя, устанавливая цвет текста,
     * отображая оригинальное состояние макета и делая инициал видимым.
     *
     * @param user Объект [UserData], содержащий информацию о пользователе.
     * @see initialsOfUsername
     */
    private fun showPlaceholder(user: UserData) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = initialsOfUsername(name = user.name)
        binding.initial.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        binding.skeletonLayout.showOriginal()
        binding.initial.isVisible = true
    }
}
