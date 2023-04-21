package ru.yandex.practicum.filmorate.validator.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.validator.film.util.FilmGenerator.getFilm;
import static ru.yandex.practicum.filmorate.validator.util.ErrorChecker.hasErrorMessage;

public class FilmValidationTest {
    @Test
    @DisplayName("1) Проверка валидации с null параметрами")
    public void filmValidationWithAllNullParamTest() {
        Film film = getFilm(null, null, null, null, null);
        assertTrue(hasErrorMessage(film, "Название не может быть пустым."));
        assertTrue(hasErrorMessage(film, "Дата релиза не может быть раньше 1895-12-28 или равной null."));
    }

    @ParameterizedTest(name = "{index}. Проверка невалидности названия \"{arguments}\"")
    @ValueSource(strings = {"", " ", "  ", "   ", "    ", "     "})
    @DisplayName("2) Проверка невалидности названия с пустыми значениями")
    public void filmNameValidationTest(String name) {
        Film film = getFilm(1L, name, "description",
                LocalDate.now().minusDays(2), 120);
        assertTrue(hasErrorMessage(film, "Название не может быть пустым."));
    }

    @Test
    @DisplayName("3) Проверка невалидности описания")
    public void filmDescriptionValidationTest() {
        Film film = getFilm(1L, "name", new String(new char[199]),
                LocalDate.now().minusDays(2), 120);
        assertFalse(hasErrorMessage(film, "Описание не может быть больше 200 символов."));

        film = getFilm(1L, "name", new String(new char[200]),
                LocalDate.now().minusDays(2), 120);
        assertFalse(hasErrorMessage(film, "Описание не может быть больше 200 символов."));

        film = getFilm(1L, "name", new String(new char[201]),
                LocalDate.now().minusDays(2), 120);
        assertTrue(hasErrorMessage(film, "Описание не может быть больше 200 символов."));
    }

    @Test
    @DisplayName("4) Проверка невалидности даты релиза")
    public void filmReleaseDateValidationTest() {
        LocalDate date = LocalDate.parse("1895-12-28");

        Film film = getFilm(1L, "name", "description",
                date.plusDays(1), 120);
        assertFalse(hasErrorMessage(film, "Дата релиза не может быть раньше 1895-12-28 или равной null."));

        film = getFilm(1L, "name", "description",
                date, 120);
        assertFalse(hasErrorMessage(film, "Дата релиза не может быть раньше 1895-12-28 или равной null."));

        film = getFilm(1L, "name", "description",
                date.minusDays(1), 120);
        assertTrue(hasErrorMessage(film, "Дата релиза не может быть раньше 1895-12-28 или равной null."));
    }

    @Test
    @DisplayName("5) Проверка невалидности продолжительности фильма")
    public void filmDurationValidationTest() {
        Film film = getFilm(1L, "name", "description",
                LocalDate.now().minusDays(2), 1);
        assertFalse(hasErrorMessage(film, "Продолжительность должна быть положительной."));

        film = getFilm(1L, "name", "description",
                LocalDate.now().minusDays(2), 0);
        assertTrue(hasErrorMessage(film, "Продолжительность должна быть положительной."));

        film = getFilm(1L, "name", "description",
                LocalDate.now().minusDays(2), -1);
        assertTrue(hasErrorMessage(film, "Продолжительность должна быть положительной."));
    }
}
