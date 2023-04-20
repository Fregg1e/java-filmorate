package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private final Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
