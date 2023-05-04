package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    Map<Long, User> getAll();
    User getUserById(Long id);
    void create(User user);
    void update(User user);
    void deleteById(Long id);
    void addFriend(Long id, Long friendId);
    void removeFriend(Long id, Long friendId);
}
