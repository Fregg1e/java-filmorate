package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final FilmStorage filmStorage;

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getAll().values());
    }

    public Film create(Film film) {
        film.setId(idGenerator.generateId());
        filmStorage.create(film);
        log.debug("Создан фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        filmStorage.update(film);
        log.debug("Фильм с id={} обновлен.", film.getId());
        return film;
    }
}
