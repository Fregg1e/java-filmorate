package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
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
        String sqlQueryUsersFriends = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? AND STATUS = TRUE;";

        try {
            User user = jdbcTemplate.queryForObject(sqlQueryUserData, this::mapRowToUser, id);
            if (user != null) {
                user.setFriends(new HashSet<>(jdbcTemplate.query(sqlQueryUsersFriends,
                        (rs, rowNum) -> rs.getLong("FRIEND_ID"), id)));
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.error("Произошло исключение! Такого пользователя не существует.");
            throw new NotFoundException("Такого пользователя не существует.");
        }
    }

    @Override
    public Long create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?);";

        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), Date.valueOf(user.getBirthday()));
        return jdbcTemplate.queryForObject("SELECT MAX(USER_ID) FROM USERS;", Long.class);
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?;";

        getUserById(user.getId());
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
        String sqlQuery = "MERGE INTO FRIENDS AS F "
                + "USING VALUES (?, ?) AS U(U1, U2) ON F.USER_ID = U.U1 AND F.FRIEND_ID = U.U2 "
                + "WHEN MATCHED AND F.STATUS = FALSE THEN UPDATE "
                + "SET STATUS = TRUE "
                + "WHEN NOT MATCHED THEN "
                + "INSERT (USER_ID, FRIEND_ID, STATUS) "
                + "VALUES (U1, U2, ?);";

        getUserById(id);
        getUserById(friendId);
        jdbcTemplate.update(sqlQuery, id, friendId, true);
        jdbcTemplate.update(sqlQuery, friendId, id, false);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?;";

        User user = getUserById(id);
        getUserById(friendId);
        if (!user.getFriends().contains(friendId)) {
            log.error("Произошло исключение! Такого пользователя нет в друзьях.");
            throw new NotFoundException("Такого пользователя нет в друзьях.");
        }
        jdbcTemplate.update(sqlQuery, id, friendId);
        jdbcTemplate.update(sqlQuery, friendId, id);
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
