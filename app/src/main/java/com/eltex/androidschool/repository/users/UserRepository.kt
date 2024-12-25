package com.eltex.androidschool.repository.users

import com.eltex.androidschool.data.users.UserData

/**
 * Интерфейс для работы с данными пользователей.
 * Определяет методы для получения списка пользователей и пользователя по идентификатору.
 *
 * @see NetworkUserRepository Реализация интерфейса в памяти.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface UserRepository {

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей [UserData].
     */
    suspend fun getUsers(): List<UserData>

    /**
     * Получает пользователя по его уникальному идентификатору.
     *
     * @param userId Уникальный идентификатор пользователя.
     * @return Пользователь [UserData], соответствующий указанному идентификатору.
     */
    suspend fun getUserById(userId: Long): UserData
}
