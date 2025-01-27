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
import java.util.HashSet;
import java.util.Objects;

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

        User userToUpdate = userStorage.findById(user.getId());
        if (Objects.nonNull(userToUpdate)) {
            userToUpdate.setName(user.getName());
            userToUpdate.setLogin(user.getLogin());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setBirthday(user.getBirthday());
            return userStorage.save(userToUpdate);
        }

        throw new NotFoundException("Пользователь не найден");
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        checkNonNullUser(userId);
        user.getFriends().add(friendId);
        User friend = userStorage.findById(friendId);
        checkNonNullUser(friendId);
        friend.getFriends().add(userId);
        userStorage.save(friend);
        userStorage.save(user);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        checkNonNullUser(userId);
        user.getFriends().remove(friendId);
        User friend = userStorage.findById(friendId);
        checkNonNullUser(friendId);
        friend.getFriends().remove(userId);
        userStorage.save(user);
        userStorage.save(friend);
        return user;
    }

    public Collection<User> getFriends(Long userId) {
        checkNonNullUser(userId);
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        checkNonNullUser(firstUserId);
        checkNonNullUser(secondUserId);
        Collection<User> friends = new HashSet<>(userStorage.getFriends(firstUserId));
        friends.retainAll(userStorage.getFriends(secondUserId));
        return friends;
    }

    private void checkNonNullUser(Long userId) {
        if (Objects.isNull(userStorage.findById(userId))) {
            throw new NotFoundException("Пользователь с таким идентификатором не найден");
        }
    }
}
