package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilmControllerValidationTest {

    @Test
    void testCreateFilmWithBlankName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateFilmWithTooLongDescription() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateFilmWithNegativeDuration() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-1);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateFilmWithBlankName() {
        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateFilmWithTooLongDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Name");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateFilmWithInvalidReleaseDate() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateFilmWithNegativeDuration() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-1);

        assertThatThrownBy(() -> Validator.validate(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateValidFilm() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Validator.validate(film);
    }
}
