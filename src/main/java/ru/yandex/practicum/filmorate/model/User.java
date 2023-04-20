package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.validator.string.NotSpacesConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Email не соответствует должному формату.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @NotSpacesConstrain(message = "Логин содержит пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть позже чем сегодня.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
