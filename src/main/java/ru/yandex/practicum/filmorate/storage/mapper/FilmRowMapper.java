package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getObject("release_date", LocalDate.class));
        film.setDuration(rs.getInt("duration"));

        if (hasColumn(rs)) {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            film.setMpa(mpa);
        }

        film.setGenres(new ArrayList<>());

        return film;
    }

    private boolean hasColumn(ResultSet rs) {
        try {
            rs.findColumn("mpa_id");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}