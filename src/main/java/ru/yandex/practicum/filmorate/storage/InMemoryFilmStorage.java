package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmsStorage = new HashMap<>();
    private final Map<Integer, Set<Integer>> filmsLikes = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return filmsStorage.values();
    }

    @Override
    public Film findFilmById(Integer id) {
        return filmsStorage.get(id);
    }

    @Override
    public Film save(Film film) {
        checkListContaining(film.getId());
        if (Objects.isNull(film.getId())) {
            film.setId(getNextId());
        }

        filmsStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        checkListContaining(film.getId());
        if (Objects.isNull(film.getId())) {
            film.setId(getNextId());
        }

        filmsStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        return filmsLikes.get(filmId).add(userId);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        return filmsLikes.get(filmId).remove(userId);
    }

    private int getNextId() {
        int currentMaxId = filmsStorage.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkListContaining(Integer id) {
        if (!filmsLikes.containsKey(id)) {
            filmsLikes.put(id, new HashSet<>());
        }
    }
}
