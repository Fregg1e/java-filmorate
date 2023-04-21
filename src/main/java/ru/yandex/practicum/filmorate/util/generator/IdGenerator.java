package ru.yandex.practicum.filmorate.util.generator;

public class IdGenerator {
    private Long id = 1L;

    public Long generateId() {
        return id++;
    }
}
