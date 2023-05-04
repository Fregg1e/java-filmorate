package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

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
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        return films.get(id);
    }

    @Override
    public void create(Film film) {
        if (films.containsValue(film)) {
            log.error("Произошло исключение!");
            throw new AlreadyExistException("Такой фильм уже существует.");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void deleteById(Long id) {
        films.remove(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (!films.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.get(id).addLike(userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        if (!films.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.get(id).removeLike(userId);
    }
}
