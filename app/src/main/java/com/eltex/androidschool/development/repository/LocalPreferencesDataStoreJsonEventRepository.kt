/*
package com.eltex.androidschool.development.repository

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

import com.eltex.androidschool.data.EventData
import com.eltex.androidschool.repository.EventRepository
import java.time.format.DateTimeFormatter

*/
/**
 * Реализация интерфейса [EventRepository], хранящая данные о событиях в памяти.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see EventRepository Интерфейс, который реализует этот класс.
 *//*

class LocalPreferencesDataStoreJsonEventRepository(
    context: Context,
) : EventRepository {
    private companion object {
        const val NEXT_ID_KEY = "NEXT_ID_KEY"
        const val EVENT_FILE = "events.json"
        const val EVENT_DATA_STORE = "events"
    }

    private val applicationContext: Context = context.applicationContext

    private val eventsFileDir: File = applicationContext.filesDir.resolve(EVENT_FILE)

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = EVENT_DATA_STORE)
    private val dataStore = applicationContext.dataStore

    private var nextId: Long = runBlocking {
        dataStore.data.first()[longPreferencesKey(NEXT_ID_KEY)] ?: 0L
    }

    private val _state: MutableStateFlow<List<EventData>> = MutableStateFlow(readPosts())

    */
/**
     * Возвращает Flow, который излучает список событий.
     *
     * @return Flow<List<Event>> Flow, излучающий список событий.
     *//*

    override fun getEvent(): Flow<List<EventData>> = _state.asStateFlow()

    */
/**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     *//*

    override fun likeById(eventId: Long) {
        _state.update { events: List<EventData> ->
            events.map { event: EventData ->
                if (event.id == eventId) {
                    event.copy(likeByMe = !event.likeByMe)
                } else {
                    event
                }
            }
        }

        sync()
    }

    */
/**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     *//*

    override fun participateById(eventId: Long) {
        _state.update { events: List<EventData> ->
            events.map { event: EventData ->
                if (event.id == eventId) {
                    event.copy(participateByMe = !event.participateByMe)
                } else {
                    event
                }
            }
        }

        sync()
    }

    */
/**
     * Удаления события по его id.
     *
     * @param eventId Идентификатор события, который нужно удалить.
     *//*

    override fun deleteById(eventId: Long) {
        _state.update { events: List<EventData> ->
            events.filter { event: EventData ->
                event.id != eventId
            }
        }

        sync()
    }

    */
/**
     * Обновляет событие по его id.
     *
     * @param eventId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание события.
     * @param link Новая ссылка события.
     *//*

    override fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ) {
        _state.update { events: List<EventData> ->
            events.map { event: EventData ->
                if (event.id == eventId) {
                    event.copy(
                        content = content,
                        link = link,
                        lastModified = LocalDateTime.now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        optionConducting = option,
                        dataEvent = data,
                    )
                } else {
                    event
                }
            }
        }

        sync()
    }

    */
/**
     * Добавляет новое событие.
     *
     * @param content Содержание нового события.
     * @param content Ссылка нового события.
     *//*

    override fun addEvent(content: String, link: String, option: String, data: String) {
        _state.update { events: List<EventData> ->
            buildList(events.size + 1) {
                add(
                    EventData(
                        id = nextId++,
                        content = content,
                        link = link,
                        author = "Student",
                        published = LocalDateTime.now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        optionConducting = option,
                        dataEvent = data,
                    )
                )
                addAll(events)
            }
        }

        sync()
    }

    */
/**
     * Синхронизирует данные с DataStore и файлом.
     *//*

    private fun sync() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[longPreferencesKey(NEXT_ID_KEY)] =
                    nextId
            }
        }

        eventsFileDir.bufferedWriter().use { bufferedWriter: BufferedWriter ->
            bufferedWriter.write(Json.encodeToString(_state.value))
        }
    }

    */
/**
     * Читает посты из файла.
     *
     * @return Список постов, прочитанных из файла.
     *//*

    private fun readPosts(): List<EventData> {
        return if (eventsFileDir.exists()) {
            eventsFileDir.bufferedReader().use { bufferedReader: BufferedReader ->
                Json.decodeFromString(bufferedReader.readText())
            }
        } else {
            emptyList()
        }
    }
}
*/
