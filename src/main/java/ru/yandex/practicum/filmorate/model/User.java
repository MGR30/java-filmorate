package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный адрес электронной почты")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friends;

    public User() {
        friends = new HashSet<>();
    }
}
