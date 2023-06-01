package ru.yandex.practicum.filmorate.validator.film.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

public class FilmGenerator {
    public static Film getFilm(Long id, String name, String description,
            LocalDate releaseDate, Integer duration) {
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
    }

    public static Film getFilm(Long id, String name, String description,
            LocalDate releaseDate, Integer duration, MPA mpa) {
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .build();
    }
}
