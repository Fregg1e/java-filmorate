package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return null;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        return null;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        
    }
}
