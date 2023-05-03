package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final UserStorage userStorage;

    public List<User> getAll() {
        return new ArrayList<>(userStorage.getAll().values());
    }

    public User create(User user) {
        user.setId(idGenerator.generateId());
        userStorage.create(user);
        log.debug("Создан пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        userStorage.update(user);
        log.debug("Пользователь с id={} обновлен.", user.getId());
        return user;
    }
}
