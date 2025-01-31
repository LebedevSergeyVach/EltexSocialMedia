package com.eltex.androidschool.repository.users

import com.eltex.androidschool.api.users.UsersApi
import com.eltex.androidschool.data.users.UserData

/**
 * Репозиторий для работы с данными пользователей, полученными из сети.
 * Реализует интерфейс [UserRepository].
 *
 * @see UserRepository Интерфейс репозитория, который реализует этот класс.
 * @see UsersApi Интерфейс для работы с API полльзователей.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
class NetworkUserRepository(
    private val usersApi: UsersApi,
) : UserRepository {

    /**
     * Получает список всех пользователей из сети.
     *
     * @return Список пользователей [UserData].
     */
    override suspend fun getUsers(): List<UserData> =
        usersApi.getAllUsers()

    /**
     * Получает пользователя по его уникальному идентификатору.
     *
     * @param userId Уникальный идентификатор пользователя.
     * @return Пользователь [UserData], соответствующий указанному идентификатору.
     */
    override suspend fun getUserById(userId: Long) =
        usersApi.getUserById(userId = userId)
}
