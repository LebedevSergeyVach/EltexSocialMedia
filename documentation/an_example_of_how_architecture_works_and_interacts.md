# **Документация на примере работы слоев проекта, а также их взаимодействия**

## **Общая схема взаимодействия**

### 1. **MVI (Model-View-Intent)**
MVI — это архитектурный подход, который разделяет приложение на три основных компонента:
- **Model (Модель)**: Состояние приложения (например, `PostWallState`).
- **View (Представление)**: UI, который отображает состояние (например, `UserFragment`).
- **Intent (Намерение)**: Действия пользователя, которые изменяют состояние (например, `PostWallMessage`).

В проекте MVI реализован через следующие компоненты:
- **Store** (`PostWallStore`): Центральное хранилище, которое управляет состоянием и эффектами.
- **Reducer** (`PostWallReducer`): Обрабатывает сообщения и обновляет состояние.
- **EffectHandler** (`PostWallEffectHandler`): Выполняет побочные эффекты (например, сетевые запросы) и возвращает результаты в виде сообщений.

### 2. **MVVM (Model-View-ViewModel)**
MVVM — это архитектурный подход, который разделяет приложение на три слоя:
- **Model (Модель)**: Данные и бизнес-логика (например, `NetworkPostRepository`).
- **View (Представление)**: UI, который отображает данные (например, `UserFragment`).
- **ViewModel**: Промежуточный слой, который управляет данными и предоставляет их View.

В проекте MVVM реализован через:
- **ViewModel** (`PostWallViewModel`): Управляет состоянием и взаимодействует с хранилищем (Store).
- **Repository** (`NetworkPostRepository`): Отвечает за получение данных из сети или локальных источников.
- **View** (`UserFragment`): Отображает данные и передает пользовательские действия в ViewModel.

---

## **Как компоненты MVI и MVVM взаимодействуют между собой**

### 1. **Поток данных в MVI**

#### a. **Пользовательское действие (Intent)**
- Пользователь выполняет действие (например, лайкает пост или обновляет список).
- Это действие передается в `PostWallViewModel` через метод `accept(message: PostWallMessage)`.

#### b. **Обработка действия в ViewModel**
- `PostWallViewModel` передает сообщение (`PostWallMessage`) в `PostWallStore`.
- `PostWallStore` обрабатывает сообщение через `Reducer` и обновляет состояние (`PostWallState`).

#### c. **Выполнение побочных эффектов**
- Если действие требует выполнения побочного эффекта (например, сетевого запроса), `PostWallStore` передает эффект (`PostWallEffect`) в `PostWallEffectHandler`.
- `PostWallEffectHandler` выполняет эффект (например, загружает данные из `NetworkPostRepository`) и возвращает результат в виде сообщения (`PostWallMessage`).

#### d. **Обновление состояния**
- Результат эффекта передается обратно в `PostWallStore`, где `Reducer` обновляет состояние (`PostWallState`).
- Обновленное состояние передается в `PostWallViewModel` и далее в `UserFragment` через `StateFlow`.

#### e. **Отображение состояния в UI**
- `UserFragment` подписывается на `StateFlow` из `PostWallViewModel` и обновляет UI в зависимости от текущего состояния (`PostWallState`).

---

### 2. **Поток данных в MVVM**

#### a. **Получение данных из Repository**
- `NetworkPostRepository` отвечает за получение данных из сети (например, через API).
- Данные возвращаются в виде моделей (`PostData`), которые затем преобразуются в UI-модели (`PostUiModel`) через `PostUiModelMapper`.

#### b. **Передача данных в ViewModel**
- `PostWallViewModel` использует `PostWallStore` для управления состоянием и эффектами.
- Данные из `NetworkPostRepository` передаются в `PostWallEffectHandler`, который возвращает их в виде сообщений (`PostWallMessage`).

#### c. **Отображение данных в View**
- `UserFragment` подписывается на `StateFlow` из `PostWallViewModel` и отображает данные (например, список постов) через адаптер (`PostAdapter`).

---

### 3. **Взаимодействие между MVI и MVVM**

#### a. **ViewModel как мост между MVI и MVVM**
- `PostWallViewModel` объединяет MVI и MVVM:
    - В MVI: Управляет состоянием через `PostWallStore` и обрабатывает сообщения (`PostWallMessage`).
    - В MVVM: Предоставляет данные (`PostWallState`) для отображения в `UserFragment`.

#### b. **Repository как источник данных**
- `NetworkPostRepository` предоставляет данные для MVI через `PostWallEffectHandler`.
- Это позволяет отделить бизнес-логику (MVI) от получения данных (MVVM).

#### c. **View как потребитель состояния**
- `UserFragment` подписывается на `StateFlow` из `PostWallViewModel` и отображает данные.
- Пользовательские действия (например, лайк или удаление) передаются в `PostWallViewModel` через метод `accept(message: PostWallMessage)`.

---

## **Пример работы сценария**

### 1. **Загрузка постов**
1. Пользователь открывает `UserFragment`.
2. `UserFragment` отправляет сообщение `PostWallMessage.Refresh` в `PostWallViewModel`.
3. `PostWallViewModel` передает сообщение в `PostWallStore`.
4. `PostWallStore` создает эффект `PostWallEffect.LoadInitialPage` и передает его в `PostWallEffectHandler`.
5. `PostWallEffectHandler` выполняет запрос к `NetworkPostRepository` для загрузки данных.
6. Данные возвращаются в виде сообщения `PostWallMessage.InitialLoaded`.
7. `PostWallStore` обновляет состояние (`PostWallState`) и передает его в `PostWallViewModel`.
8. `UserFragment` получает обновленное состояние и отображает список постов.

### 2. **Лайк поста**
1. Пользователь нажимает кнопку лайка на посте.
2. `UserFragment` отправляет сообщение `PostWallMessage.Like(post)` в `PostWallViewModel`.
3. `PostWallViewModel` передает сообщение в `PostWallStore`.
4. `PostWallStore` создает эффект `PostWallEffect.Like(post)` и передает его в `PostWallEffectHandler`.
5. `PostWallEffectHandler` выполняет запрос к `NetworkPostRepository` для обновления лайка.
6. Результат возвращается в виде сообщения `PostWallMessage.LikeResult`.
7. `PostWallStore` обновляет состояние (`PostWallState`) и передает его в `PostWallViewModel`.
8. `UserFragment` получает обновленное состояние и обновляет UI.

---

## **Преимущества такого подхода**

1. **Четкое разделение ответственности**:
    - MVI управляет состоянием и эффектами.
    - MVVM управляет данными и их отображением.

2. **Простота тестирования**:
    - Каждый компонент (Store, Reducer, EffectHandler) можно тестировать отдельно.
    - ViewModel и Repository также легко тестируются.

3. **Прозрачность потока данных**:
    - Все действия и изменения состояния четко отслеживаются через сообщения и эффекты.

4. **Гибкость**:
    - MVI позволяет легко добавлять новые функции и изменять бизнес-логику без изменения UI.

---

## **1. Что такое эффекты?**

**Эффекты (Effects)** — это побочные действия, которые выполняются в ответ на пользовательские действия или изменения состояния. Они не изменяют состояние напрямую, но могут вызывать асинхронные операции, такие как:
- Сетевые запросы (например, загрузка постов с сервера).
- Работа с базой данных (например, сохранение или удаление данных).
- Взаимодействие с системой (например, вибрация или уведомления).

В проекте эффекты представлены через `PostWallEffect`:
- `LoadInitialPage`: Загрузка начальной страницы постов.
- `LoadNextPage`: Загрузка следующей страницы постов.
- `Like`: Лайк поста.
- `Delete`: Удаление поста.

Эффекты обрабатываются в `PostWallEffectHandler`, который выполняет необходимые действия (например, сетевые запросы через `NetworkPostRepository`) и возвращает результаты в виде сообщений (`PostWallMessage`).

---

## **2. Где хранятся данные (например, список постов), после того, как они загрузились с сервера?**

Данные (например, список постов) хранятся в **состоянии (State)**. В проекте состояние представлено классом `PostWallState`:
- **`posts: List<PostUiModel>`**: Список постов, который отображается в UI.
- **`statusPost: PostStatus`**: Текущий статус загрузки (например, загрузка, ошибка, успех).
- **`singleError: Throwable?`**: Ошибка, если она произошла.

После загрузки данных с сервера:
1. Данные преобразуются из `PostData` (модель данных из API) в `PostUiModel` (UI-модель) через `PostUiModelMapper`.
2. Обновленное состояние (`PostWallState`) сохраняется в `PostWallStore`.
3. `PostWallStore` передает обновленное состояние в `PostWallViewModel` через `StateFlow`.
4. `UserFragment` подписывается на `StateFlow` и отображает данные в UI.

Таким образом, данные хранятся в состоянии (`PostWallState`), которое управляется через `PostWallStore`.

---

## **Схема взаимодействия**

Для наглядности я создал схему, которая показывает, как компоненты MVI и MVVM взаимодействуют между собой.

### **Общая схема взаимодействия**

```plaintext
+-------------------+       +-------------------+       +-------------------+
|      View         |       |    ViewModel      |       |      Store        |
|  (UserFragment)   | <---> |(PostWallViewModel)| <---> | (PostWallStore)   |
+-------------------+       +-------------------+       +-------------------+
        |                           |                           |
        |                           |                           |
        v                           v                           v
+-------------------+       +-------------------+       +-------------------+
|   UI Components   |       |     StateFlow     |       |     Reducer       |
|  (PostAdapter)    |       |  (PostWallState)  |       | (PostWallReducer) |
+-------------------+       +-------------------+       +-------------------+
        |                           |                           |
        |                           |                           |
        v                           v                           v
+-------------------+       +-------------------+       +-------------------+
|  User Actions     |       |     Effects       |       |  EffectHandler    |
|  (Like, Delete)   |       | (PostWallEffect)  |       |(PostWallEffectHandler)
+-------------------+       +-------------------+       +-------------------+
        |                           |                           |
        |                           |                           |
        v                           v                           v
+-------------------+       +-------------------+       +-------------------+
|   Repository      |       |     API Calls     |       |  NetworkPostRepo  |
| (NetworkPostRepo) | <---> | (getLatestPosts)  | <---> | (PostsApi)        |
+-------------------+       +-------------------+       +-------------------+
```

---

### **Пояснение к схеме**

1. **View (UserFragment)**:
    - Отображает данные (список постов) через `PostAdapter`.
    - Передает пользовательские действия (например, лайк или удаление) в `PostWallViewModel`.

2. **ViewModel (PostWallViewModel)**:
    - Управляет состоянием через `PostWallStore`.
    - Передает сообщения (`PostWallMessage`) в `PostWallStore` для обработки.

3. **Store (PostWallStore)**:
    - Хранит текущее состояние (`PostWallState`).
    - Обрабатывает сообщения через `Reducer` и обновляет состояние.
    - Передает эффекты (`PostWallEffect`) в `PostWallEffectHandler` для выполнения побочных действий.

4. **EffectHandler (PostWallEffectHandler)**:
    - Выполняет побочные эффекты (например, сетевые запросы) через `NetworkPostRepository`.
    - Возвращает результаты в виде сообщений (`PostWallMessage`).

5. **Repository (NetworkPostRepository)**:
    - Получает данные из сети через API (`PostsApi`).
    - Преобразует данные из `PostData` в `PostUiModel` через `PostUiModelMapper`.

6. **UI Components (PostAdapter)**:
    - Отображает данные (список постов) в `RecyclerView`.
    - Обрабатывает пользовательские действия (например, клики на лайк или удаление).

---

### **Поток данных**

1. **Пользовательское действие**:
    - Пользователь выполняет действие (например, лайкает пост).
    - Действие передается в `PostWallViewModel` через метод `accept(message: PostWallMessage)`.

2. **Обработка действия**:
    - `PostWallViewModel` передает сообщение в `PostWallStore`.
    - `PostWallStore` обрабатывает сообщение через `Reducer` и обновляет состояние.

3. **Выполнение эффекта**:
    - Если действие требует выполнения эффекта (например, сетевого запроса), `PostWallStore` передает эффект в `PostWallEffectHandler`.
    - `PostWallEffectHandler` выполняет эффект (например, загружает данные через `NetworkPostRepository`).

4. **Обновление состояния**:
    - Результат эффекта возвращается в виде сообщения (`PostWallMessage`).
    - `PostWallStore` обновляет состояние и передает его в `PostWallViewModel`.

5. **Отображение данных**:
    - `UserFragment` подписывается на `StateFlow` из `PostWallViewModel` и обновляет UI.

---

### **Пример сценария**

#### **Загрузка постов**
1. Пользователь открывает `UserFragment`.
2. `UserFragment` отправляет сообщение `PostWallMessage.Refresh` в `PostWallViewModel`.
3. `PostWallViewModel` передает сообщение в `PostWallStore`.
4. `PostWallStore` создает эффект `PostWallEffect.LoadInitialPage` и передает его в `PostWallEffectHandler`.
5. `PostWallEffectHandler` выполняет запрос к `NetworkPostRepository` для загрузки данных.
6. Данные возвращаются в виде сообщения `PostWallMessage.InitialLoaded`.
7. `PostWallStore` обновляет состояние (`PostWallState`) и передает его в `PostWallViewModel`.
8. `UserFragment` получает обновленное состояние и отображает список постов.

---

### **Преимущества схемы**

1. **Четкое разделение ответственности**:
    - Каждый компонент выполняет свою задачу (View, ViewModel, Store, EffectHandler, Repository).

2. **Прозрачность потока данных**:
    - Все действия и изменения состояния четко отслеживаются через сообщения и эффекты.

3. **Гибкость**:
    - Легко добавлять новые функции или изменять бизнес-логику без изменения UI.
