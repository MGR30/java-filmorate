package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();

    public Collection<User> getAll() {
        return userStorage.values();
    }

    public User findById(Long id) {
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

    public Collection<User> getFriends(Long userId) {
        User user = findById(userId);
        return user.getFriends().stream().map(this::findById).toList();
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
