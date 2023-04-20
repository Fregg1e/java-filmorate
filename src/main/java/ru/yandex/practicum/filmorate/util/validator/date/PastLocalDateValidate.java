package ru.yandex.practicum.filmorate.util.validator.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocalDateValidate implements ConstraintValidator<FilmDateConstrain, String> {
    @Override
    public void initialize(FilmDateConstrain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
