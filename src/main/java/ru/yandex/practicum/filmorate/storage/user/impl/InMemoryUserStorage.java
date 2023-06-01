package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        return users.get(id);
    }

    @Override
    public Long create(User user) {
        if (users.containsValue(user)) {
            log.error("Произошло исключение!");
            throw new AlreadyExistException("Такой пользователь уже существует.");
        }
        users.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        user.setFriends(users.get(user.getId()).getFriends());
        users.put(user.getId(), user);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        users.get(id).addFriend(friendId);
        users.get(friendId).addFriend(id);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        if (!users.get(id).getFriends().contains(friendId)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя нет в друзьях.");
        }
        users.get(id).removeFriend(friendId);
        users.get(friendId).removeFriend(id);
    }
}
