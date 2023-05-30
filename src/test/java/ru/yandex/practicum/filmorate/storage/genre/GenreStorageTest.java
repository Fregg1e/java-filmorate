package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    @Order(1)
    @DisplayName("1) Проверка получения всех жанров")
    void getAllGenresTest() {
        List<Genre> genres = genreDbStorage.getAll();
        assertEquals(6, genres.size());
        assertEquals("Комедия", genres.get(0).getName());
    }

    @Test
    @Order(2)
    @DisplayName("2) Проверка получения жанра по id")
    void getGenreByIdTest() {
        Genre genre = genreDbStorage.getGenreById(3);
        assertEquals("Мультфильм", genre.getName());
    }
}