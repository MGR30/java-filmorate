package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaRowMapper.class})
public class FilmoRateApplicationMpaTests {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetAll() {
        List<Mpa> mpaList = mpaStorage.getAll();

        assertThat(mpaList)
                .hasSize(5)
                .extracting("id")
                .containsExactly(1, 2, 3, 4, 5); // Сортировка по id ASC

        assertThat(mpaList)
                .extracting("name")
                .containsExactly("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    public void testFindByIdSuccess() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(3);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa)
                            .hasFieldOrPropertyWithValue("id", 3)
                            .hasFieldOrPropertyWithValue("name", "PG-13");
                });
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(999);

        assertThat(mpaOptional)
                .isNotPresent();
    }
}
