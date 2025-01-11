package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> filmsStorage = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmsStorage.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начало создания сущности фильма");
        Validator.validate(film);
        film.setId(getNextId());
        film.setDuration(film.getDuration());
        filmsStorage.put(film.getId(), film);
        log.info("Созданная сущность фильма : {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Начало обновления сущности фильма.");
        if (film.getId() == null) {
            log.error("Идентификатор должен быть указан");
            throw new ValidationException("Идентификатор должен быть указан");
        }
        if (filmsStorage.containsKey(film.getId())) {
            Film filmToUpdate = filmsStorage.get(film.getId());
            Validator.validate(film);
            filmToUpdate.setName(film.getName());
            filmToUpdate.setDescription(film.getDescription());
            filmToUpdate.setDuration(film.getDuration());
            filmToUpdate.setReleaseDate(film.getReleaseDate());
            return filmsStorage.put(film.getId(), filmToUpdate);
        }

        throw new NotFoundException("Фильм не найден");
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
