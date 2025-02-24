package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id ASC ";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

    protected final JdbcTemplate jdbc;
    protected final RowMapper<Genre> mapper;

    @Override
    public List<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        List<Genre> genres = jdbc.query(FIND_BY_ID_QUERY, new GenreRowMapper(), id);
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
    }
}
