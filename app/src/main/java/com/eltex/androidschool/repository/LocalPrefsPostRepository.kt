package com.eltex.androidschool.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.serialization.json.Json

import java.time.LocalDateTime
import java.io.BufferedReader
import java.io.File

import com.eltex.androidschool.data.Post
import kotlinx.serialization.encodeToString
import java.io.BufferedWriter

/**
 * Реализация интерфейса [PostRepository], хранящая данные о постах в памяти.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see PostRepository Интерфейс, который реализует этот класс.
 */
// InMemoryPostRepository -> PrefsPostRepository
class LocalPrefsPostRepository(
    context: Context,
) : PostRepository {
    private val applicationContext: Context = context.applicationContext

    private val postsFileDir: File = applicationContext.filesDir.resolve(POSTS_FILE)

    /**
     * Computes the post count for the given post repository
     *
     * @constructor Create empty Companion
     */
    private companion object {
        const val NEXT_ID_KEY = "NEXT_ID_KEY"
        const val POSTS_FILE = "posts.json"
    }

    private val prefs: SharedPreferences =
        applicationContext.getSharedPreferences("posts", Context.MODE_PRIVATE)

    private var nextId: Long = 0L

    /**
     * Flow, хранящий текущее состояние списка постов.
     * Инициализируется списком из 20 постов с фиктивными данными.
     */
//    private val _state = MutableStateFlow(
//        List(nextId.toInt()) { int ->
//            Post(
//                id = int.toLong(),
//                author = "Sergey Lebedev",
//                published = LocalDateTime.now().toString(),
//                content = "№ ${int + 1} ❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F" +
//                        "☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F\n"
//                        + "\nLast Christmas, I gave you my heart\n"
//                        + "But the very next day, you gave it away\n"
//                        + "This year, to save me from tears\n"
//                        + "I'll give it to someone special\n"
//                        + "Last Christmas, I gave you my heart\n"
//                        + "But the very next day, you gave it away (You gave it away)\n"
//                        + "This year, to save me from tears\n"
//                        + "I'll give it to someone special (Special)",
//            )
//        }
//            .reversed()
//    )

    private val _state: MutableStateFlow<List<Post>> = MutableStateFlow(readPosts())

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

        sync()
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

        sync()
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
                    post.copy(content = content, lastModified = LocalDateTime.now().toString())
                } else {
                    post
                }
            }
        }

        sync()
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
                        published = LocalDateTime.now().toString()
                    )
                )
                addAll(posts)
            }
        }

        sync()
    }

    private fun sync() {
        prefs.edit {
            putLong(NEXT_ID_KEY, nextId)
        }

        postsFileDir.bufferedWriter().use {bufferedWriter: BufferedWriter ->
            bufferedWriter.write(Json.encodeToString(_state.value))
        }

        /*
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putLong(NEXT_ID_KEY, nextId)
        editor.putString(POSTS_KEY, Json.encodeToString(_state.value))

        editor.commit() // записывает в файл на текущем потоке и блокируя его
        editor.apply() // на фооновый поток передает задачу на, что нужно в фоне сохранить файл
        */
    }

    private fun readPosts(): List<Post> {
        return if (postsFileDir.exists()) {
            postsFileDir.bufferedReader().use { bufferedReader: BufferedReader ->
                Json.decodeFromString(bufferedReader.readText())
            }
        } else {
            emptyList()
        }
    }
}
