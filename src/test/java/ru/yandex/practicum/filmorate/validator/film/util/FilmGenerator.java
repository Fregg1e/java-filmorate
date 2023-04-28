package ru.yandex.practicum.filmorate.validator.film.util;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmGenerator {
    public static Film getFilm(Long id, String name, String description,
                                    LocalDate releaseDate, Integer duration) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }
}
