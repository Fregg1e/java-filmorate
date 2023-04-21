package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private IdGenerator idGenerator = new IdGenerator();
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            ValidationException validationException = new ValidationException("Такой фильм уже существует.");
            log.error("Произошло исключение! ", validationException);
            throw validationException;
        }
        film.setId(idGenerator.generateId());
        films.put(film.getId(), film);
        log.debug("Создан фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            ValidationException validationException = new ValidationException("Такого фильма не существует.");
            log.error("Произошло исключение! ", validationException);
            throw validationException;
        }
        films.put(film.getId(), film);
        log.debug("Фильм с id={} обновлен.", film.getId());
        return film;
    }
}
