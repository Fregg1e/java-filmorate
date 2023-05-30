package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;
import ru.yandex.practicum.filmorate.validator.user.util.UserGenerator;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    @Order(1)
    @DisplayName("1) Проверка создания пользователя")
    void createUserTest() {
        User user = UserGenerator.getUser(1L, "test@email.com", "test", "test",
                LocalDate.now().minusYears(20));
        userDbStorage.create(user);
        user = userDbStorage.getUserById(1L);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    @DisplayName("2) Проверка обновления пользователя")
    void updateUserTest() {
        User user = UserGenerator.getUser(1L, "testupdate@email.com", "test", "test",
                LocalDate.now().minusYears(20));
        userDbStorage.update(user);
        user = userDbStorage.getUserById(1L);
        assertThat(user).hasFieldOrPropertyWithValue("email", "testupdate@email.com");
    }

    @Test
    @Order(3)
    @DisplayName("3) Проверка обновления неизвестного пользователя")
    void updateUnknownUserTest() {
        User user = UserGenerator.getUser(9999L, "testupdate@email.com", "test", "test",
                LocalDate.now().minusYears(20));
        String message = null;
        try {
            userDbStorage.update(user);
        } catch (NotFoundException e) {
            message = e.getMessage();
        }
        assertEquals("Такого пользователя не существует.", message);
    }

    @Test
    @Order(4)
    @DisplayName("4) Проверка получения всех пользователей")
    void getAllUsersTest() {
        User user = UserGenerator.getUser(1L, "testupdate@email.com", "test", "test",
                LocalDate.now().minusYears(20));
        List<User> users = userDbStorage.getAll();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    @Order(5)
    @DisplayName("5) Проверка удаления пользователя")
    void deleteUserByIdTest() {
        userDbStorage.deleteById(1L);
        User user = userDbStorage.getUserById(1L);
        assertNull(user);
    }

    @Test
    @Order(6)
    @DisplayName("6) Проверка добавления в друзья")
    void addFriendTest() {
        User user = UserGenerator.getUser(2L, "test@email.com", "test", "test",
                LocalDate.now().minusYears(20));
        User friend = UserGenerator.getUser(3L, "friend@email.com", "friend", "friend",
                LocalDate.now().minusYears(21));
        userDbStorage.create(user);
        userDbStorage.create(friend);
        userDbStorage.addFriend(2L, 3L);
        user = userDbStorage.getUserById(2L);
        friend = userDbStorage.getUserById(3L);
        assertTrue(user.getFriends().contains(3L));
        assertFalse(friend.getFriends().contains(2L));
    }

    @Test
    @Order(7)
    @DisplayName("7) Проверка подтверждения добавления в друзья")
    void confirmFriendshipTest() {
        userDbStorage.addFriend(3L, 2L);
        User user = userDbStorage.getUserById(2L);
        User friend = userDbStorage.getUserById(3L);
        assertTrue(user.getFriends().contains(3L));
        assertTrue(friend.getFriends().contains(2L));
    }

    @Test
    @Order(8)
    @DisplayName("8) Проверка удаления из друзей")
    void removeFriendshipTest() {
        userDbStorage.removeFriend(2L, 3L);
        User user = userDbStorage.getUserById(2L);
        User friend = userDbStorage.getUserById(3L);
        assertTrue(user.getFriends().isEmpty());
        assertTrue(friend.getFriends().isEmpty());
    }
}