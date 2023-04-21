package ru.yandex.practicum.filmorate.util.validator.date.impl;

import ru.yandex.practicum.filmorate.util.validator.date.PastLocalDateConstrain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastLocalDateValidate implements ConstraintValidator<PastLocalDateConstrain, LocalDate> {
    private LocalDate currentDate;
    @Override
    public void initialize(PastLocalDateConstrain constraintAnnotation) {
        currentDate = LocalDate.parse(constraintAnnotation.current());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date != null && (date.isEqual(currentDate) || date.isAfter(currentDate));
    }
}
