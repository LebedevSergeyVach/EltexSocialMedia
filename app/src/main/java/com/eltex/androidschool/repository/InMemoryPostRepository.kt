package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Post
import java.time.LocalDateTime

/**
 * Реализация интерфейса [PostRepository], хранящая данные о постах в памяти.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see PostRepository Интерфейс, который реализует этот класс.
 */
class InMemoryPostRepository : PostRepository {
    private var nextId: Long = 10L

    /**
     * Flow, хранящий текущее состояние списка постов.
     * Инициализируется списком из 20 постов с фиктивными данными.
     */
    private val _state = MutableStateFlow(
        List(nextId.toInt()) { int ->
            Post(
                id = int.toLong(),
                author = "Sergey Lebedev",
                published = LocalDateTime.now(),
                content = "№ ${int + 1} ❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F\n" + "\nLast Christmas, I gave you my heart\n" + "But the very next day, you gave it away\n" + "This year, to save me from tears\n" + "I'll give it to someone special\n" + "Last Christmas, I gave you my heart\n" + "But the very next day, you gave it away (You gave it away)\n" + "This year, to save me from tears\n" + "I'll give it to someone special (Special)",
            )
        }
            .reversed()
    )

    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @return Flow<List<Post>> Flow, излучающий список постов.
     */
    override fun getPost(): Flow<List<Post>> = _state.asStateFlow()

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     */
    override fun likeById(postId: Long) {
        _state.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    post.copy(likeByMe = !post.likeByMe)
                } else {
                    post
                }
            }
        }
    }

    /**
     * Удаления поста по его id.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    override fun deleteById(postId: Long) {
        _state.update { posts: List<Post> ->
            posts.filter { post: Post ->
                post.id != postId
            }
        }
    }

    /**
     * Обновляет пост по его id.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     */
    override fun updateById(postId: Long, content: String) {
        _state.update { posts: List<Post> ->
            posts.map { post: Post ->
                if (post.id == postId) {
                    post.copy(content = content, lastModified = LocalDateTime.now())
                } else {
                    post
                }
            }
        }
    }

    /**
     * Добавляет новый пост.
     *
     * @param content Содержание нового поста.
     */
    override fun addPost(content: String) {
        _state.update { posts: List<Post> ->
            buildList(posts.size + 1) {
                add(
                    Post(
                        id = nextId++,
                        content = content,
                        author = "Student",
                        published = LocalDateTime.now()
                    )
                )
                addAll(posts)
            }
        }
    }
}
