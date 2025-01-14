# Документация к проекту
<a name="вверх"></a>

---

## 🛠️ Установка и запуск

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/LebedevSergeyVach/EltexSocialMedia.git
   ```

2. Откройте проект в Android Studio.

3. Соберите проект с помощью Gradle.

4. Запустите приложение на эмуляторе или физическом устройстве.

---

## 🌟 Функциональность

### 1. **Работа с постами и событиями**
   - Реализован **CRUD** (Create, Read, Update, Delete) для работы с постами и событиями.
   - Возможность создания, чтения, обновления и удаления постов и событий через удаленный сервер.
   - Использование **[Retrofit](https://square.github.io/retrofit/)** для отправки запросов на сервер и получения данных в формате JSON.
   - Поддержка **[RxJava](https://github.com/ReactiveX/RxJava)** для реактивного программирования, что позволяет обрабатывать асинхронные операции без использования колбэков.
   - Переход на **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** для упрощения асинхронного кода и повышения производительности.

### 2. **Локальное хранение данных**
   - Использование **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** для хранения настроек приложения и других локальных данных. DataStore предоставляет более современный и безопасный способ хранения данных по сравнению с SharedPreferences.
   - Работа с локальной базой данных **SQLite** через **[ORM Room](https://developer.android.com/training/data-storage/room)**. Room упрощает работу с базой данных, предоставляя абстракцию над SQLite.
   - Поддержка **[ksp (Kotlin Symbol Processing)](https://developer.android.com/jetpack/androidx/releases/room)** для генерации кода на этапе компиляции, что ускоряет работу с Room.
   - Использование **[Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** для удобства тестирования. Это позволяет заполнить базу данных начальными данными перед запуском приложения.

### 3. **Сетевое взаимодействие**
   - Работа с удаленным сервером через **[Retrofit2](https://square.github.io/retrofit/)** и **[OkHttp3](https://square.github.io/okhttp/)**. Retrofit используется для создания HTTP-запросов, а OkHttp — для обработки сетевых запросов и ответов.
   - Обработка данных в формате **JSON** с использованием **[Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)**.
   - Поддержка реактивного программирования через **[RxJava3](https://github.com/ReactiveX/RxJava)**. RxJava позволяет работать с асинхронными потоками данных и упрощает обработку событий.
   - Переход с **RxJava3** на **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** для асинхронных операций. Coroutines предоставляют более простой и читаемый способ работы с асинхронным кодом.

### 4. **Архитектура и паттерны**
   - Проект построен на архитектуре **MVVM** (Model-View-ViewModel). MVVM разделяет логику приложения на три компонента: Model (данные), View (интерфейс) и ViewModel (логика представления).
   - Использование следующих архитектурных паттернов и парадигм:
     - **Строитель (Builder)**: Паттерн для создания сложных объектов.
     - **Наблюдаемый (Observable)**: Паттерн для уведомления объектов об изменениях в данных.
     - **Наблюдатель (Observer)**: Паттерн для подписки на изменения данных.
     - **Адаптер (Adapter)**: Паттерн для преобразования интерфейса одного класса в интерфейс, ожидаемый клиентом.
     - **Внедрение зависимостей (Dependency Injection)**: Паттерн для управления зависимостями между компонентами.
     - **Архитектура с одной активностью (Single Activity Architecture)**: Подход, при котором все экраны приложения управляются одной Activity, что упрощает навигацию и управление состоянием.
     - **Модульная архитектура (Modular Architecture)**: Разделение приложения на независимые модули для упрощения разработки и тестирования.
     - **Реактивное программирование (Reactive Programming)**: Подход к программированию, основанный на асинхронных потоках данных.

### 5. **Настройки внешнего вида**
   - Возможность выбора языка интерфейса: **Русский**, **Английский** или **системный язык**.
   - Выбор темы приложения: **темная**, **светлая** или **системная тема**.
   - Включение и отключение **виброотклика** в приложении.

### 6. **Тестирование и отладка**
   - Использование **Prepopulate your Room database** для удобного тестирования локальной базы данных.
   - Поддержка модульной архитектуры для упрощения тестирования отдельных компонентов.
   - Использование **[JUnit](https://junit.org/junit5/)** для модульного тестирования и **[Espresso](https://developer.android.com/training/testing/espresso)** для UI-тестирования.

### 7. **Реактивное программирование**
   - Переход с **RxJava3** на **Coroutines** для более удобной работы с асинхронными операциями.
   - Использование suspend-функций для упрощения кода и повышения производительности.

---

## 📦 Зависимости проекта

Проект использует множество библиотек и инструментов для реализации различных функций. Ниже приведен список зависимостей, используемых в проекте:

### 1. **Основные зависимости**
   - **AndroidX Core KTX**: Утилиты для работы с Kotlin в Android.
     ```kotlin
     implementation(libs.androidx.core.ktx)
     ```
     [Документация](https://developer.android.com/jetpack/androidx/releases/core)
   - **AndroidX AppCompat**: Поддержка обратной совместимости для новых функций Android.
     ```kotlin
     implementation(libs.androidx.appcompat)
     ```
     [Документация](https://developer.android.com/jetpack/androidx/releases/appcompat)
   - **Material Design**: Компоненты Material Design для создания современного интерфейса.
     ```kotlin
     implementation(libs.material)
     ```
     [Документация](https://material.io/develop/android)
   - **AndroidX Activity**: Упрощение работы с Activity в Android.
     ```kotlin
     implementation(libs.androidx.activity)
     ```
     [Документация](https://developer.android.com/jetpack/androidx/releases/activity)
   - **AndroidX ConstraintLayout**: Гибкий и мощный макет для создания сложных интерфейсов.
     ```kotlin
     implementation(libs.androidx.constraintlayout)
     ```
     [Документация](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout)

### 2. **Локальное хранение данных**
   - **Room**: ORM для работы с SQLite.
     ```kotlin
     implementation(libs.androidx.room.runtime)
     implementation(libs.androidx.room.ktx)
     ksp(libs.androidx.room.compiler)
     ```
     [Документация](https://developer.android.com/training/data-storage/room)
   - **DataStore**: Хранение ключевых данных и настроек.
     ```kotlin
     implementation(libs.androidx.datastore.preferences)
     implementation(libs.androidx.datastore)
     implementation(libs.androidx.datastore.core)
     ```
     [Документация](https://developer.android.com/topic/libraries/architecture/datastore)

### 3. **Сетевое взаимодействие**
   - **Retrofit**: HTTP-клиент для работы с REST API.
     ```kotlin
     implementation(libs.retrofit)
     implementation(libs.retrofit2.kotlinx.serialization.converter)
     ```
     [Документация](https://square.github.io/retrofit/)
   - **OkHttp**: Клиент для работы с HTTP-запросами.
     ```kotlin
     implementation(platform(libs.okhttp.bom))
     implementation(libs.okhttp)
     implementation(libs.logging.interceptor)
     ```
     [Документация](https://square.github.io/okhttp/)
   - **RxJava**: Реактивное программирование для асинхронных операций.
     ```kotlin
     implementation(libs.rxjava)
     implementation(libs.rxandroid)
     implementation(libs.adapter.rxjava3)
     implementation(libs.rxkotlin)
     ```
     [Документация](https://github.com/ReactiveX/RxJava)

### 4. **Асинхронные операции**
   - **Coroutines**: Упрощение работы с асинхронными операциями.
     ```kotlin
     testImplementation(libs.kotlinx.coroutines.test)
     ```
     [Документация](https://kotlinlang.org/docs/coroutines-overview.html)

### 5. **UI и анимации**
   - **ViewParticleEmitter**: Анимации для создания эффектов.
     ```kotlin
     implementation(libs.confetti)
     ```
     [Документация](https://github.com/jinatonic/confetti)
   - **SplashScreen**: Поддержка экрана загрузки.
     ```kotlin
     implementation(libs.androidx.core.splashscreen)
     ```
     [Документация](https://developer.android.com/guide/topics/ui/splash-screen)
   - **SwipeRefreshLayout**: Поддержка "pull-to-refresh" для обновления данных.
     ```kotlin
     implementation(libs.androidx.swiperefreshlayout)
     ```
     [Документация](https://developer.android.com/reference/androidx/swiperefreshlayout/widget/SwipeRefreshLayout)

### 6. **Тестирование**
   - **JUnit**: Фреймворк для модульного тестирования.
     ```kotlin
     testImplementation(libs.junit)
     ```
     [Документация](https://junit.org/junit5/)
   - **AndroidX Test**: Инструменты для тестирования Android-приложений.
     ```kotlin
     androidTestImplementation(libs.androidx.junit)
     androidTestImplementation(libs.androidx.espresso.core)
     ```
     [Документация](https://developer.android.com/training/testing)

### 7. **Прочие зависимости**
   - **Protobuf**: Сериализация данных для хранения и передачи.
     ```kotlin
     implementation(libs.protobuf.javalite)
     ```
     [Документация](https://developers.google.com/protocol-buffers)
   - **Kotlin Serialization**: Сериализация данных в формате JSON.
     ```kotlin
     implementation(libs.kotlinx.serialization.json)
     ```
     [Документация](https://github.com/Kotlin/kotlinx.serialization)
   - **Desugaring**: Поддержка Java 8 API на старых версиях Android.
     ```kotlin
     coreLibraryDesugaring(libs.desugar.jdk.libs)
     ```
     [Документация](https://developer.android.com/studio/write/java8-support)

---

## 🚀 Стек используемых технологий

- **Kotlin**: Основной язык программирования.
- **Retrofit**: HTTP-клиент для работы с REST API.
- **RxJava**: Реактивное программирование.
- **Room**: ORM для работы с SQLite.
- **DataStore**: Хранение ключевых данных и настроек.
- **Coroutines**: Упрощение работы с асинхронными операциями.
- **Material Design**: Компоненты для создания современного интерфейса.

---

## 📄 Лицензия

Проект распространяется под лицензией **AGPL v3**. Подробнее см. [LICENSE](../LICENSE).

---

## 👥 Авторы

- **[Анатолий Спитченко](https://gitflic.ru/user/onotole)** - Android-разработчик и преподаватель курса.
- **[Лебедев Сергей Вячеславович](https://github.com/LebedevSergeyVach)** – Kotlin-разработчик.

---

#### [documentation](documentation.md) [вверх](#вверх)
