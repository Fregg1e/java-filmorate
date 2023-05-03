package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public Map<Long, Film> getAll() {
        return films;
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

    @Override
    public void create(Film film) {
        if (films.containsValue(film)) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такой фильм уже существует.");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void delete(Long id) {
        films.remove(id);
    }
}
