package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

import kotlinx.coroutines.flow.Flow

import com.eltex.androidschool.db.EventTableInfo
import com.eltex.androidschool.entity.EventEntity

@Dao
interface EventDao {
    @Query(
        """
            SELECT * FROM ${EventTableInfo.TABLE_NAME} ORDER BY id DESC
        """
    )
    fun getAll(): Flow<List<EventEntity>>

    @Upsert
    fun save(event: EventEntity): Long

    @Query(
        """
            UPDATE ${EventTableInfo.TABLE_NAME} SET
                likeByMe =  CASE WHEN likeByMe THEN 0 ELSE 1 END
            WHERE id = :eventId
        """
    )
    fun likeById(eventId: Long)

    @Query(
        """
            UPDATE ${EventTableInfo.TABLE_NAME} SET
                participateByMe =  CASE WHEN participateByMe THEN 0 ELSE 1 END
            WHERE id = :eventId
        """
    )
    fun participateById(eventId: Long)

    @Query(
        """
            DELETE FROM ${EventTableInfo.TABLE_NAME} WHERE id = :eventId
        """
    )
    fun deleteById(eventId: Long)

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
}
