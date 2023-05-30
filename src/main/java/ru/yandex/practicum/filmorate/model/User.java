package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.util.validator.string.NotSpacesConstrain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(regexp = "^[a-zA-Z\\.\\-\\_]+[@][a-zA-Z\\.]+[\\.].+$",
            message = "Email не соответствует должному формату.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @NotSpacesConstrain(message = "Логин содержит пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть позже чем сегодня.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Long> friends;

    public User() {
    }

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.equals("")) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.equals("")) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.friends = friends;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void confirmFriendRequest(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
