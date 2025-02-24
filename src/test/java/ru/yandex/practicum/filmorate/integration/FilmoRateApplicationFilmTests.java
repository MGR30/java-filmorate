package ru.yandex.practicum.filmorate.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.properties")
@Import({FilmDbStorage.class, FilmRowMapper.class, MpaDbStorage.class, MpaRowMapper.class, GenreRowMapper.class})
public class FilmoRateApplicationFilmTests {
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .hasSize(4)
                .extracting("id")
                .containsExactlyInAnyOrder(1, 2, 3, 4);

        Film funnyAdventure = films.stream().filter(f -> f.getId() == 1).findFirst().get();
        assertThat(funnyAdventure.getGenres())
                .hasSize(1)
                .extracting("name")
                .containsExactly("Комедия");
        assertThat(funnyAdventure.getMpa().getName()).isEqualTo("PG");
    }

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.findFilmById(3);

        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 3)
                .hasFieldOrPropertyWithValue("name", "Space Wars")
                .hasFieldOrPropertyWithValue("description", "Epic space battles")
                .hasFieldOrPropertyWithValue("duration", 150);

        assertThat(film.getGenres())
                .hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("Документальный", "Мультфильм");

        assertThat(film.getMpa().getName()).isEqualTo("PG-13");
    }

    @Test
    public void testFindFilmByIdNotFound() {
        assertThatThrownBy(() -> filmStorage.findFilmById(999))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Фильм не найден с ID: 999");
    }

    @Test
    public void testSaveFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Movie");
        newFilm.setDescription("New Description");
        newFilm.setReleaseDate(LocalDate.of(2025, 1, 1));
        newFilm.setDuration(140);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        newFilm.setMpa(mpa);
        Genre genre1 = new Genre();
        genre1.setId(6);
        genre1.setName("Боевик");
        Genre genre2 = new Genre();
        genre2.setId(1);
        genre2.setName("Комедия");
        newFilm.setGenres(List.of(genre1, genre2));

        Film savedFilm = filmStorage.save(newFilm);

        assertThat(savedFilm)
                .hasFieldOrPropertyWithValue("name", "New Movie")
                .matches(f -> f.getId() > 0);

        assertThat(savedFilm.getGenres())
                .hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("Боевик", "Комедия");

        assertThat(savedFilm.getMpa().getName()).isEqualTo("G");
    }

    @Test
    public void testUpdateFilm() {
        Film filmToUpdate = filmStorage.findFilmById(2);
        filmToUpdate.setName("Updated Dark Secrets");
        filmToUpdate.setDescription("Updated mystery tale");
        Mpa mpa = new Mpa();
        mpa.setId(5);
        mpa.setName("NC-17");
        filmToUpdate.setMpa(mpa);
        Genre genre1 = new Genre();
        genre1.setId(2);
        genre1.setName("Драма");
        Genre genre2 = new Genre();
        genre2.setId(4);
        genre2.setName("Триллер");
        filmToUpdate.setGenres(List.of(genre1, genre2));

        Film updatedFilm = filmStorage.update(filmToUpdate);

        assertThat(updatedFilm)
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "Updated Dark Secrets")
                .hasFieldOrPropertyWithValue("description", "Updated mystery tale");

        assertThat(updatedFilm.getGenres())
                .hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("Драма", "Триллер");

        assertThat(updatedFilm.getMpa().getName()).isEqualTo("NC-17");
    }

    @Test
    public void testAddLike() {
        boolean result = filmStorage.addLike(3, 2);

        assertThat(result).isTrue();

        String checkSql = "SELECT COUNT(*) FROM films_likes WHERE film_id = 3 AND user_id = 2";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void testRemoveLike() {
        boolean result = filmStorage.removeLike(1, 1);

        assertThat(result).isTrue();

        String checkSql = "SELECT COUNT(*) FROM films_likes WHERE film_id = 1 AND user_id = 1";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testGetPopularFilms() {
        List<Film> popularFilms = filmStorage.getPopularFilms(3);

        assertThat(popularFilms)
                .hasSize(3)
                .extracting("id")
                .containsExactly(1, 4, 2);

        assertThat(popularFilms.get(0))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "The Funny Adventure");

        assertThat(popularFilms.get(0).getGenres()).isNotEmpty();
        assertThat(popularFilms.get(0).getMpa()).isNotNull();
    }
}
