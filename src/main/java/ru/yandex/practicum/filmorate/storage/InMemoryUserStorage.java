package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userStorage = new HashMap<>();

    public Collection<User> getAll() {
        return userStorage.values();
    }

    @Override
    public User findById(Integer id) {
        return userStorage.get(id);
    }

    public User save(User user) {
        if (Objects.isNull(user.getId())) {
            user.setId(getNextId());
        }

        userStorage.put(user.getId(), user);
        log.info("Созданная сущность пользователя: {}", user);
        return user;
    }

    @Override
    public Collection<User> getFriends(Integer userId) {
        User user = findById(userId);
        return user.getFriends().stream().map(this::findById).toList();
    }

    private int getNextId() {
        int currentMaxId = userStorage.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
