package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
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
    public void addLike(Long id, User user) {
        if (!films.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.get(id).addLike(user.getId());
    }

    @Override
    public void removeLike(Long id, User user) {
        if (!films.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого фильма не существует.");
        }
        films.get(id).removeLike(user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return getAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f1, Film f2) {
        Integer size1 = f1.getLikes().size();
        Integer size2 = f2.getLikes().size();
        return size1.compareTo(size2) * -1;
    }
}
