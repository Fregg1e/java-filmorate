package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface FilmStorage {
    Map<Long, Film> getAll();

    Film getFilmById(Long id);

    void create(Film film);

    void update(Film film);

    void deleteById(Long id);

    void addLike(Long id, User user);

    void removeLike(Long id, User user);
}
