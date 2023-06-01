package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.validator.date.PastLocalDateConstrain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше 200 символов.")
    private String description;
    @PastLocalDateConstrain(current = "1895-12-28",
            message = "Дата релиза не может быть раньше 1895-12-28 или равной null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной.")
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres;
    @EqualsAndHashCode.Exclude
    private MPA mpa;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Long> likes;

    public void addLike(Long id) {
        likes.add(id);
    }

    public void removeLike(Long id) {
        likes.remove(id);
    }
}
