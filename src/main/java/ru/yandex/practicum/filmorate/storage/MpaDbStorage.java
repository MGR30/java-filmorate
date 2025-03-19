package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa order by id asc";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    protected final JdbcTemplate jdbc;
    protected final RowMapper<Mpa> mapper;

    @Override
    public List<Mpa> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Mpa> findById(Integer id) {
        List<Mpa> mpaFromDB = jdbc.query(FIND_BY_ID_QUERY, new Object[]{id}, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        });
        System.out.println("найдено" + mpaFromDB);
        return mpaFromDB.isEmpty() ? Optional.empty() : Optional.of(mpaFromDB.getFirst());
    }
}