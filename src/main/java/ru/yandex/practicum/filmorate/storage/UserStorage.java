package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User save(User user);

    User findById(Long id);

    Collection<User> getFriends(Long userId);
}
