package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с таким id не найден: " + id));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.findAll();
    }
}
