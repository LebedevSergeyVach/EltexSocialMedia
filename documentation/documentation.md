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
- Поддержка **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** для асинхронных операций, что упрощает код и повышает производительность.

### 2. **Локальное хранение данных**
- Использование **[Jetpack Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)** для хранения настроек приложения и других локальных данных. DataStore предоставляет более современный и безопасный способ хранения данных по сравнению с SharedPreferences.
- Работа с локальной базой данных **SQLite** через **[ORM Room](https://developer.android.com/training/data-storage/room)**. Room упрощает работу с базой данных, предоставляя абстракцию над SQLite.
- Поддержка **[ksp (Kotlin Symbol Processing)](https://developer.android.com/jetpack/androidx/releases/room)** для генерации кода на этапе компиляции, что ускоряет работу с Room.
- Использование **[Prepopulate your Room database](https://developer.android.com/training/data-storage/room/prepopulate)** для удобства тестирования. Это позволяет заполнить базу данных начальными данными перед запуском приложения.

### 3. **Сетевое взаимодействие**
- Работа с удаленным сервером через **[Retrofit2](https://square.github.io/retrofit/)** и **[OkHttp3](https://square.github.io/okhttp/)**. Retrofit используется для создания HTTP-запросов, а OkHttp — для обработки сетевых запросов и ответов.
- Обработка данных в формате **JSON** с использованием **[Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)**.
- Поддержка асинхронных операций через **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)**. Coroutines предоставляют более простой и читаемый способ работы с асинхронным кодом.

### 4. **Архитектура и паттерны**
- Проект построен на архитектуре **MVI** (Model-View-Intent), которая обеспечивает четкое разделение ответственности между компонентами приложения. Основные элементы MVI:
    - **Model**: Представляет собой состояние приложения. В нашем случае это данные о постах, событиях и текущем статусе загрузки.
    - **View**: Отвечает за отображение данных пользователю. В проекте это фрагменты и активити, которые отображают список постов и другие элементы интерфейса.
    - **Intent**: Представляет собой намерения пользователя, такие как загрузка постов, лайки, удаление и т.д. Эти намерения передаются в **ViewModel**, где обрабатываются и преобразуются в изменения состояния.

- Проект построен на архитектуре **MVVM** (Model-View-ViewModel). MVVM разделяет логику приложения на три компонента: Model (данные), View (интерфейс) и ViewModel (логика представления).
    - **Model**: Отвечает за данные и бизнес-логику приложения. Включает в себя работу с локальной базой данных (Room), сетевыми запросами (Retrofit) и другими источниками данных.
    - **View**: Отвечает за отображение данных пользователю. Это фрагменты и активити, которые взаимодействуют с пользователем и отображают UI.
    - **ViewModel**: Связывает Model и View. ViewModel получает данные от Model и преобразует их в формат, который может быть отображен в View. Также ViewModel обрабатывает пользовательские действия и передает их в Model.

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

### 7. **Отображение скелетонов**
- Использование библиотеки **[SkeletonLayout](https://github.com/Faltenreich/SkeletonLayout)** для отображения анимированных заглушек (скелетонов) во время загрузки данных.
- Скелетоны помогают улучшить пользовательский опыт, показывая, что данные загружаются, и предотвращая "мерцание" интерфейса.
- Библиотека поддерживает настройку цвета анимации, фона и других параметров для создания адаптивных скелетонов.

---

## 🏗️ Архитектуры в проекте: MVVM и MVI

В проекте используются две архитектуры: **MVVM** (Model-View-ViewModel) и **MVI** (Model-View-Intent). Обе архитектуры помогают организовать код, разделяя ответственность между компонентами приложения, но они имеют свои особенности и применяются в разных частях проекта.

---

### Архитектура MVVM (Model-View-ViewModel)

В проекте также используется архитектура **MVVM** (Model-View-ViewModel), которая обеспечивает четкое разделение ответственности между компонентами приложения. Основные элементы MVVM:

- **Model**: Отвечает за данные и бизнес-логику приложения. Включает в себя работу с локальной базой данных (Room), сетевыми запросами (Retrofit) и другими источниками данных.
- **View**: Отвечает за отображение данных пользователю. Это фрагменты и активити, которые взаимодействуют с пользователем и отображают UI.
- **ViewModel**: Связывает Model и View. ViewModel получает данные от Model и преобразует их в формат, который может быть отображен в View. Также ViewModel обрабатывает пользовательские действия и передает их в Model.

#### Основные компоненты MVVM в проекте:
1. **PostRepository**: Репозиторий, который управляет данными о постах. Он взаимодействует с локальной базой данных (Room) и сетевым API (Retrofit) для получения и обновления данных.
2. **PostViewModel**: ViewModel, которая связывает View с репозиторием. Она получает данные от репозитория и предоставляет их View через LiveData или StateFlow.
3. **View**: Фрагменты и активити, которые отображают данные пользователю и обрабатывают пользовательские действия, такие как лайки, удаление и обновление постов.

#### Работа с сервером:
- **Загрузка постов**: Посты загружаются с сервера порциями (пагинация). ViewModel запрашивает данные у репозитория, который, в свою очередь, взаимодействует с Retrofit для выполнения сетевых запросов.
- **Лайки и удаление**: Пользователь может лайкать посты и удалять их. Эти действия передаются в ViewModel, которая обновляет данные через репозиторий и уведомляет View об изменениях.
- **Обработка ошибок**: В случае ошибок при загрузке данных или выполнении действий, ViewModel уведомляет View, и пользователю показываются соответствующие уведомления.

#### Используемые технологии:
- **Retrofit2**: Для выполнения сетевых запросов к серверу.
- **Coroutines**: Для асинхронной обработки запросов и обновления состояния.
- **Jetpack DataStore**: Для хранения локальных настроек, таких как выбор темы и языка.
- **Room**: Для работы с локальной базой данных SQLite.

#### Преимущества MVVM:
- **Разделение ответственности**: Каждый компонент выполняет свою задачу, что упрощает поддержку и тестирование кода.
- **Жизненный цикл ViewModel**: ViewModel сохраняет данные при изменении конфигурации (например, при повороте экрана), что улучшает пользовательский опыт.
- **Поддержка DataBinding**: MVVM хорошо интегрируется с DataBinding, что позволяет автоматически обновлять UI при изменении данных.

---

### Архитектура MVI (Model-View-Intent)

Проект также построен на архитектуре **MVI** (Model-View-Intent), которая обеспечивает четкое разделение ответственности между компонентами приложения. Основные элементы MVI:

- **Model**: Представляет собой состояние приложения. В нашем случае это данные о постах, событиях и текущем статусе загрузки.
- **View**: Отвечает за отображение данных пользователю. В проекте это фрагменты и активити, которые отображают список постов и другие элементы интерфейса.
- **Intent**: Представляет собой намерения пользователя, такие как загрузка постов, лайки, удаление и т.д. Эти намерения передаются в **ViewModel**, где обрабатываются и преобразуются в изменения состояния.

#### Основные компоненты MVI в проекте:
1. **PostStore**: Хранилище, которое управляет состоянием постов и эффектами. Оно обрабатывает сообщения (Intents) и обновляет состояние (Model).
2. **PostEffectHandler**: Обработчик эффектов, который выполняет побочные эффекты, такие как загрузка данных с сервера, лайки и удаление постов.
3. **PostReducer**: Редьюсер, который обрабатывает сообщения и обновляет состояние приложения. Он определяет, как состояние изменяется в ответ на действия пользователя.
4. **PostViewModel**: ViewModel, которая связывает View с хранилищем (Store). Она принимает сообщения от пользователя и передает их в хранилище для обработки.

#### Работа с сервером:
- **Загрузка постов**: Посты загружаются с сервера порциями (пагинация). Когда пользователь приближается к концу списка, автоматически загружается следующая порция данных.
- **Лайки и удаление**: Пользователь может лайкать посты и удалять их. Эти действия отправляются на сервер, и состояние приложения обновляется в реальном времени.
- **Обработка ошибок**: В случае ошибок при загрузке данных или выполнении действий, пользователю показываются соответствующие уведомления, а состояние приложения корректно обновляется.

#### Используемые технологии:
- **Retrofit2**: Для выполнения сетевых запросов к серверу.
- **Coroutines**: Для асинхронной обработки запросов и обновления состояния.
- **Jetpack DataStore**: Для хранения локальных настроек, таких как выбор темы и языка.
- **Room**: Для работы с локальной базой данных SQLite.

#### Преимущества MVI:
- **Предсказуемость**: Состояние приложения всегда ясно и предсказуемо, так как все изменения происходят через редьюсер.
- **Тестируемость**: Компоненты MVI легко тестируются, так как они изолированы и имеют четкие обязанности.
- **Масштабируемость**: Архитектура MVI позволяет легко добавлять новые функции и изменять существующие без нарушения работы приложения.

---

### Сравнение MVVM и MVI

| Характеристика    | MVVM                                                                       | MVI                                                                      |
|-------------------|----------------------------------------------------------------------------|--------------------------------------------------------------------------|
| **Состояние**     | Состояние может быть распределено между ViewModel и View.                  | Состояние централизовано и неизменно.                                    |
| **Поток данных**  | Двусторонний: View может обновлять ViewModel, и наоборот.                  | Односторонний: View отправляет Intents, а ViewModel обновляет состояние. |
| **Тестируемость** | Легко тестировать ViewModel, но сложнее тестировать взаимодействие с View. | Легко тестировать все компоненты, так как они изолированы.               |
| **Сложность**     | Проще в реализации для небольших проектов.                                 | Более сложная архитектура, но лучше подходит для больших проектов.       |
| **Поддержка**     | Широко используется в Android-разработке.                                  | Менее распространен, но набирает популярность.                           |

---

### Когда использовать MVVM и MVI?

- **MVVM** лучше подходит для небольших проектов или экранов с простой логикой. Она проще в реализации и поддерживается большинством Android-разработчиков.
- **MVI** лучше подходит для сложных проектов, где важно управление состоянием и предсказуемость. MVI помогает избежать багов, связанных с изменением состояния, и упрощает тестирование.

---

## 📦 Зависимости проекта

Проект использует множество библиотек и инструментов для реализации различных функций. Ниже приведен список зависимостей, используемых в проекте:

### 1. **Основные зависимости**
- **AndroidX Core KTX**: Утилиты для работы с Kotlin в Android.
    ```kotlin
        implementation(libs.androidx.core.ktx)
    ```
  [**Документация**](https://developer.android.com/jetpack/androidx/releases/core)

- **AndroidX AppCompat**: Поддержка обратной совместимости для новых функций Android.
    ```kotlin
        implementation(libs.androidx.appcompat)
    ```
  [**Документация**](https://developer.android.com/jetpack/androidx/releases/appcompat)

- **Material Design**: Компоненты Material Design для создания современного интерфейса.
    ```kotlin
        implementation(libs.material)
    ```
  [**Документация**](https://material.io/develop/android)

- **AndroidX Activity**: Упрощение работы с Activity в Android.
    ```kotlin
        implementation(libs.androidx.activity)
    ```
  [**Документация**](https://developer.android.com/jetpack/androidx/releases/activity)

- **AndroidX ConstraintLayout**: Гибкий и мощный макет для создания сложных интерфейсов.
    ```kotlin
        implementation(libs.androidx.constraintlayout)
    ```
  [**Документация**](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout)

### 2. **Локальное хранение данных**
- **Room**: ORM для работы с SQLite.
    ```kotlin
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.androidx.room.compiler)
    ```
  [**Документация**](https://developer.android.com/training/data-storage/room)

- **DataStore**: Хранение ключевых данных и настроек.
    ```kotlin
        implementation(libs.androidx.datastore.preferences)
        implementation(libs.androidx.datastore)
        implementation(libs.androidx.datastore.core)
    ```
  [**Документация**](https://developer.android.com/topic/libraries/architecture/datastore)

### 3. **Сетевое взаимодействие**
- **Retrofit**: HTTP-клиент для работы с REST API.
    ```kotlin
        implementation(libs.retrofit)
        implementation(libs.retrofit2.kotlinx.serialization.converter)
    ```
  [**Документация**](https://square.github.io/retrofit/)

- **OkHttp**: Клиент для работы с HTTP-запросами.
    ```kotlin
        implementation(platform(libs.okhttp.bom))
        implementation(libs.okhttp)
        implementation(libs.logging.interceptor)
    ```
  [**Документация**](https://square.github.io/okhttp/)

### 4. **Асинхронные операции**
- **Coroutines**: Упрощение работы с асинхронными операциями.
    ```kotlin
        testImplementation(libs.kotlinx.coroutines.test)
    ```
  [**Документация**](https://kotlinlang.org/docs/coroutines-overview.html)

### 5. **UI и анимации**
- **ViewParticleEmitter**: Анимации для создания эффектов.
    ```kotlin
        implementation(libs.confetti)
    ```
  [**Документация**](https://github.com/jinatonic/confetti)

- **SplashScreen**: Поддержка экрана загрузки.
    ```kotlin
        implementation(libs.androidx.core.splashscreen)
    ```
  [**Документация**](https://developer.android.com/guide/topics/ui/splash-screen)

- **SwipeRefreshLayout**: Поддержка "pull-to-refresh" для обновления данных.
    ```kotlin
        implementation(libs.androidx.swiperefreshlayout)
    ```
  [**Документация**](https://developer.android.com/reference/androidx/swiperefreshlayout/widget/SwipeRefreshLayout)

- **SkeletonLayout**: Библиотека для отображения скелетонов (заглушек) во время загрузки данных.
    ```kotlin
        implementation(libs.skeletonlayout)
    ```
  [**Документация**](https://github.com/Faltenreich/SkeletonLayout)

- **Glide**: Быстрая и эффективная платформа для управления мультимедиа и загрузки изображений с открытым исходным кодом для Android.
    ```kotlin
        implementation(libs.glide)
    ```
  [**Документация**](https://github.com/bumptech/glide)

### 6. **Тестирование**
- **JUnit**: Фреймворк для модульного тестирования.
    ```kotlin
        testImplementation(libs.junit)
    ```
  [**Документация**](https://junit.org/junit5/)

- **AndroidX Test**: Инструменты для тестирования Android-приложений.
    ```kotlin
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    ```
  [**Документация**](https://developer.android.com/training/testing)

### 7. **Прочие зависимости**
- **Protobuf**: Сериализация данных для хранения и передачи.
    ```kotlin
        implementation(libs.protobuf.javalite)
    ```
  [**Документация**](https://developers.google.com/protocol-buffers)

- **Kotlin Serialization**: Сериализация данных в формате JSON.
    ```kotlin
        implementation(libs.kotlinx.serialization.json)
    ```
  [**Документация**](https://github.com/Kotlin/kotlinx.serialization)

- **Desugaring**: Поддержка Java 8 API на старых версиях Android.
    ```kotlin
        coreLibraryDesugaring(libs.desugar.jdk.libs)
    ```
  [**Документация**](https://developer.android.com/studio/write/java8-support)

- **Dagger2-Hill**: Внедрение зависимостей с помощью Hilt — это библиотека внедрения зависимостей для Android, которая упрощает шаблонное внедрение зависимостей вручную в ваш проект.
    ```kotlin
        implementation(libs.hilt.android)
        ksp(libs.hilt.android.compiler)
    ```
  [**Документация**](https://developer.android.com/training/dependency-injection/hilt-android)
---

## 🚀 Стек используемых технологий

- **Kotlin**: Основной язык программирования.
- **Retrofit**: HTTP-клиент для работы с REST API.
- **Room**: ORM для работы с SQLite.
- **DataStore**: Хранение ключевых данных и настроек.
- **Coroutines**: Упрощение работы с асинхронными операциями.
- **Material Design**: Компоненты для создания современного интерфейса.
- **SkeletonLayout**: Библиотека для отображения скелетонов во время загрузки данных.
- **Glide**: Библиотека для управления мультимедиа и загрузки изображений с открытым исходным кодом для Android.

<details close="open">
    <summary><h3>🚀 Стек используемых технологий (не отображается на GitHub)</h3></summary>
    <div style="display: flex; flex-wrap: wrap; gap: 16px;">
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://kotlinlang.org/" target="_blank">
                <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" height="40" alt="kotlin logo" />
            </a>
            <p>Kotlin</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://square.github.io/retrofit/" target="_blank">
                <img src="https://uploads-ssl.webflow.com/60996f3af06ca2ff488a7bfb/60a269bf446a85794a4d3b6b_Retrofit.jpg" height="40" alt="retrofit logo" />
            </a>
            <p>Retrofit</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://reactivex.io/" target="_blank">
                <img src="https://static.cdnlogo.com/logos/r/27/reactivex.svg" height="40" alt="rxjava logo" />
            </a>
            <p>RxJava</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://github.com/Faltenreich/SkeletonLayout" target="_blank">
                <img src="https://raw.githubusercontent.com/Faltenreich/SkeletonLayout/refs/heads/develop/images/android.png" height="40" alt="SkeletonLayout logo" />
            </a>
            <p>SkeletonLayout</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://gradle.org/" target="_blank">
                <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" height="40" alt="gradle logo" />
            </a>
            <p>Gradle</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://developer.android.com/jetpack" target="_blank">
                <img src="https://services.google.com/fh/files/emails/android_dev_newsletter_feb_image3.png" height="40" alt="androidx jetpack logo" />
            </a>
            <p>AndroidX Jetpack</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://www.android.com/" target="_blank">
                <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-plain-wordmark.svg" height="40" alt="android logo" />
            </a>
            <p>Android</p>
        </div>
        <div style="border: 1px solid #ddd; padding: 16px; border-radius: 8px; text-align: center;">
            <a href="https://github.com/bumptech/glide" target="_blank">
                <img src="https://s3.amazonaws.com/playstore/images/60bb08c2fc6d0bddb91e0e3553dcdb48" height="40" alt="Glide logo" />
            </a>
            <p>Android</p>
        </div>
    </div>
</details>

---

## 📄 Лицензия

Проект распространяется под лицензией **AGPL v3**. Подробнее см. [LICENSE](../LICENSE).

---

#### [documentation](documentation.md) [вверх](#вверх)
