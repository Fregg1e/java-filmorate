package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getAll().values());
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
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

    public void addLike(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.addLike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        userStorage.getUserById(userId);
        filmStorage.removeLike(id, userId);
    }

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
