package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film findFilmById(Long id);

    Film save(Film film);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);
}
