package com.eltex.androidschool.api.users

import com.eltex.androidschool.data.users.UserData

import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Интерфейс для взаимодействия с API, связанным с пользователями.
 * Использует Retrofit для выполнения сетевых запросов.
 *
 * @see UserData Класс, представляющий данные пользователя.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface UsersApi {

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей [UserData].
     */
    @GET("api/users")
    suspend fun getAllUsers(): List<UserData>

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя, который нужно получить.
     * @return [UserData] Пользователь, соответствующий указанному идентификатору.
     */
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): UserData

    /**
     * Объект-компаньон для создания экземпляра [UsersApi].
     */
    companion object {
        /**
         * Экземпляр [UsersApi], созданный с использованием [RetrofitFactoryUser].
         */
        val INSTANCE: UsersApi by lazy {
            RetrofitFactoryUser.INSTANCE.create<UsersApi>()
        }
    }
}
