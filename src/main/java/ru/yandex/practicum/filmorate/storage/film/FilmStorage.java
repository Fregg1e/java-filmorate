package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Long, Film> getAll();
    Film getById(Long id);
    void create(Film film);
    void update(Film film);
    void delete(Long id);
}
