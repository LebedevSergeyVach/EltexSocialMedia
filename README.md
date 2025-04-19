# Eltex Social Media
<a name="up"></a>

---

🚀 **The project is written in the [Kotlin](https://kotlinlang.org) programming language**

Educational project Development of Android applications in Java and Kotlin from Eltex Academy.
This project is based on the **MVVM** architecture **(Model-View-ViewModel)**.
The **MVI** (Model-View-Intent) architecture is used to process and download data from the server.

The project implements working with the server to perform **CRUD** operations (Create, Read, Update, Delete) with posts and events.
The [**Retrofit2**](https://github.com/square/retrofit) library is used to interact with the server along with [**Coroutines**](https://github.com/Kotlin/kotlinx.coroutines ), which allows asynchronous requests to be executed without blocking the main thread.
To implement the [**DI Dependency Injection**](https://developer.android.com/training/dependency-injection), the [**Dagger2-Hilt**](https://developer.android.com/training/dependency-injection/hilt-android) library and the Singleton antipattern removal **Factory** are used.

To work with the local database **SQLite**, **[ORM ROOM](https://developer.android.com/training/data-storage/room)** is used together with **[ksp](https://developer.android.com/jetpack/androidx/releases/room)**.
**[The Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** is used in the project for convenient testing.
Using **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** to work with local files on the device.

To work with requests to the server and process the received data in JSON format, the **[Retrofit2](https://github.com/square/retrofit)** and **[OkHttp3](https://github.com/square/okhttp)** libraries from **[Square](https://github.com/square)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** from **[ReactiveX](https://github.com/ReactiveX)** are used to implement reactive programming, instead of accumulating Callbacks. The project has been rewritten from reactive programming on **[Retrofit2](https://github.com/square/retrofit)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** to standard **[Retrofit2](https://github.com/square/retrofit)** + **[Coroutines](https://github.com/Kotlin/kotlinx.coroutines)** (suspend).

The project uses several types of architecture patterns and paradigms: **DI Dependency Injection**, **Builder**, **Observable**, **Observer**, **Adapter**, **Dependency Injection**, **Single Activity Architecture**, **Modular Architecture**, **Reactive Programming** and **Factory**.

To animate the loading of a list of posts or events in the **UI**, the [**SkeletonLayout**](https://github.com/Faltenreich/SkeletonLayout) library is used: displaying skeletons during initial loading and loading of the next page.
The [**Glide**](https://github.com/bumptech/glide) library is used to manage multimedia and download images from the server for display in the **UI**.

Basic settings for the application's appearance are implemented, such as: language selection (Russian, English, system) and theme selection (dark, light, system). The ability to enable and disable vibration feedback in the application, display the size and clear the application cache has also been added.

---

## 🖥️ Server Infrastructure

The project uses a [**home server**](https://socialmedianetwork.serphantom.space/) for backend deployment. This solution provides full control over the infrastructure, flexibility in configuration, and cost savings on cloud resources.

### 🛠️ Key Technologies and Tools

- **Docker** and **Docker Compose**: Used for containerization and service management. All system components (backend, database, web server) are deployed in isolated containers, simplifying deployment and scaling.
- **Debian**: The server's operating system. Chosen for its stability, security, and extensive customization options.
- **PostgreSQL**: A powerful and reliable relational database used for storing application data.
- **Spring Boot**: A framework for backend development in **Kotlin**. It ensures high performance, ease of development, and seamless integration with other system components.
- **Nginx**: A web server and reverse proxy server. Used for request routing, load balancing, and serving static files.
- **HTTPS**: Ensures secure communication between the client and server using **SSL/TLS** certificates.
- **ImageKit.io**: A service for image optimization and delivery. Integrated into the project for fast media file loading.

### 📦 Deployment

The project is deployed on the home server using **`docker-compose`**, which automates the launch of all necessary services. Below is an example configuration:

### 🔒 Security

- All external requests pass through **Nginx**, which provides protection against **DDoS** attacks and filters unwanted traffic.
- Data encryption is ensured using **HTTPS** with certificates from [**Let's Encrypt**](https://letsencrypt.org/).
- The **PostgreSQL** database is configured to use **SSL** for encrypted connections.

### 🌐 Integration with [**ImageKit.io**](https://imagekit.io/)

The project uses [**ImageKit.io**](https://imagekit.io/) for image optimization and delivery. This allows:
- Reducing server load through caching and **CDN**.
- Automatically optimizing images for different devices and resolutions.
- Simplifying media file management through a user-friendly interface.

---

### 📄 Documentation for the project [**documentation**](documentation/documentation.md).
### 🖼️ View images of the mobile app [**Eltex Social Media**](documentation/README.md).

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Version](https://img.shields.io/badge/Version-2.1.0-green.svg)](https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v2.1.0)

<details open="open">
    <summary><h2>🚀 The stack of technologies used</h2></summary>

| [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" width="45" height="45" alt="Kotlin" />](https://kotlinlang.org/) | [<img src="https://uploads-ssl.webflow.com/60996f3af06ca2ff488a7bfb/60a269bf446a85794a4d3b6b_Retrofit.jpg" width="45" height="45" alt="Retrofit" />](https://github.com/square/retrofit) | [<img src="https://avatars.githubusercontent.com/u/82592?s=48&v=4" width="45" height="45" alt="OkHttp" />](https://github.com/square/okhttp) | [<img src="https://s3.amazonaws.com/playstore/images/60bb08c2fc6d0bddb91e0e3553dcdb48" width="45" height="45" alt="Glide" />](https://github.com/bumptech/glide) | [<img src="https://koenig-media.raywenderlich.com/uploads/2020/02/AdvancedDagger2-feature.png" width="45" height="45" alt="Dagger Hilt" />](https://developer.android.com/training/dependency-injection/hilt-android) | [<img src="https://oracle-base.com/blog/wp-content/uploads/2016/10/materialdesign_principles_metaphor.png" width="45" height="45" alt="Google Material Design" />](https://github.com/material-components/material-components-android) | [<img src="https://services.google.com/fh/files/emails/android_dev_newsletter_feb_image3.png" width="45" height="45" alt="AndroidX Jetpack" />](https://developer.android.com/jetpack) | [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" width="45" height="45" alt="Gradle" />](https://gradle.org/) | [<img src="https://raw.githubusercontent.com/Faltenreich/SkeletonLayout/refs/heads/develop/images/android.png" width="45" height="45" alt="SkeletonLayout" />](https://github.com/Faltenreich/SkeletonLayout) |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                           Kotlin                                                                           |                                                                                         Retrofit                                                                                         |                                                                    OkHttp                                                                    |                                                                              Glide                                                                               |                                                                                                      Dagger Hilt                                                                                                      |                                                                                                            Material Design                                                                                                             |                                                                                    AndroidX Jetpack                                                                                    |                                                                         Gradle                                                                         |                                                                                                Skeleton Layout                                                                                                |

</details>

---

> [!IMPORTANT]
> ## **📱 Project Description**
>
> An educational project for the development of Android applications in Java and Kotlin from Eltex Academy.
> The application is a social network that will allow users to create posts and events, leave comments and share their opinions under posts, indicate their places of work and much more.

> [!NOTE]
> ## **✨ Features and functionality of the application**
>
> ### **📝 Posts**
> - Create, edit, and delete posts:
> - - Write a text for a new post.
> - - Attach an image to a new post.
> - - Edit or delete an existing post.
> - Watch the feed of posts from all users.
> - Please like the post.
> - Share your opinion in the comments.
>
> ### **📅 Events**
> - Create, edit, and delete events:
> - - Write a text for a new event.
> - - Choose the date and time for the event.
> - - Attach a link or an address for the event.
> - - Attach an image to a new event.
> - - Edit or delete existing events.
> - Watch the feed of events from all users.
> - Please likes and participate in the event.
>
> ### **📱 Detailed viewing**
> - See the detailed description of the posts and events.
> - See who liked the event and who is participating in it.
> - See the comments and share your comments.
> - Please like and participate yourself.
>
> ### **💭 Comments**
> - Share your opinion and information with other users of the app under the posts.
> - - Write a new comment.
> - - Delete your comments.
> - Please like other comments.
>
> ### **👤 Account**
> - Registering a new account:
> - - Specify the username, name, and avatar for your account.
> - Manage your posts, events, and work/study information.
>
> ### **👥 Profile**
> - View the profile of other users.
> - View all the posted information from the user.
> - See the list of all users of the application.
>
> ### **🏢 Information about places of work**
> - Create new information about your place of work/study:
> - - Please provide information about the company: the name, your position, the reference and the period of employment.
> - See other users' places of work/study.
>
> ### **⚙️ Settings**
> - Choose an application theme:
> - - Light.
> - - Dark .
> - - System.
> - Choose the application language:
> - - English.
> - - Russian.
> - - System.
> - Turn on and off the vibration response in the app.
> - Keep track of the cache size and also clear it in the application.
> - Keep an eye on all app updates.
>
> ### **🚧 The project is under active development!**
>
> Application version 2.1.0
> At the moment, work is underway to improve the functionality of the application, add new features to the project, as well as solve possible problems and bugs.
>
> ### 📅 Further development plans:
> - Add features to the app for working with voice posts/events and videos.
> - Improve caching of data downloaded from the server.
> - Implement a fragment displaying data about a post/event:
> - - Displaying information about a post/event;- Completed!
> - - Users who liked/participated;- Completed!
> - - Comments;- Completed!
> - - Displaying the location/event on the map;
> - - Mentioning other users when creating a post/event.
>
> ### 🤝 Project development assistance:
> - If you find a bug or have an idea for improvement, please create [**issue**](https://github.com/LebedevSergeyVach/EltexSocialMedia/issues).
> - You can also suggest your changes or implement new features/functions via [**pull request**](https://github.com/LebedevSergeyVach/EltexSocialMedia/pulls).

> [!WARNING]
> ### **🔧 Compilation of the project**
>
> In order to build a project, you need to create **`secrets.properties`** in the root of the project:
>
>```properties
>    API_KEY="The key to access the server" Name: Authorization (apiKey) String
>    URL_SERVER="the URL of the connected server" String
>```

---

### [Anatoly Spitchenko](https://github.com/Onotole1) - Android developer and Course Teacher

### [Lebedev Sergey Vyacheslav](https://github.com/LebedevSergeyVach) – Student of the course

---

#### [README](README.md) [UP](#up)

---

# Социальная медиа сеть Eltex
<a name="вверх"></a>

---

🚀 **Проект написан на языке программирования [Kotlin](https://kotlinlang.org).**

Образовательный проект по разработке Android-приложений на Java и Kotlin от Eltex Academy.
Данный проект основан по архитектуре **MVVM** **(Model-View-ViewModel)**.
Для обработки и загрузки данных с сервера используется архитектура **MVI** (Model-View-Intent).

В проекте реализована работа с сервером для выполнения операций **CRUD** (Create, Read, Update, Delete) с постами и событиями.
Для взаимодействия с сервером используется библиотека [**Retrofit2**](https://github.com/square/retrofit) вместе с [**Coroutines**](https://github.com/Kotlin/kotlinx.coroutines), что позволяет выполнять асинхронные запросы без блокировки основного потока.
Для реализации [**Внедрение зависимостей DI**](https://developer.android.com/training/dependency-injection) используется библиотека [**Dagger2-Hilt**](https://developer.android.com/training/dependency-injection/hilt-android) и паттерн **Фабрика** для удаления антипаттерн **Singleton**.

Для работы с локальной базой данных **SQLite** используется **[ORM ROOM](https://developer.android.com/training/data-storage/room)** вместе с **[ksp](https://developer.android.com/jetpack/androidx/releases/room)**.
**[The Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** используется в проекте для удобства тестирования.
Используется **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** для работы с локальными файлами на устройстве.

Для работы с запросами на сервер и обработкой полученных данных в формате JSON используется библиотеки **[Retrofit2](https://github.com/square/retrofit)** и **[OkHttp3](https://github.com/square/okhttp)** от **[Square](https://github.com/square)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** от **[ReactiveX](https://github.com/ReactiveX)** для реализации реактивного программировая, вместо накапливающихся Callback - ов. Проект переписан с реактивного программирования на **[Retrofit2](https://github.com/square/retrofit)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** на стандартные **[Retrofit2](https://github.com/square/retrofit)** + **[Coroutines](https://github.com/Kotlin/kotlinx.coroutines)** (suspend).

В проекте используется несколько типов архитектурных паттернов и парадигм: **DI Внедрение зависимостей**, **Строитель**, **Наблюдаемый**, **Наблюдатель**, **Адаптер**, **Внедрение зависимостей**, **Архитектура с одной активностью**, **Модульная архитектура**, **Реактивное программирование** и **Фабрика**.

Для анимации загрузки в **UI** списка данных постов или событий используется библиотека [**SkeletonLayout**](https://github.com/Faltenreich/SkeletonLayout): отображение скелетонов при начальной загрузке и загрузке следующей страницы.
Для управления мультимедиа и загрузки изображений с сервера для отображения в **UI** используется библиотека [**Glide**](https://github.com/bumptech/glide).

Реализованы базовые настройки внешнего вида приложения, такие как: выбор языка (Русский, Английский, системный) и выбор темы (темная, светлая, системная). Также добавлена возможность включения и отключения виброотклика в приложении, показ размера и очистка кэша приложения.

---

## 🖥️ Серверная инфраструктура

Проект использует [**домашний сервер**](https://socialmedianetwork.serphantom.space/) для развертывания бэкенда.
Это решение обеспечивает полный контроль над инфраструктурой, гибкость в настройке и экономию на облачных ресурсах.

### 🛠️ Основные технологии и инструменты

- **Docker** и **Docker Compose**: Для контейнеризации и управления сервисами. Все компоненты системы (бэкенд, база данных, веб-сервер) развернуты в изолированных контейнерах, что упрощает развертывание и масштабирование.
- **Debian**: Операционная система сервера. Выбор обусловлен стабильностью, безопасностью и широкими возможностями для настройки.
- **PostgreSQL**: Мощная и надежная реляционная база данных, используемая для хранения данных приложения.
- **Spring Boot**: Фреймворк для разработки бэкенда на **Kotlin**. Обеспечивает высокую производительность, простоту разработки и интеграцию с другими компонентами системы.
- **Nginx**: Веб-сервер и обратный прокси-сервер. Используется для маршрутизации запросов, балансировки нагрузки и обслуживания статических файлов.
- **HTTPS**: Для обеспечения безопасного соединения между клиентом и сервером используется **SSL/TLS** сертификат.
- **ImageKit.io**: Сервис для оптимизации и доставки изображений. Интегрирован с проектом для быстрой загрузки медиафайлов.

### 📦 Развертывание

Для развертывания проекта на домашнем сервере используется **`docker-compose`**, который автоматизирует запуск всех необходимых сервисов. Пример конфигурации:

### 🔒 Безопасность

- Все внешние запросы проходят через **Nginx**, который обеспечивает защиту от **DDoS**-атак и фильтрацию нежелательного трафика.
- Для шифрования данных используется **HTTPS** с сертификатами от [**Let's Encrypt**](https://letsencrypt.org/).
- База данных **PostgreSQL** настроена на использование **SSL** для шифрования соединений.

### 🌐 Интеграция с [**ImageKit.io**](https://imagekit.io/)

Для оптимизации и доставки изображений используется сервис [**ImageKit.io**](https://imagekit.io/). Это позволяет:
- Уменьшить нагрузку на сервер за счет кэширования и **CDN**.
- Автоматически оптимизировать изображения для разных устройств и разрешений.
- Упростить управление медиафайлами через удобный интерфейс.

---

### 📄 Документация к проеку [**documentation**](documentation/documentation.md).
### 🖼️ Посмотреть изображения мобильного приложения [**Eltex Social Media**](documentation/README.md).

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Version](https://img.shields.io/badge/Version-2.1.0-green.svg)](https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v2.1.0)

<details open="open">
    <summary><h2>🚀 Стек используемых технологий</h2></summary>

| [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" width="45" height="45" alt="Kotlin" />](https://kotlinlang.org/) | [<img src="https://uploads-ssl.webflow.com/60996f3af06ca2ff488a7bfb/60a269bf446a85794a4d3b6b_Retrofit.jpg" width="45" height="45" alt="Retrofit" />](https://github.com/square/retrofit) | [<img src="https://avatars.githubusercontent.com/u/82592?s=48&v=4" width="45" height="45" alt="OkHttp" />](https://github.com/square/okhttp) | [<img src="https://s3.amazonaws.com/playstore/images/60bb08c2fc6d0bddb91e0e3553dcdb48" width="45" height="45" alt="Glide" />](https://github.com/bumptech/glide) | [<img src="https://koenig-media.raywenderlich.com/uploads/2020/02/AdvancedDagger2-feature.png" width="45" height="45" alt="Dagger Hilt" />](https://developer.android.com/training/dependency-injection/hilt-android) | [<img src="https://oracle-base.com/blog/wp-content/uploads/2016/10/materialdesign_principles_metaphor.png" width="45" height="45" alt="Google Material Design" />](https://github.com/material-components/material-components-android) | [<img src="https://services.google.com/fh/files/emails/android_dev_newsletter_feb_image3.png" width="45" height="45" alt="AndroidX Jetpack" />](https://developer.android.com/jetpack) | [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" width="45" height="45" alt="Gradle" />](https://gradle.org/) | [<img src="https://raw.githubusercontent.com/Faltenreich/SkeletonLayout/refs/heads/develop/images/android.png" width="45" height="45" alt="SkeletonLayout" />](https://github.com/Faltenreich/SkeletonLayout) |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                           Kotlin                                                                           |                                                                                         Retrofit                                                                                         |                                                                    OkHttp                                                                    |                                                                              Glide                                                                               |                                                                                                      Dagger Hilt                                                                                                      |                                                                                                            Material Design                                                                                                             |                                                                                    AndroidX Jetpack                                                                                    |                                                                         Gradle                                                                         |                                                                                                Skeleton Layout                                                                                                |

</details>

---

> [!IMPORTANT]
> ## **📱 Описание приложения**
>
> Образовательный проект по разработке Android-приложений на Java и Kotlin от Eltex Academy.
> Приложение представляет собой социальную сеть, которая позволит пользователям создавать посты и события, оставлять комменатрии и делиться своим мнением под постами, указывать места своей работы и многое другое.

> [!NOTE]
> ## **✨ Возможности и функционал приложения**
>
> ### **📝 Посты**
> - Создавайте, редактируйте и удаляйте посты:
> - - Пишите текст для нового поста.
> - - Прикрепляйте  изображение к новому посту.
> - - Редактируйте  или удаляйте существующий пост.
> - Смотрите ленту постов от всех пользователей.
> - Ставьте лайк посту.
> - Делитесь своим мнением в комментариях.
>
> ### **📅 События**
> - Создавайте, редактируйте и удаляйте события:
> - - Пишите текст для нового события.
> - - Выбирайте дату и время для проводимого события.
> - - Прикрепляйте ссылку или адрес для проводимого события.
> - - Прикрепляйте  изображение к новому событию.
> - - Редактируйте или удаляйте существующие событие.
> - Смотрите ленту событий от всех пользователей.
> - Ставьте лайки и участвуйте в мероприятии.
> 
> ### **📱 Подробный просмотр**
> - Смотрите подробное описание постов и событий. 
> - Посмотрите, кому понравилось мероприятие и кто в нем участвует.
> - Смотрите комментарии и делитесь комментариями.
> - Ставьте лайки и участвуйте сами.
> 
> ### **💭 Комментарии**
> - Делитесь мнением и информацией с другими пользователями приложения под постами.
> - - Пишите новый комментарий.
> - - Удаляйте свои комментарии.
> - Ставьте лайки другим комментариям.
>
> ### **👤 Аккаунт**
> - Регистрируете новый аккаунт:
> - - Указывайте логин, имя и аватар для Вашего аккаунта.
> - Управляйте своими постами, событиями и информации о работе/учебе.
>
> ### **👥 Профиль**
> - Просматривайте профиль других пользователей.
> - Просматривайте всю выложенную информацию от пользователя.
> - Смотрите список всех пользователей приложения.
>
> ### **🏢 Информация о местах работы**
> - Создавайте новую информацию о месте Вашей работы/учебы:
> - - Указывайте информацию о компании: название, Ваша должность, ссылка и промежуток трудоустройства.
> - Смотрите места работы/учебы у других пользователей.
>
> ### **⚙️ Настройки**
> - Выбирайте тему приложения:
> - - Светлая.
> - - Темная.
> - - Системная.
> - Выбирайте язык приложения:
> - - Русский.
> - - Английский.
> - - Системный.
> - Включайте и отключайте виброотклик в приложении.
> - Следите за размером кэша, а также очищайте его в приложении.
> - Смотрите за всеми обновлениями приложения.
>
> ### **🚧 Проект находится в стадии активной разработки!**
>
> Версия приложения 2.1.0
> На данный момент идет работа над улучшением функциональности приложения, добавления новых фишек в проект, а также решение возможных проблем и багов.
>
> ### 📅 Планы дальнейшего развития:
> - Добавить функции в приложение по работе с голосовыми постами/события и видео.
> - Улучшить кэширование данных, загруженных с сервера.
> - Реализовать фрагмент с отображением данных о посте/событии:
> - - Отображение информации о посте/событии; - Выполнено!
> - - Пользователи, поставившие лайк/участие; - Выполнено!
> - - Комментарии; - Выполнено!
> - - Отображение места/проведения события на карте;
> - - Упоминание других пользователей при создании поста/события.
>
> ### 🤝 Помощь в развитии проекта:
> - Если Вы нашли баг или у вас есть идея для улучшения, пожалуйста, создайте [**issue**](https://github.com/LebedevSergeyVach/EltexSocialMedia/issues).
> - Вы также можете предложить свои изменения или реализацию новых фишек/функций через [**pull request**](https://github.com/LebedevSergeyVach/EltexSocialMedia/pulls).

> [!WARNING]
> ### **🔧 Компиляция проекта**
>
> Для того, чтобы собрать проект, необходимо создать **`secrets.properties`** в корне проекта:
>
>```properties
>    API_KEY="Ключ для доступа к серверу" Name: Authorization (apiKey) String
>    URL_SERVER="URL подключаемого сервера" String
>```

---

### [Анатолий Спитченко](https://github.com/Onotole1) - Android-Разработчик и преподаватель курса

### [Лебедев Сергей Вячеславович](https://github.com/LebedevSergeyVach) – Студент курса

---

#### [README](README.md) [ВВЕРХ](#вверх)
