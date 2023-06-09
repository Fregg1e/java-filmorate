# Java-Filmorate

Java-Filmorate - это сервис для работы с фильмами и оценками пользователей,
который поможет выбрать фильм для просмотра

## Функционал приложения:

1. Создавать, обновлять и получать фильмы;
2. Получать список всех фильмов;
3. Ставить лайки фильмам;
4. Получать топ популярных фильмов;
5. Создавать, обновлять и получать пользователей;
6. Показывать историю просмотренных задач.
7. Получать список всех пользователей.
8. Добавлять пользователей в друзья.
9. Получать список общих пользователей.

## Диаграмма DB:

![DB diagram](DBDiagram.png)

### Описание диаграммы:

**USERS** - содержит данные о пользователях\
Таблица включает такие поля:

* первичный ключ `USER_ID` - идентификатор пользователя
* `EMAIL` - почта пользователя
* `LOGIN` - логин пользователя
* `USER_NAME` - имя пользователя
* `BIRTHDAY` - день рождение пользователя

**FRIENDS** - содержит данные о друзьях пользователя\
Таблица включает такие поля:

* первичный ключ №1 и внешний ключ `USER_ID` - идентификатор пользователя
* первичный ключ №2 и внешний ключ `FRIEND_ID` - идентификатор друга пользователя
* `STATUS` - статус дружбы
    - `FALSE` - ожидает подтверждения
    - `TRUE` - дружба подтверждена

**FILMS** - содержит данные о фильмах\
Таблица включает такие поля:

* первичный ключ `FILM_ID` - идентификатор фильма
* `FILM_NAME` - название фильма
* `DESCRIPTION` - описание фильма
* `RELEASE_DATE` - дата выхода фильма
* `DURATION` - продолжительность (в минутах) фильма
* внешний ключ `MPA_RATING_ID` - идентификатор возрастного рейтинга

**MPA_RATING** - содержит типы возрастных рейтингов Ассоциации кинокомпаний
Таблица включает такие поля:

* первичный ключ `MPA_RATING_ID` - идентификатор возрастного рейтинга
* `MPA_RATING_NAME` - название возрастного рейтинга:
    - `G`
    - `PG`
    - `PG-13`
    - `R`
    - `NC-17`

**GENRES** - содержит типы жанров
Таблица включает такие поля:

* первичный ключ `GENRE_ID` - идентификатор жанра
* `GENRE_NAME` - название жанра:
    - `Комедия`
    - `Драма`
    - `Мультфильм`
    - `Триллер`
    - `Документальный`
    - `Боевик`

**FILMS_GENRES** - содержит данные жанрах фильмов
Таблица включает такие поля:

* первичный ключ №1 и внешний ключ `FILM_ID` - идентификатор фильма
* первичный ключ №2 и внешний ключ `GENRE_ID` - идентификатор жанра

**LIKES** - содержит данные поставленных лайках фильмам
Таблица включает такие поля:

* первичный ключ №1 и внешний ключ `FILM_ID` - идентификатор фильма
* первичный ключ №2 и внешний ключ `USER_ID` - идентификатор пользователя

### Примеры основных запросов:

* Получение всех пользователей:

```
SELECT *
FROM USERS;
```

* Получение пользователя с id = 1:

```
SELECT *
FROM USERS
WHERE USER_ID = 1;
```

* Получение друзей пользователя с id = 1:

```
SELECT F.FRIEND_ID,
       U.EMAIL,
       U.LOGIN,
       U.USER_NAME,
       U.BIRTHDAY
FROM FRIENDS AS F
LEFT OUTER JOIN USERS AS U ON F.FRIEND_ID = U.USER_ID
WHERE F.USER_ID = 1 AND F.STATUS = TRUE;
```

* Получение общих друзей пользователей с id = 1 и id = 2:

```
SELECT F.FRIEND_ID,
       U.EMAIL,
       U.LOGIN,
       U.USER_NAME,
       U.BIRTHDAY
FROM FRIENDS AS F
LEFT OUTER JOIN USERS AS U ON F.FRIEND_ID = U.USER_ID
WHERE F.USER_ID = 1 
      AND F.STATUS = TRUE
      AND F.FRIEND_ID IN (
          SELECT FRIEND_ID
          FROM FRIENDS
          WHERE USER_ID = 2
      );
```

* Получение всех фильмов:

```
SELECT *
FROM FILMS;
```

* Получение фильма с id = 1:

```
SELECT *
FROM FILMS
WHERE FILM_ID = 1;
```

* Получение топ 10 популярных фильмов:

```
SELECT F.*,
       M.MPA_RATING_NAME
FROM FILMS AS F
INNER JOIN MPA_RATING AS M ON F.MPA_RATING_ID = M.MPA_RATING_ID
INNER JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID
GROUP BY F.FILM_ID
ORDER BY COUNT(L.USER_ID) DESC
LIMIT 10;
```