package ru.yandex.practicum.filmorate.util.validator.string;

import ru.yandex.practicum.filmorate.util.validator.string.impl.NotSpacesValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotSpacesValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSpacesConstrain {
    String message() default "Неправильная строка.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
