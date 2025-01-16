package com.eltex.androidschool.effects.posts

import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Запечатанный интерфейс, представляющий различные эффекты (действия), которые могут быть выполнены
 * в контексте постов на стене пользователя. Эффекты используются для обработки бизнес-логики, такой как загрузка постов,
 * лайки и удаление.
 */
sealed interface PostWallEffect {

    /**
     * Эффект для загрузки следующей страницы постов.
     *
     * @property authorId Идентификатор автора постов.
     * @property id Идентификатор последнего поста, начиная с которого нужно загрузить следующую страницу.
     * @property count Количество постов, которые нужно загрузить.
     */
    data class LoadNextPage(val authorId: Long, val id: Long, val count: Int) : PostWallEffect

    /**
     * Эффект для загрузки начальной страницы постов.
     *
     * @property authorId Идентификатор автора постов.
     * @property count Количество постов, которые нужно загрузить.
     */
    data class LoadInitialPage(val authorId: Long, val count: Int) : PostWallEffect

    /**
     * Эффект для лайка поста.
     *
     * @property post Пост, который нужно лайкнуть или убрать лайк.
     */
    data class Like(val post: PostUiModel) : PostWallEffect

    /**
     * Эффект для удаления поста.
     *
     * @property post Пост, который нужно удалить.
     */
    data class Delete(val post: PostUiModel) : PostWallEffect
}
