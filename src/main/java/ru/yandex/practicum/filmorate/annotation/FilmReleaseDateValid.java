package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.annotation.annotation_handlers.FilmReleaseDateValidationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidationHandler.class)
public @interface FilmReleaseDateValid {
    String message() default "{film.releaseDate.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
