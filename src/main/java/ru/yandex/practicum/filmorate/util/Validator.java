package ru.yandex.practicum.filmorate.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Set;

@Slf4j
public class Validator {
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(t);
        if (!validate.isEmpty()) {
            validate.stream().map(ConstraintViolation::getMessage).forEach(log::error);
            throw new ValidationException("Валидация не пройдена");
        }
    }
}
