# Eltex Social Media
<a name="up"></a>

---

**The project is written in the [Kotlin](https://kotlinlang.org) programming language**

Educational project Development of Android applications in Java and Kotlin from Eltex Academy.
This project is based on the **MVVM** architecture **(Model-View-ViewModel)**.
The **MVI** (Model-View-Intent) architecture is used to process and download data from the server.

The project implements working with the server to perform **CRUD** operations (Create, Read, Update, Delete) with posts and events.
The [**Retrofit2**](https://github.com/square/retrofit) library is used to interact with the server along with [**Coroutines**](https://github.com/Kotlin/kotlinx.coroutines ), which allows asynchronous requests to be executed without blocking the main thread.

To work with the local database **SQLite**, **[ORM ROOM](https://developer.android.com/training/data-storage/room)** is used together with **[ksp](https://developer.android.com/jetpack/androidx/releases/room)**.
**[The Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** is used in the project for convenient testing.
Using **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** to work with local files on the device.

To work with requests to the server and process the received data in JSON format, the **[Retrofit2](https://github.com/square/retrofit)** and **[OkHttp3](https://github.com/square/okhttp)** libraries from **[Square](https://github.com/square)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** from **[ReactiveX](https://github.com/ReactiveX)** are used to implement reactive programming, instead of accumulating Callbacks. The project has been rewritten from reactive programming on **[Retrofit2](https://github.com/square/retrofit)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** to standard **[Retrofit2](https://github.com/square/retrofit)** + **[Coroutines](https://github.com/Kotlin/kotlinx.coroutines)** (suspend).

The project uses several types of architecture patterns and paradigms: **Builder**, **Observable**, **Observer**, **Adapter**, **Dependency Injection**, **Single Activity Architecture**, **Modular Architecture** and **Reactive Programming**.

Basic settings for the application's appearance are implemented, such as: language selection (Russian, English, system) and theme selection (dark, light, system). The ability to turn on and off the vibration response in the app has also been added.

#### Documentation for the project [**documentation**](documentation/documentation.md).

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Version](https://img.shields.io/badge/Version-0.14.0-green.svg)](https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.13.0)

<details open="open">
    <summary><h2>🚀 The stack of technologies used</h2></summary>
    <div align="left">
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" height="40" alt="kotlin logo"  />
        <img width="12" />
        <img src="https://uploads-ssl.webflow.com/60996f3af06ca2ff488a7bfb/60a269bf446a85794a4d3b6b_Retrofit.jpg" height="40" alt="retrofit logo"  />
        <img width="12" />
        <img src="https://static.cdnlogo.com/logos/r/27/reactivex.svg" height="40" alt="rxjava logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" height="40" alt="gradle logo"  />
        <img width="12" />
        <img src="https://services.google.com/fh/files/emails/android_dev_newsletter_feb_image3.png" height="40" alt="androidx jetpack logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-plain-wordmark.svg" height="40" alt="android logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/sqlite/sqlite-original.svg" height="40" alt="sqlite logo"  />
        <img width="12" />
    </div>
</details>

---

### [Anatoly Spitchenko](https://gitflic.ru/user/onotole) - Android developer and Course Teacher

### [Lebedev Sergey Vyacheslav](https://github.com/LebedevSergeyVach) – Kotlin-Developer

---

#### [README](README.md) [UP](#up)

---

# Социальная сеть Eltex
<a name="вверх"></a>

---

**Проект написан на языке программирования [Kotlin](https://kotlinlang.org).**

Образовательный проект по разработке Android-приложений на Java и Kotlin от Eltex Academy.
Данный проект основан по архитектуре **MVVM** **(Model-View-ViewModel)**.
Для обработки и загрузки данных с сервера используется архитектура **MVI** (Model-View-Intent).

В проекте реализована работа с сервером для выполнения операций **CRUD** (Create, Read, Update, Delete) с постами и событиями.
Для взаимодействия с сервером используется библиотека [**Retrofit2**](https://github.com/square/retrofit) вместе с [**Coroutines**](https://github.com/Kotlin/kotlinx.coroutines), что позволяет выполнять асинхронные запросы без блокировки основного потока.

Для работы с локальной базой данных **SQLite** используется **[ORM ROOM](https://developer.android.com/training/data-storage/room)** вместе с **[ksp](https://developer.android.com/jetpack/androidx/releases/room)**.
**[The Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** используется в проекте для удобства тестирования.
Используется **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** для работы с локальными файлами на устройстве.

Для работы с запросами на сервер и обработкой полученных данных в формате JSON используется библиотеки **[Retrofit2](https://github.com/square/retrofit)** и **[OkHttp3](https://github.com/square/okhttp)** от **[Square](https://github.com/square)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** от **[ReactiveX](https://github.com/ReactiveX)** для реализации реактивного программировая, вместо накапливающихся Callback - ов. Проект переписан с реактивного программирования на **[Retrofit2](https://github.com/square/retrofit)** + **[RxJava3](https://github.com/ReactiveX/RxJava)** на стандартные **[Retrofit2](https://github.com/square/retrofit)** + **[Coroutines](https://github.com/Kotlin/kotlinx.coroutines)** (suspend).

В проекте используется несколько типов архитектурных паттернов и парадигм: **Строитель**, **Наблюдаемый**, **Наблюдатель**, **Адаптер**, **Внедрение зависимостей**, **Архитектура с одной активностью**, **Модульная архитектура** and **Реактивное программирование**.

Реализованы базовые настройки внешнего вида приложения, такие как: выбор языка (Русский, Английский, системный) и выбор темы (темная, светлая, системная). Также добавлена возможность включения и отключения виброотклика в приложении.

#### Документация к проеку [**documentation**](documentation/documentation.md).

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Version](https://img.shields.io/badge/Version-0.14.0-green.svg)](https://github.com/LebedevSergeyVach/EltexSocialMedia/releases/tag/v0.13.0)

<details open="open">
    <summary><h2>🚀 Стек используемых технологий</h2></summary>
    <div align="left">
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" height="40" alt="kotlin logo"  />
        <img width="12" />
        <img src="https://uploads-ssl.webflow.com/60996f3af06ca2ff488a7bfb/60a269bf446a85794a4d3b6b_Retrofit.jpg" height="40" alt="retrofit logo"  />
        <img width="12" />
        <img src="https://static.cdnlogo.com/logos/r/27/reactivex.svg" height="40" alt="rxjava logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" height="40" alt="gradle logo"  />
        <img width="12" />
        <img src="https://services.google.com/fh/files/emails/android_dev_newsletter_feb_image3.png" height="40" alt="androidx jetpack logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-plain-wordmark.svg" height="40" alt="android logo"  />
        <img width="12" />
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/sqlite/sqlite-original.svg" height="40" alt="sqlite logo"  />
        <img width="12" />
    </div>
</details>

---

### [Анатолий Спитченко](https://gitflic.ru/user/onotole) - Android-Разработчик и преподаватель курса

### [Лебедев Сергей Вячеславович](https://github.com/LebedevSergeyVach) – Kotlin-Разработчик

---

#### [README](README.md) [ВВЕРХ](#вверх)
