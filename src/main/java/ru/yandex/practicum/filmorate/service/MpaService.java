package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage storage;


    public Mpa getMpaById(Integer id) {
        return storage.findById(id).orElseThrow(() -> new NotFoundException("MPA с таким id не найден: " + id));
    }

    public List<Mpa> getAll() {
        return storage.getAll();
    }
}
