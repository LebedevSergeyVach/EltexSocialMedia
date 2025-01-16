package com.eltex.androidschool.viewmodel.posts.postswall

import com.eltex.androidschool.mvi.Store
import com.eltex.androidschool.effects.posts.PostWallEffect

/**
 * Псевдоним типа для хранилища (Store), специализированного для работы с постами на стене пользователя.
 * Этот тип упрощает использование хранилища, связанного с состоянием постов, сообщениями и эффектами.
 *
 * @see Store Базовый класс хранилища, который управляет состоянием и эффектами.
 * @see PostWallState Состояние, связанное с постами на стене пользователя.
 * @see PostWallMessage Сообщения, которые могут изменять состояние постов.
 * @see PostWallEffect Эффекты, которые выполняются в ответ на сообщения.
 */
typealias PostWallStore = Store<PostWallState, PostWallMessage, PostWallEffect>
