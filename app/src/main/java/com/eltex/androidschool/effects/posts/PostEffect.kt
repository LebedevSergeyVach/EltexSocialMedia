package com.eltex.androidschool.effects.posts

import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Запечатанный интерфейс, представляющий различные эффекты (действия), которые могут быть выполнены
 * в контексте постов. Эффекты используются для обработки бизнес-логики, такой как загрузка постов,
 * лайки и удаление.
 */
sealed interface PostEffect {

    /**
     * Эффект для загрузки следующей страницы постов.
     *
     * @property id Идентификатор последнего поста, начиная с которого нужно загрузить следующую страницу.
     * @property count Количество постов, которые нужно загрузить.
     */
    data class LoadNextPage(val id: Long, val count: Int) : PostEffect

    /**
     * Эффект для загрузки начальной страницы постов.
     *
     * @property count Количество постов, которые нужно загрузить.
     */
    data class LoadInitialPage(val count: Int) : PostEffect

    /**
     * Эффект для лайка поста.
     *
     * @property post Пост, который нужно лайкнуть или убрать лайк.
     */
    data class Like(val post: PostUiModel) : PostEffect

    /**
     * Эффект для удаления поста.
     *
     * @property post Пост, который нужно удалить.
     */
    data class Delete(val post: PostUiModel) : PostEffect
}
