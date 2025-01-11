package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerValidationTest {
    private User user;
    private UserController userController;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("sir@ya.ru");
        user.setName("Name");
        user.setLogin("Login");
        user.setBirthday(LocalDate.now().minusYears(18));
        userController = new UserController();
    }

    @Test
    void validationTest_success() {
        User user = userController.create(this.user);
        Assertions.assertTrue(userController.getAll().contains(user));
    }

    @Test
    void validationTest_BlankEmail_throwsException() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validationTest_NotCorrectEmail_throwsException() {
        user.setEmail("sir.ya.ru");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validationTest_NullLogin_throwsException() {
        user.setLogin(null);
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validationTest_BirthdayInFuture_throwsException() {
        user.setBirthday(LocalDate.now().plusYears(1));
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }
}