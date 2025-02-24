package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    public Film create(Film film) {
        log.info("Начало создания сущности фильма");
        validateMpa(film.getMpa());
        validateGenres(film.getGenres());
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
            validateMpa(film.getMpa());
            validateGenres(film.getGenres());
            filmToUpdate.setName(film.getName());
            filmToUpdate.setDescription(film.getDescription());
            filmToUpdate.setDuration(film.getDuration());
            filmToUpdate.setMpa(film.getMpa());
            filmToUpdate.setReleaseDate(film.getReleaseDate());
            return filmStorage.update(filmToUpdate);
        }

        throw new NotFoundException("Фильм не найден");
    }

    public boolean addLike(Integer filmId, Integer userId) {
        checkNonNullFilm(filmId);
        checkNonNullUser(userId);
        return filmStorage.addLike(filmId, userId);
    }

    public boolean removeLike(Integer filmId, Integer userId) {
        checkNonNullFilm(filmId);
        checkNonNullUser(userId);
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int limit) {
        return filmStorage.getPopularFilms(limit);
    }

    private void checkNonNullFilm(Integer filmId) {
        if (filmStorage.findFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с таким идентификатором не найден");
        }
    }

    private void checkNonNullUser(Integer userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    private void validateMpa(Mpa mpa) {
        if (Objects.nonNull(mpa) && mpaStorage.findById(mpa.getId()).isEmpty()) {
            throw new NotFoundException("Такого рейтинга не существует");
        }

    }

    private void validateGenres(List<Genre> genres) {
        int existsGenresCount = 0;
        if (Objects.nonNull(genres) && !genres.isEmpty()) {
            for (Genre genre : genres) {
                if (genreStorage.findById(genre.getId()).isPresent()) {
                    ++existsGenresCount;
                }
            }

            if (existsGenresCount == 0) {
                throw new NotFoundException("Такого жанра не существует");
            }
        }
    }

}
