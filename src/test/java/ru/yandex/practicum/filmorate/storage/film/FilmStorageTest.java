package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;
import ru.yandex.practicum.filmorate.validator.film.util.FilmGenerator;
import ru.yandex.practicum.filmorate.validator.user.util.UserGenerator;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    @Order(1)
    @DisplayName("1) Проверка создания фильма")
    void createFilmTest() {
        Film film = FilmGenerator.getFilm(1L, "name", "description test",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        filmDbStorage.create(film);
        film = filmDbStorage.getFilmById(1L);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    @DisplayName("2) Проверка обновления фильма")
    void updateFilmTest() {
        Film film = FilmGenerator.getFilm(1L, "name update", "description test",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        filmDbStorage.update(film);
        film = filmDbStorage.getFilmById(1L);
        assertThat(film).hasFieldOrPropertyWithValue("name", "name update");
    }

    @Test
    @Order(3)
    @DisplayName("3) Проверка обновления неизвестного фильма")
    void updateUnknownFilmTest() {
        Film film = FilmGenerator.getFilm(9999L, "name update", "description test",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        String message = null;
        try {
            filmDbStorage.update(film);
        } catch (NotFoundException e) {
            message = e.getMessage();
        }
        assertEquals("Такого фильма не существует.", message);
    }

    @Test
    @Order(4)
    @DisplayName("4) Проверка получения всех фильмов")
    void getAllFilmsTest() {
        Film film = FilmGenerator.getFilm(1L, "name update", "description test",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        List<Film> films = filmDbStorage.getAll();
        assertEquals(1, films.size());
        assertEquals(film, films.get(0));
    }

    @Test
    @Order(5)
    @DisplayName("5) Проверка удаления фильма")
    void deleteFilmByIdTest() {
        filmDbStorage.deleteById(1L);
        Film film = filmDbStorage.getFilmById(1L);
        assertNull(film);
    }

    @Test
    @Order(6)
    @DisplayName("6) Проверка получения популярных фильмов")
    void getPopularFilmTest() {
        User user1 = UserGenerator.getUser(1L, "first@email.com", "first", "first",
                LocalDate.now().minusYears(20));
        User user2 = UserGenerator.getUser(2L, "second@email.com", "second", "second",
                LocalDate.now().minusYears(20));
        User user3 = UserGenerator.getUser(3L, "third@email.com", "third", "third",
                LocalDate.now().minusYears(20));
        Film firstfilm = FilmGenerator.getFilm(2L, "name update", "description test",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        Film secondfilm = FilmGenerator.getFilm(3L, "second film", "description second film",
                LocalDate.now().minusDays(2), 120, MPA.builder().id(1).name("G").build());
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        filmDbStorage.create(firstfilm);
        filmDbStorage.create(secondfilm);
        filmDbStorage.addLike(2L, user1);
        filmDbStorage.addLike(2L, user2);
        filmDbStorage.addLike(3L, user3);
        List<Film> films = filmDbStorage.getPopularFilms(2);
        assertEquals(firstfilm, films.get(0));
        assertEquals(secondfilm, films.get(1));
    }
}