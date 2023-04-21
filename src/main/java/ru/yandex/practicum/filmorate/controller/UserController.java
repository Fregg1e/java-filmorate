package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private IdGenerator idGenerator = new IdGenerator();
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsValue(user)) {
            ValidationException validationException = new ValidationException("Такой пользователь уже существует.");
            log.error("Произошло исключение! ", validationException);
            throw validationException;
        }
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            ValidationException validationException = new ValidationException("Такого пользователя не существует.");
            log.error("Произошло исключение! ", validationException);
            throw validationException;
        }
        users.put(user.getId(), user);
        log.debug("Пользователь с id={} обновлен.", user.getId());
        return user;
    }
}
