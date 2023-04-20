package ru.yandex.practicum.filmorate.util.validator.string;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotSpacesValidate implements ConstraintValidator<NotSpacesConstrain, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.contains(" ");
    }
}
