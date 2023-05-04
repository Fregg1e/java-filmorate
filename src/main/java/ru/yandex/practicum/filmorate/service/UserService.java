package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final UserStorage userStorage;

    public List<User> getAll() {
        return new ArrayList<>(userStorage.getAll().values());
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
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

    public void addFriend(Long id, Long friendId) {
        userStorage.addFriend(id, friendId);
        log.debug("Пользователь с id={} добавил пользователя с id={} в друзья.", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.removeFriend(id, friendId);
        log.debug("Пользователь с id={} удалил пользователя с id={} из друзей.", id, friendId);
    }

    public List<User> getUserFriends(Long id) {
        Set<Long> userFriendsId = userStorage.getUserById(id).getFriends();
        return userFriendsId.stream().map(userStorage::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> userFriends = userStorage.getUserById(id).getFriends();
        Set<Long> otherUserFriends = userStorage.getUserById(otherId).getFriends();
        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends.stream().map(userStorage::getUserById).collect(Collectors.toList());
    }
}
