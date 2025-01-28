package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsStorage = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return filmsStorage.values();
    }

    @Override
    public Film findFilmById(Long id) {
        return filmsStorage.get(id);
    }

    @Override
    public Film save(Film film) {
        if (Objects.isNull(film.getId())) {
            film.setId(getNextId());
        }

        filmsStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = filmsStorage.get(filmId);
        film.getLikes().add(userId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = filmsStorage.get(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    private long getNextId() {
        long currentMaxId = filmsStorage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
