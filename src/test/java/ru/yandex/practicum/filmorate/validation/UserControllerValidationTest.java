package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserControllerValidationTest {
    @Test
    void testCreateUserWithBlankEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateUserWithInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateUserWithBlankLogin() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateUserWithLoginContainingSpaces() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateUserWithFutureBirthday() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1)); // Дата в будущем

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateUserWithBlankEmail() {
        User user = new User();
        user.setId(1);
        user.setEmail("");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateUserWithInvalidEmail() {
        User user = new User();
        user.setId(1);
        user.setEmail("invalid-email");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateUserWithBlankLogin() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateUserWithLoginContainingSpaces() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testUpdateUserWithFutureBirthday() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThatThrownBy(() -> Validator.validate(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Валидация не пройдена");
    }

    @Test
    void testCreateValidUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Validator.validate(user);
    }
}