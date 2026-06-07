# Схема базы данных
![Схема бд](db.png)

# Основные таблицы

* users — пользователи
* film — фильмы
* mpa — рейтинги MPA
* genre — жанры
* film_genre — связь фильмов с жанрами
* film_like — лайки пользователей фильмов
* friendship — дружба между пользователями

# Примеры запросов

### 1. Добавление фильма
```
INSERT INTO film (name, description, mpa_id, release_date, duration)
VALUES ('Film', 'Film', 1, '2010-11-31', 136);
``` 
### 2. Получение всех фильмов
```
SELECT f.id, f.name, f.description, f.release_date, f.duration, m.name AS mpa
FROM film f
JOIN mpa m ON f.mpa_id = m.id
ORDER BY f.id;
```
### 3. Добавление пользователя
```
INSERT INTO users (name, login, email, birthday)
VALUES ('Test', 'login', 'test@mail.com', '1990-01-01');
```

### 4. Добавление в друзья
```
INSERT INTO friendship (user1_id, user2_id, requester_id, status)
VALUES (1, 2, 1, 'PENDING');
```
