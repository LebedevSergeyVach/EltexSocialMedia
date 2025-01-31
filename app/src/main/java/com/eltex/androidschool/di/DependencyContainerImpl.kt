package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.api.common.OkHttpClientFactory
import com.eltex.androidschool.api.common.RetrofitFactory
import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.api.jobs.JobsApi
import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.api.users.UsersApi
import com.eltex.androidschool.factory.events.EventStoreFactory
import com.eltex.androidschool.factory.posts.PostStoreFactory
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.repository.events.NetworkEventRepository
import com.eltex.androidschool.repository.jobs.JobRepository
import com.eltex.androidschool.repository.jobs.NetworkJobRepository
import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.repository.users.NetworkUserRepository
import com.eltex.androidschool.repository.users.UserRepository
import com.eltex.androidschool.utils.DnsSelector
import com.eltex.androidschool.viewmodel.events.events.EventViewModel
import com.eltex.androidschool.viewmodel.events.eventwall.EventWallViewModel
import com.eltex.androidschool.viewmodel.events.newevent.NewEventViewModel
import com.eltex.androidschool.viewmodel.jobs.jobswall.JobViewModel
import com.eltex.androidschool.viewmodel.jobs.newjob.NewJobViewModel
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostViewModel
import com.eltex.androidschool.viewmodel.posts.post.PostViewModel
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallViewModel
import com.eltex.androidschool.viewmodel.user.UserViewModel
import com.eltex.androidschool.viewmodel.users.UsersViewModel
import kotlinx.serialization.json.Json

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create

/**
 * Реализация контейнера зависимостей.
 * Этот класс предоставляет фабрики для создания ViewModel и инициализирует зависимости,
 * такие как репозитории и API.
 *
 * @see DependencyContainer
 * @see ViewModelProvider.Factory
 */
class DependencyContainerImpl : DependencyContainer {

    /**
     * Фабрика для создания экземпляра OkHttpClient.
     *
     * Этот объект отвечает за настройку и создание экземпляра OkHttpClient, который используется для выполнения сетевых запросов.
     * OkHttpClient настроен с использованием интерсепторов для добавления заголовков и логирования.
     *
     * Экземпляр OkHttpClient, настроенный для работы с API, настроенный для выполнения HTTP-запросов.
     *
     * - Установлено время ожидания соединения в 30 секунд.
     * - В режиме отладки (BuildConfig.DEBUG) добавлен интерсептор [HttpLoggingInterceptor] для логирования тела запросов и ответов.
     * - Добавлен интерсептор для установки заголовков [API_KEY] и [API_AUTHORIZATION] на основе значений из [BuildConfig].
     * - Используется пользовательская DNS-реализация [DnsSelector].
     *
     * Ленивая инициализация, чтобы создать экземпляр OkHttpClient только при первом обращении.
     * Этот клиент используется для выполнения сетевых запросов к API.
     */
    private val okHttpClient: OkHttpClient = OkHttpClientFactory.createOkHttpClient()

    /**
     * Фабрика для создания экземпляра Retrofit.
     *
     * Этот объект отвечает за настройку и создание экземпляра Retrofit, который используется для выполнения сетевых запросов.
     * Retrofit настроен с использованием OkHttpClient и конвертера для работы с JSON.
     *
     * Ленивая инициализация экземпляра Retrofit.
     * Настраивает базовый URL, конвертер JSON и клиент OkHttp.
     *
     * @see Retrofit Основной класс для работы с сетевыми запросами.
     * @see RxJava3CallAdapterFactory Адаптер для интеграции RxJava3 с Retrofit.
     * @see Json Конфигурация JSON-сериализации.
     *
     * @return Настроенный экземпляр Retrofit.
     * @see [OkHttpClientFactory]
     */
    private val retrofit: Retrofit = RetrofitFactory.createRetrofit(okHttpClient)

    /**
     * API для работы с постами.
     * Создается с помощью Retrofit.
     *
     * @see PostsApi
     */
    private val postsApi: PostsApi = retrofit.create()

    /**
     * API для работы с событиями.
     * Создается с помощью Retrofit.
     *
     * @see EventsApi
     */
    private val eventsApi: EventsApi = retrofit.create()

    /**
     * API для работы с пользователями.
     * Создается с помощью Retrofit.
     *
     * @see UsersApi
     */
    private val usersApi: UsersApi = retrofit.create()

    /**
     * API для работы с медиафайлами.
     * Создается с помощью Retrofit.
     *
     * @see MediaApi
     */
    private val mediaApi: MediaApi = retrofit.create()

    /**
     * API для работы с вакансиями.
     * Создается с помощью Retrofit.
     *
     * @see JobsApi
     */
    private val jobsApi: JobsApi = retrofit.create()

    /**
     * Репозиторий для работы с постами.
     * Использует [postsApi] и [mediaApi] для выполнения операций с постами.
     *
     * @see PostRepository
     * @see NetworkPostRepository
     */
    private val postRepository: PostRepository = NetworkPostRepository(
        postsApi = postsApi,
        mediaApi = mediaApi,
    )

    /**
     * Репозиторий для работы с событиями.
     * Использует [eventsApi] и [mediaApi] для выполнения операций с событиями.
     *
     * @see EventRepository
     * @see NetworkEventRepository
     */
    private val eventRepository: EventRepository = NetworkEventRepository(
        eventsApi = eventsApi,
        mediaApi = mediaApi,
    )

    /**
     * Репозиторий для работы с пользователями.
     * Использует [usersApi] для выполнения операций с пользователями.
     *
     * @see UserRepository
     * @see NetworkUserRepository
     */
    private val userRepository: UserRepository = NetworkUserRepository(
        usersApi = usersApi,
    )

    /**
     * Репозиторий для работы с вакансиями.
     * Использует [jobsApi] для выполнения операций с вакансиями.
     *
     * @see JobRepository
     * @see NetworkJobRepository
     */
    private val jobRepository: JobRepository = NetworkJobRepository(
        jobsApi = jobsApi,
    )

    /**
     * Фабрика для создания хранилищ, связанных с постами.
     * Использует [postRepository] для создания [PostStore] и [PostWallStore].
     *
     * @see PostStoreFactory
     */
    private val postStoreFactory = PostStoreFactory(repository = postRepository)

    /**
     * Фабрика для создания хранилищ, связанных с событиями.
     * Использует [eventRepository] для создания [EventStore].
     *
     * @see EventStoreFactory
     */
    private val eventStoreFactory = EventStoreFactory(repository = eventRepository)

    /**
     * Возвращает фабрику для создания [PostViewModel].
     *
     * @return [ViewModelProvider.Factory] Фабрика для создания [PostViewModel].
     *
     * @see PostViewModel
     */
    override fun getPostsViewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    postStore = postStoreFactory.createPostFactory()
                )
            }
        }

    /**
     * Возвращает фабрику для создания [NewPostViewModel].
     *
     * @param postId Идентификатор поста.
     * @return [ViewModelProvider.Factory] Фабрика для создания [NewPostViewModel].
     *
     * @see NewPostViewModel
     */
    override fun getNewPostViewModelFactory(postId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    repository = postRepository,
                    postId = postId,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [PostWallViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания [PostWallViewModel].
     *
     * @see PostWallViewModel
     */
    override fun getPostsWallViewModelFactory(userId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(PostWallViewModel::class) {
                PostWallViewModel(
                    postWallStore = postStoreFactory.createPostsWallFactory(userId = userId)
                )
            }
        }

    /**
     * Возвращает фабрику для создания [EventViewModel].
     *
     * @return [ViewModelProvider.Factory] Фабрика для создания [EventViewModel].
     *
     * @see EventViewModel
     */
    override fun getEventsViewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    eventStore = eventStoreFactory.createEventFactory(),
                )
            }
        }

    /**
     * Возвращает фабрику для создания [NewEventViewModel].
     *
     * @param eventId Идентификатор события.
     * @return [ViewModelProvider.Factory] Фабрика для создания [NewEventViewModel].
     *
     * @see NewEventViewModel
     */
    override fun getNewEventViewModelFactory(eventId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(NewEventViewModel::class) {
                NewEventViewModel(
                    repository = eventRepository,
                    eventId = eventId,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [EventWallViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания [EventWallViewModel].
     *
     * @see EventWallViewModel
     */
    override fun getEventsWallViewModelFactory(userId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(EventWallViewModel::class) {
                EventWallViewModel(
                    repository = eventRepository,
                    userId = userId,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [UserViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания [UserViewModel].
     *
     * @see UserViewModel
     */
    override fun getUsersViewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(UsersViewModel::class) {
                UsersViewModel(
                    repository = userRepository,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [UserViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания [UserViewModel].
     *
     * @see UserViewModel
     */
    override fun getAccountViewModelFactory(userId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(UserViewModel::class) {
                UserViewModel(
                    repository = userRepository,
                    userId = userId,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [JobViewModel].
     *
     * @param userId Идентификатор пользователя.
     * @return [ViewModelProvider.Factory] Фабрика для создания [JobViewModel].
     *
     * @see JobViewModel
     */
    override fun getJobWallViewModelFactory(userId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(JobViewModel::class) {
                JobViewModel(
                    repository = jobRepository,
                    userId = userId,
                )
            }
        }

    /**
     * Возвращает фабрику для создания [NewJobViewModel].
     *
     * @param jobId Идентификатор вакансии.
     * @return [ViewModelProvider.Factory] Фабрика для создания [NewJobViewModel].
     *
     * @see NewJobViewModel
     */
    override fun getNewJobViewModelFactory(jobId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(NewJobViewModel::class) {
                NewJobViewModel(
                    repository = jobRepository,
                    jobId = jobId,
                )
            }
        }
}
