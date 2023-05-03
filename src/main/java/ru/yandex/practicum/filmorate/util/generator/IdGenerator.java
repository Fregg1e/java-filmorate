package ru.yandex.practicum.filmorate.util.generator;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private Long id = 1L;

    public Long generateId() {
        return id++;
    }
}
