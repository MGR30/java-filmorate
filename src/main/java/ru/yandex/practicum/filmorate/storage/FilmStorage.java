package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film findFilmById(Integer id);

    Film save(Film film);

    Film update(Film film);

    boolean addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(int limit);
}
