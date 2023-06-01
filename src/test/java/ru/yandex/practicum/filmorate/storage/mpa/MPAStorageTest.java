package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MPADbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MPAStorageTest {
    private final MPADbStorage mpaDbStorage;

    @Test
    @Order(1)
    @DisplayName("1) Проверка получения всех MPA")
    void getAllMPATest() {
        List<MPA> mpas = mpaDbStorage.getAll();
        assertEquals(5, mpas.size());
        assertEquals("G", mpas.get(0).getName());
    }

    @Test
    @Order(2)
    @DisplayName("2) Проверка получения MPA по id")
    void getMPAByIdTest() {
        MPA mpa = mpaDbStorage.getMPAById(3);
        assertEquals("PG-13", mpa.getName());
    }
}