package ru.yandex.practicum.filmorate.annotation.annotation_handlers;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDateValid;

import java.time.LocalDate;

public class FilmReleaseDateValidationHandler implements ConstraintValidator<FilmReleaseDateValid, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
        return localDate.isAfter(cinemaBirthday) || localDate.isEqual(cinemaBirthday);
    }
}
