package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class, GenreRowMapper.class})
public class FilmoRateApplicationGenresTests {
    private final GenreDbStorage genreStorage;

    @Test
    public void testFindAll() {
        List<Genre> genres = genreStorage.findAll();

        assertThat(genres)
                .hasSize(6)
                .extracting("id")
                .containsExactly(1, 2, 3, 4, 5, 6); // Сортировка по id ASC

        assertThat(genres)
                .extracting("name")
                .containsExactly("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");
    }

    @Test
    public void testFindByIdSuccess() {
        Optional<Genre> genreOptional = genreStorage.findById(4);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre)
                            .hasFieldOrPropertyWithValue("id", 4)
                            .hasFieldOrPropertyWithValue("name", "Триллер");
                });
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Genre> genreOptional = genreStorage.findById(999);

        assertThat(genreOptional)
                .isNotPresent();
    }
}
