package ru.yandex.practicum.filmorate.util.validator.date;

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
        return date.isEqual(currentDate) || date.isAfter(currentDate);
    }
}
