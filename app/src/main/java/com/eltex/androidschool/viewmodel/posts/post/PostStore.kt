package com.eltex.androidschool.viewmodel.posts.post

import com.eltex.androidschool.mvi.Store
import com.eltex.androidschool.effects.posts.PostEffect

/**
 * Псевдоним типа для хранилища (Store), специализированного для работы с постами.
 * Этот тип упрощает использование хранилища, связанного с состоянием постов, сообщениями и эффектами.
 *
 * @see Store Базовый класс хранилища, который управляет состоянием и эффектами.
 * @see PostState Состояние, связанное с постами.
 * @see PostMessage Сообщения, которые могут изменять состояние постов.
 * @see PostEffect Эффекты, которые выполняются в ответ на сообщения.
 */
typealias PostStore = Store<PostState, PostMessage, PostEffect>
