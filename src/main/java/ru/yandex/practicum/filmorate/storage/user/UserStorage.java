package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User getUserById(Long id);

    Long create(User user);

    void update(User user);

    void deleteById(Long id);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);
}
