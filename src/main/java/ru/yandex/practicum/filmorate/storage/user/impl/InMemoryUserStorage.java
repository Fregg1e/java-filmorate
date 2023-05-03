package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public Map<Long, User> getAll() {
        return users;
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public void create(User user) {
        if (users.containsValue(user)) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такой пользователь уже существует.");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Произошло исключение!");
            throw new ValidationException("Такого пользователя не существует.");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }
}
