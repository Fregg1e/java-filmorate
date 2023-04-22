package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {
    private final IdGenerator idGenerator;
    private final Map<Long, User> users;

    public UserService() {
        idGenerator = new IdGenerator();
        users = new HashMap<>();
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        if (users.containsValue(user)) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такой пользователь уже существует.");
        }
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такого пользователя не существует.");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь с id={} обновлен.", user.getId());
        return user;
    }
}
