package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAll();

    User save(User user);

    User update(User user);

    Optional<User> findById(Integer id);

    Collection<User> getFriends(Integer userId);

    Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId);

    boolean removeFriend(Integer userId, Integer friendId);

    User addFriend(Integer userId, Integer friendId);

    Optional<User> confirmFriend(Integer userId, Integer friendId);
}