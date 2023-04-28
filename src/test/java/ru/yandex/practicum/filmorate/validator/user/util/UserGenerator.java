package ru.yandex.practicum.filmorate.validator.user.util;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserGenerator {
    public static User getUser(Long id, String email, String login, String name, LocalDate birthday) {
        return new User(id, email, login, name, birthday);
    }
}
