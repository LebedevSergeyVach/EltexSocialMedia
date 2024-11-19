package com.eltex.androidschool.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import android.content.Context

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.serialization.json.Json

import java.time.LocalDateTime
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import kotlinx.serialization.encodeToString

import com.eltex.androidschool.data.PostData
import java.time.format.DateTimeFormatter

/**
 * Реализация интерфейса [PostRepository], хранящая данные о постах в памяти.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see PostRepository Интерфейс, который реализует этот класс.
 */
// InMemoryPostRepository -> PrefsPostRepository
class LocalPreferencesDataStoreJsonPostRepository(
    context: Context,
) : PostRepository {
    private companion object {
        const val NEXT_ID_KEY = "NEXT_ID_KEY"
        const val POSTS_FILE = "posts.json"
        const val POST_DATA_STORE = "posts"
    }

    private val applicationContext: Context = context.applicationContext

    private val postsFileDir: File = applicationContext.filesDir.resolve(POSTS_FILE)

//    private val prefs: SharedPreferences =
//        applicationContext.getSharedPreferences("posts", Context.MODE_PRIVATE)

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = POST_DATA_STORE)
    private val dataStore = applicationContext.dataStore

//    private var nextId: Long = 0L

    private var nextId: Long = runBlocking {
        dataStore.data.first()[longPreferencesKey(NEXT_ID_KEY)] ?: 0L
    }

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

    private val _state: MutableStateFlow<List<PostData>> = MutableStateFlow(readPosts())

    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @return Flow<List<Post>> Flow, излучающий список постов.
     */
    override fun getPost(): Flow<List<PostData>> = _state.asStateFlow()

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
        _state.update { posts: List<PostData> ->
            posts.filter { post: PostData ->
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
        _state.update { posts: List<PostData> ->
            posts.map { post: PostData ->
                if (post.id == postId) {
                    post.copy(
                        content = content,
                        lastModified = LocalDateTime.now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    )
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
        _state.update { posts: List<PostData> ->
            buildList(posts.size + 1) {
                add(
                    PostData(
                        id = nextId++,
                        content = content,
                        author = "Student",
                        published = LocalDateTime.now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    )
                )
                addAll(posts)
            }
        }

        sync()
    }

    /**
     * Синхронизирует данные с DataStore и файлом.
     */
    private fun sync() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[longPreferencesKey(NEXT_ID_KEY)] = nextId
            }
        }

        postsFileDir.bufferedWriter().use { bufferedWriter: BufferedWriter ->
            bufferedWriter.write(Json.encodeToString(_state.value))
        }

//        prefs.edit {
//            putLong(NEXT_ID_KEY, nextId)
//        }
//
//        postsFileDir.bufferedWriter().use { bufferedWriter: BufferedWriter ->
//            bufferedWriter.write(Json.encodeToString(_state.value))
//        }

        /*
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putLong(NEXT_ID_KEY, nextId)
        editor.putString(POSTS_KEY, Json.encodeToString(_state.value))

        editor.commit() // записывает в файл на текущем потоке и блокируя его
        editor.apply() // на фооновый поток передает задачу на, что нужно в фоне сохранить файл
        */
    }

    /**
     * Читает посты из файла.
     *
     * @return Список постов, прочитанных из файла.
     */
    private fun readPosts(): List<PostData> {
        return if (postsFileDir.exists()) {
            postsFileDir.bufferedReader().use { bufferedReader: BufferedReader ->
                Json.decodeFromString(bufferedReader.readText())
            }
        } else {
            emptyList()
        }
    }
}
