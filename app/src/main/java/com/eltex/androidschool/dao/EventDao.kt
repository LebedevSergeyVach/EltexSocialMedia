package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

import kotlinx.coroutines.flow.Flow

import com.eltex.androidschool.db.EventTableInfo
import com.eltex.androidschool.entity.EventEntity

/**
 * Data Access Object (DAO) для работы с сущностями [EventEntity] в базе данных.
 */
@Dao
interface EventDao {
    /**
     * Получает все события из базы данных, отсортированные по идентификатору в порядке убывания.
     *
     * @return Flow со списком событий.
     */
    @Query(
        """
            SELECT * FROM ${EventTableInfo.TABLE_NAME} ORDER BY id DESC
        """
    )
    fun getAll(): Flow<List<EventEntity>>

    /**
     * Сохраняет или обновляет событие в базе данных.
     *
     * @param event Событие для сохранения или обновления.
     * @return Идентификатор сохраненного или обновленного события.
     */
    @Upsert
    fun save(event: EventEntity): Long

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    @Query(
        """
            UPDATE ${EventTableInfo.TABLE_NAME} SET
                likeByMe =  CASE WHEN likeByMe THEN 0 ELSE 1 END
            WHERE id = :eventId
        """
    )
    fun likeById(eventId: Long)

    /**
     * Переключает состояние участия у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    @Query(
        """
            UPDATE ${EventTableInfo.TABLE_NAME} SET
                participateByMe =  CASE WHEN participateByMe THEN 0 ELSE 1 END
            WHERE id = :eventId
        """
    )
    fun participateById(eventId: Long)

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    @Query(
        """
            DELETE FROM ${EventTableInfo.TABLE_NAME} WHERE id = :eventId
        """
    )
    fun deleteById(eventId: Long)

    /**
     * Обновляет содержимое события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param content Новое содержимое события.
     * @param link Ссылка на событие.
     * @param option Вариант проведения события.
     * @param data Дата события.
     * @param lastModified Дата и время последнего изменения.
     */
    @Query(
        """
            UPDATE ${EventTableInfo.TABLE_NAME} 
                SET content = :content, link = :link, optionConducting = :option, dataEvent = :data, lastModified = :lastModified WHERE id = :eventId
        """
    )
    fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String,
        lastModified: String
    )

    @Query(
        """
            SELECT * FROM ${EventTableInfo.TABLE_NAME} WHERE id = :eventId
        """
    )
    fun getEventById(eventId: Long): EventEntity
}
