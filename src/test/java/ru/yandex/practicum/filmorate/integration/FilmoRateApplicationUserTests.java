package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class FilmoRateApplicationUserTests {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbc;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAll() {
        Collection<User> users = userStorage.getAll();

        assertThat(users)
                .hasSize(4)
                .extracting("id")
                .containsExactlyInAnyOrder(1, 2, 3, 4);
    }

    @Test
    public void testSave() {
        User newUser = new User();
        newUser.setEmail("test@email.com");
        newUser.setLogin("test");
        newUser.setName("Test User");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.save(newUser);

        assertThat(savedUser)
                .hasFieldOrPropertyWithValue("email", "test@email.com")
                .matches(u -> u.getId() > 0);
    }

    @Test
    public void testUpdate() {
        User userToUpdate = userStorage.findById(1).get();
        userToUpdate.setName("Updated John");
        userToUpdate.setEmail("updated@email.com");

        User updatedUser = userStorage.update(userToUpdate);

        assertThat(updatedUser)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Updated John")
                .hasFieldOrPropertyWithValue("email", "updated@email.com");
    }

    @Test
    public void testGetFriends() {
        Collection<User> friends = userStorage.getFriends(1);

        assertThat(friends)
                .hasSize(1)
                .extracting("id")
                .containsExactly(3);
    }

    @Test
    public void testGetCommonFriends() {
        Collection<User> commonFriends = userStorage.getCommonFriends(1, 2);

        assertThat(commonFriends)
                .isEmpty();
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.addFriend(2, 3);

        assertThat(user)
                .hasFieldOrPropertyWithValue("id", 2);

        Collection<User> friends = userStorage.getFriends(2);
        assertThat(friends)
                .extracting("id")
                .contains(3);
    }

    @Test
    public void testRemoveFriend() {
        boolean removed = userStorage.removeFriend(1, 3);

        assertThat(removed).isTrue();

        Collection<User> friends = userStorage.getFriends(1);
        assertThat(friends)
                .isEmpty();
    }

    @Test
    public void testConfirmFriend() {
        // Проверяем исходное состояние (должна быть запись 1 -> 3)
        String checkInitialSql = "SELECT * FROM friendships WHERE user_id = 1 AND friend_id = 3";
        List<Friendship> initialFriendships = jdbc.query(checkInitialSql, new FriendshipRowMapper());
        assertThat(initialFriendships).hasSize(1);

        // Вызываем confirmFriend(3, 1) - подтверждаем дружбу от 3 к 1
        Optional<User> user = userStorage.confirmFriend(3, 1);

        // Проверяем, что пользователь возвращен
        assertThat(user)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 3)
                );

        // Проверяем, что обратная запись (3 -> 1) добавлена
        String checkSql = "SELECT * FROM friendships WHERE user_id = 3 AND friend_id = 1";
        List<Friendship> friendships = jdbc.query(checkSql, new FriendshipRowMapper());
        assertThat(friendships).hasSize(1);

        // Проверяем, что исходная запись (1 -> 3) осталась
        assertThat(initialFriendships).hasSize(1);
    }

    @Test
    public void testConfirmFriendNoRequest() {
        assertThatThrownBy(() -> userStorage.confirmFriend(1, 4))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Заявка в друзья от пользователя 4 к 1 не найдена");
    }
}
