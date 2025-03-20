package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String ADD_LIKE_QUERY = "INSERT INTO films_likes (film_id , user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT F.* " +
            "FROM FILMS F " +
            "JOIN ( " +
            "    SELECT FL.film_id " +
            "    FROM FILMS_LIKES FL " +
            "    GROUP BY FL.film_id " +
            "    ORDER BY COUNT(FL.USER_ID)  DESC, FL.film_id " +
            "    LIMIT ? " +
            ") AS top_films ON F.ID = top_films.film_id";
    private static final String FIND_GENRES_FOR_FILM_QUERY = "select * from genres where id in (select fg.genre_id from FILMS_GENRES as fg where FILM_ID = ?)";
    private static final String FIND_MPA_FOR_FILM_QUERY = "select * from mpa where id = ?";
    private static final String INSERT_FILMS_GENRES = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_FILMS_GENRES_BY_FILM_ID = "DELETE FROM films_genres WHERE film_id = ?";

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<Mpa> mpaRowMapper;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper, JdbcTemplate jdbcTemplate1, RowMapper<Mpa> mpaRowMapper, MpaStorage mpaStorage) {
        super(jdbcTemplate, mapper);
        this.jdbcTemplate = jdbcTemplate1;
        this.mpaRowMapper = mpaRowMapper;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        fillFilmsByGenresAndMpa(films);
        return films;
    }

    @Override
    public Film findFilmById(Integer id) {
        Optional<Film> one = findOne(FIND_BY_ID_QUERY, id);
        Film film = one.orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + id));
        fillFilmsByGenresAndMpa(List.of(film));

        return film;
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        return simpleInsert(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        return delete(REMOVE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public Film save(Film film) {
        return insertFilmWithGenres(film);
    }

    public Film update(Film film) {
        updateFilmWithGenres(film);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        List<Film> films = jdbcTemplate.query(FIND_POPULAR_FILMS_QUERY, ps -> {
            ps.setInt(1, limit);
        }, new FilmRowMapper());
        fillFilmsByGenresAndMpa(films);

        return films;
    }

    private void fillFilmsByGenresAndMpa(List<Film> films) {
        for (Film film : films) {
            List<Genre> genres = jdbcTemplate.query(FIND_GENRES_FOR_FILM_QUERY, ps -> {
                ps.setInt(1, film.getId());
            }, new GenreRowMapper());
            film.getGenres().addAll(genres);

            Mpa mpa = mpaStorage.findById(film.getMpa().getId()).orElse(null);
            film.setMpa(mpa);
        }
    }

    private Film insertFilmWithGenres(Film film) {
        int id = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genre> genres = film.getGenres()
                    .stream()
                    .distinct()
                    .toList();
            insertFilmGenres(id, genres);
        }
        Mpa mpa = jdbcTemplate.queryForObject(FIND_MPA_FOR_FILM_QUERY, mpaRowMapper, film.getMpa().getId());
        film.setMpa(mpa);
        return film;
    }

    private void insertFilmGenres(int filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            executeUpdate(INSERT_FILMS_GENRES, filmId, genre.getId());
        }
    }

    private void updateFilmWithGenres(Film film) {
        updateFilm(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            updateFilmGenres(film.getId(), film.getGenres());
        }
    }

    private void updateFilm(Film film) {
        update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    private void updateFilmGenres(int filmId, List<Genre> genres) {
        update(DELETE_FILMS_GENRES_BY_FILM_ID, filmId);
        for (Genre genre : genres) {
            executeUpdate(INSERT_FILMS_GENRES, filmId, genre.getId());
        }
    }
}