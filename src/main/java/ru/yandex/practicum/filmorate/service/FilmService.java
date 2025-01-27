package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film create(Film film) {
        log.info("Начало создания сущности фильма");
        Validator.validate(film);
        film.setDuration(film.getDuration());
        Film savedFilm = filmStorage.save(film);
        log.info("Созданная сущность фильма : {}", savedFilm);
        return savedFilm;
    }

    public Film update(Film film) {
        log.info("Начало обновления сущности фильма.");
        if (film.getId() == null) {
            log.error("Идентификатор должен быть указан");
            throw new ValidationException("Идентификатор должен быть указан");
        }
        Film filmToUpdate = filmStorage.findFilmById(film.getId());
        if (Objects.nonNull(filmToUpdate)) {
            Validator.validate(film);
            filmToUpdate.setName(film.getName());
            filmToUpdate.setDescription(film.getDescription());
            filmToUpdate.setDuration(film.getDuration());
            filmToUpdate.setReleaseDate(film.getReleaseDate());
            return filmStorage.save(filmToUpdate);
        }

        throw new NotFoundException("Фильм не найден");
    }

    public Film addLike(Long filmId, Long userId) {
        checkNonNullFilm(filmId);
        checkNonNullUser(userId);
        return filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        checkNonNullFilm(filmId);
        checkNonNullUser(userId);
        return filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int limit) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(limit)
                .toList()
                .reversed();
    }

    private void checkNonNullFilm(Long filmId) {
        if (filmStorage.findFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с таким идентификатором не найден");
        }
    }

    private void checkNonNullUser(Long userId) {
        if (Objects.isNull(userStorage.findById(userId))) {
            throw new NotFoundException("Пользователь с таким идентификатором не найден");
        }
    }
}
