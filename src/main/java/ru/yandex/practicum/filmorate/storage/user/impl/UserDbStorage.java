package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(Long id) {
        String sqlQueryUserData = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?;";
        String sqlQueryUsersFriends = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? AND STATUS = 'TRUE'";
        User user = jdbcTemplate.queryForObject(sqlQueryUserData, this::mapRowToUser);

        if (user == null) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        user.setFriends(new HashSet<>(jdbcTemplate.query(sqlQueryUsersFriends,
                                      (rs, rowNum) -> rs.getLong("FRIEND_ID"), id)));
        return user;
    }

    @Override
    public void create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?);";

        if (getUserById(user.getId()) != null) {
            log.error("Произошло исключение!");
            throw new AlreadyExistException("Такой пользователь уже существует.");
        }
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                            user.getName(), Date.valueOf(user.getBirthday()));
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?;";

        if (getUserById(user.getId()) == null) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), Date.valueOf(user.getBirthday()), user.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?;";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?);";

        if (getUserById(id) == null || getUserById(friendId) == null) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        jdbcTemplate.update(sqlQuery, id, friendId, true);
        jdbcTemplate.update(sqlQuery, friendId, id, false);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ?;";

        if (getUserById(id) == null || getUserById(friendId) == null) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя не существует.");
        }
        if (!getUserById(id).getFriends().contains(friendId)) {
            log.error("Произошло исключение!");
            throw new NotFoundException("Такого пользователя нет в друзьях.");
        }
        jdbcTemplate.update(sqlQuery, id);
        jdbcTemplate.update(sqlQuery, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
