package ru.yandex.practicum.filmorate.validator.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validator.user.util.UserGenerator.getUser;
import static ru.yandex.practicum.filmorate.validator.util.ErrorChecker.hasErrorMessage;

public class UserValidationTest {
    @Test
    @DisplayName("1) Проверка валидации с null параметрами")
    public void userValidationWithAllNullParamTest() {
        User user = getUser(null, null, null, null, null);
        assertTrue(hasErrorMessage(user, "Email не может быть пустым."));
        assertTrue(hasErrorMessage(user, "Логин не может быть пустым."));
    }

    @ParameterizedTest(name = "{index}. Проверка невалидности названия \"{arguments}\"")
    @ValueSource(strings = {"", " ", "test", "test@test", "123test&@test.ru", "test-email@", "@email.com"})
    @DisplayName("2) Проверка невалидности email")
    public void userEmailValidationTest(String email) {
        User user = getUser(1L, email, "login", "name", LocalDate.now().minusYears(20));
        assertTrue(hasErrorMessage(user, "Email не соответствует должному формату."));
    }

    @ParameterizedTest(name = "{index}. Проверка невалидности логина на пробелы \"{arguments}\"")
    @ValueSource(strings = {" ", " test", "te st", "test "})
    @DisplayName("3) Проверка невалидности логина на пробелы")
    public void userLoginSpacesValidationTest(String login) {
        User user = getUser(1L, "test@email.ru", login,
                "name", LocalDate.now().minusYears(20));
        assertTrue(hasErrorMessage(user, "Логин содержит пробелы."));
    }

    @Test
    @DisplayName("4) Проверка валидации с name = null")
    public void userNullNameValidationTest() {
        User user = getUser(1L, "test@email.ru", "login",
                null, LocalDate.now().minusYears(20));
        assertEquals("login", user.getName());
    }

    @Test
    @DisplayName("5) Проверка валидации с дня рождения")
    public void userBirthdayValidationTest() {
        User user = getUser(1L, "test@email.ru", "login",
                null, LocalDate.now().minusDays(1));
        assertFalse(hasErrorMessage(user, "Дата рождения не может быть позже чем сегодня."));

        user = getUser(1L, "test@email.ru", "login",
                null, LocalDate.now());
        assertFalse(hasErrorMessage(user, "Дата рождения не может быть позже чем сегодня."));

        user = getUser(1L, "test@email.ru", "login",
                null, LocalDate.now().plusDays(1));
        assertTrue(hasErrorMessage(user, "Дата рождения не может быть позже чем сегодня."));
    }
}
