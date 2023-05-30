package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film getFilmById(Long id);

    void create(Film film);

    void update(Film film);

    void deleteById(Long id);

    void addLike(Long id, User user);

    void removeLike(Long id, User user);

    List<Film> getPopularFilms(Integer count);
}
