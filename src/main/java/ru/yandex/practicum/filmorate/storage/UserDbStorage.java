package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "JOIN friendships f ON u.id = f.friend_id " +
            "WHERE f.user_id = ?";

    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "JOIN friendships f1 ON u.id = f1.friend_id " +
            "JOIN friendships f2 ON u.id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";

    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE (user_id = ? AND friend_id = ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User save(User user) {
        int id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> getFriends(Integer userId) {
        return jdbc.query(GET_FRIENDS_QUERY, new Object[]{userId}, new UserRowMapper());
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) {
        return jdbc.query(GET_COMMON_FRIENDS_QUERY, new Object[]{userId, otherUserId}, new UserRowMapper());
    }

    @Override
    public boolean removeFriend(Integer userId, Integer friendId) {


        return jdbc.update(DELETE_FRIENDSHIP_QUERY, userId, friendId) > 0;
    }

    @Override
    public Optional<User> confirmFriend(Integer userId, Integer friendId) {
        String checkSql = "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?";
        List<Friendship> existingRequests = jdbc.query(
                checkSql,
                new Object[]{friendId, userId},
                new FriendshipRowMapper()
        );

        if (existingRequests.isEmpty()) {
            throw new RuntimeException("Заявка в друзья от пользователя " + friendId + " к " + userId + " не найдена");
        }

        // Проверяем, не подтверждена ли дружба уже (обратная запись)
        List<Friendship> reverseFriendship = jdbc.query(
                checkSql,
                new Object[]{userId, friendId},
                new FriendshipRowMapper()
        );

        if (!reverseFriendship.isEmpty()) {
            throw new RuntimeException("Дружба между " + userId + " и " + friendId + " уже подтверждена");
        }

        // Добавляем обратную запись для подтверждения дружбы
        String insertSql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        jdbc.update(insertSql, userId, friendId);

        // Возвращаем пользователя
        return findById(userId);
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = findById(userId).orElseThrow();

        String checkSql = "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?";
        List<Friendship> existingFriendships = jdbc.query(checkSql, new Object[]{userId, friendId}, new FriendshipRowMapper());

        if (!existingFriendships.isEmpty()) {
            throw new RuntimeException("Заявка в друзья уже отправлена");
        }

        String sql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        jdbc.update(sql, userId, friendId);

        return user;
    }
}