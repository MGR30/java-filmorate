package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDateValid;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;
    @FilmReleaseDateValid(message = "Дата выхода фильма должна быть позже 28 декабря 1895 или равна ей")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
    private Mpa mpa;
    private List<Genre> genres;
}
