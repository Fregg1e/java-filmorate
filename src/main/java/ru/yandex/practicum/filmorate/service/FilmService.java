package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {
    private final IdGenerator idGenerator;
    private final Map<Long, Film> films;

    public FilmService() {
        idGenerator = new IdGenerator();
        films = new HashMap<>();
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        if (films.containsValue(film)) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такой фильм уже существует.");
        }
        film.setId(idGenerator.generateId());
        films.put(film.getId(), film);
        log.debug("Создан фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такого фильма не существует.");
        }
        films.put(film.getId(), film);
        log.debug("Фильм с id={} обновлен.", film.getId());
        return film;
    }
}
