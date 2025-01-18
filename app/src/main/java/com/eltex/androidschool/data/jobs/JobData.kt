package com.eltex.androidschool.data.jobs

import com.eltex.androidschool.data.common.InstantSerializer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import java.time.Instant

/**
 * Модель данных для места работы.
 *
 * Этот класс представляет данные о месте работы, полученные из API.
 *
 * @property id Идентификатор места работы.
 * @property name Название места работы.
 * @property position Должность.
 * @property start Дата начала работы.
 * @property finish Дата окончания работы.
 * @property link Ссылка на дополнительную информацию.
 */
@Serializable
data class JobData(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("name")
    val name: String = "",
    @SerialName("position")
    val position: String = "",
    @SerialName("start")
    @Serializable(with = InstantSerializer::class)
    val start: Instant = Instant.now(),
    @SerialName("finish")
    @Serializable(with = InstantSerializer::class)
    val finish: Instant = Instant.now(),
    @SerialName("link")
    val link: String = "",
)
