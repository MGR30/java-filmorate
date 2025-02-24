package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        Validator.validate(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return userStorage.save(user);
    }

    public User update(User user) {
        Validator.validate(user);
        if (user.getId() == null) {
            log.error("Идентификатор должен быть указан");
            throw new ValidationException("Идентификатор должен быть указан");
        }

        User userToUpdate = userStorage.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + user.getId()));

        userToUpdate.setName(user.getName());
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setBirthday(user.getBirthday());
        return userStorage.update(userToUpdate);

    }

    public User addFriend(Integer userId, Integer friendId) {
        checkNonNullUser(userId);
        checkNonNullUser(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    public boolean removeFriend(Integer userId, Integer friendId) {
        checkNonNullUser(userId);
        checkNonNullUser(friendId);
        return userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(Integer userId) {
        checkNonNullUser(userId);
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        checkNonNullUser(firstUserId);
        checkNonNullUser(secondUserId);
        return userStorage.getCommonFriends(firstUserId, secondUserId);
    }

    public User confirmFriend(Integer userId, Integer friendId) {
        return userStorage.confirmFriend(userId, friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    private void checkNonNullUser(Integer userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }
}
