package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.validator.date.PastLocalDateConstrain;

import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше 200 символов.")
    private String description;
    @PastLocalDateConstrain(current = "1895-12-28", message = "Дата релиза не может быть раньше 1895-12-28.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной.")
    private Integer duration;
}
