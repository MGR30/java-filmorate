package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final String LOG_ERROR_VALIDATION_MESSAGE = "Валидация не пройдена по причине : {}";
    private final Map<Long, User> userStorage = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        Validator.validate(user);
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        userStorage.put(user.getId(), user);
        log.info("Созданная сущность пользователя: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            log.error("Идентификатор должен быть указан");
            throw new ValidationException("Идентификатор должен быть указан");
        }
        if (userStorage.containsKey(user.getId())) {
            Validator.validate(user);
            User userToUpdate = userStorage.get(user.getId());
            userToUpdate.setName(user.getName());
            userToUpdate.setLogin(user.getLogin());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setBirthday(user.getBirthday());
            return userStorage.put(user.getId(), userToUpdate);
        }
        throw new NotFoundException("Пользователь не найден");
    }

    private long getNextId() {
        long currentMaxId = userStorage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}


