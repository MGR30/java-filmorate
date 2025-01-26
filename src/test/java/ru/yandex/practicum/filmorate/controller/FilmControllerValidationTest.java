package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerValidationTest {
    private Film film;
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100L);
        filmController = new FilmController();
    }

    @Test
    void validationTest_success() {
        Film film = filmController.create(this.film);
        Assertions.assertTrue(filmController.getAllFilms().contains(film));
    }

    @Test
    void validationTest_nameIsEmpty_throwsException() {
        film.setName("");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(this.film));
    }

    @Test
    void validationTest_nameIsNull_throwsException() {
        film.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(this.film));
    }

    @Test
    void validationTest_descriptionIsExceeds_throwsException() {
        film.setDescription("Группа исследователей отправляется в заброшенный город, чтобы найти древние артефакты, но сталкивается с мистическими силами, которые изменяют их восприятие реальности. В борьбе за выживание им предстоит раскрыть тайны прошлого и противостоять собственным страхам.");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(this.film));
    }

    @Test
    void validationTest_durationIsNegative_throwsException() {
        film.setDuration(-100L);
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(this.film));
    }

    @Test
    void validationTest_releaseDateIsOlderThenCinemaBirthday_throwsException() {
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(this.film));
    }
}