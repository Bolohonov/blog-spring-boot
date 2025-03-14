# Веб-приложение для блога на Spring Boot

Это веб-приложение представляет собой блог, реализованный с использованием **Spring Boot**. Сборка осуществляется с помощью **Gradle**. Код написан на **Java 21**.

## Описание проекта

### Основные функции:

- Лента постов с фильтрацией по тегам и пагинацией.
- Возможность добавления, редактирования и удаления постов.
- Отображение комментариев с возможностью их добавления, редактирования и удаления.
- Лайки для каждого поста с увеличением счётчика.
- Возможность добавления поста через форму с полями для названия, картинки, текста и тегов.
- Пагинация ленты постов (10, 20, 50 постов на страницу).

### Структура ленты постов:

- Превью каждого поста включает:
    - Название.
    - Картинку.
    - Краткий текст (первый абзац, не более трёх строк).
    - Количество комментариев.
    - Количество лайков.
    - Теги поста.
- Посты отображаются сверху вниз.
- Есть возможность фильтрации по тегу.

### Страница поста:

- Название поста.
- Картинка.
- Текст поста, разбитый на абзацы.
- Теги поста.
- Кнопки для удаления и редактирования поста.
- Кнопка для добавления комментария.
- Кнопка для лайков, при нажатии на которую увеличивается счётчик лайков.
- Список комментариев, с возможностью редактировать или удалять каждый комментарий.
- Добавление и редактирование комментариев происходит на той же странице.

## Технологии:

- **Backend**: Spring Boot.
- **Frontend**: HTML, JavaScript.
- **База данных**: PostgreSQL (персистентная база данных) или H2 (база данных в памяти).
- **Сборка**: Gradle.
- **Java**: 21.

## Установка и настройка

### Требования:

1. **JDK 21**: Для работы с Java 21.
2. **Gradle**: Для сборки проекта.
3**База данных**: PostgreSQL или H2.

### Установка зависимостей:

1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/ваш_репозиторий.git
    cd ваш_репозиторий
    ```

2. Установите зависимости:
    - Для **Gradle**:
        ```bash
        gradle build
        ```

### Запуск приложения:

1. Для запуска приложения:
    - Для **Gradle**:
        ```bash
        gradle run
        ```
    - Для создания FatJar **Gradle**:
        ```bash
        gradle shadowJar
        ```

2. Откройте браузер и перейдите по адресу:
    ```
    http://localhost:8080
    ```

## Структура проекта

- **controller** — содержит классы для обработки запросов.
- **model** — классы моделей, такие как посты, комментарии, пользователи.
- **repository** — интерфейсы для работы с базой данных.
- **service** — бизнес-логика приложения.
- **mapper** — мапперы.
- **utils** — SqlUtils.
