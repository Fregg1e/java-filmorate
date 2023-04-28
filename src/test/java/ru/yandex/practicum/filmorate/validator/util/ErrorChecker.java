package ru.yandex.practicum.filmorate.validator.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ErrorChecker {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean hasErrorMessage(T model, @NotNull String message) {
        Set<ConstraintViolation<T>> errors = VALIDATOR.validate(model);
        return errors.stream().map(ConstraintViolation::getMessage).anyMatch(message::equals);
    }
}
