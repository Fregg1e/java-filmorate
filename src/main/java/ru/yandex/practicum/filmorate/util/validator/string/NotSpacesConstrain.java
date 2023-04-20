package ru.yandex.practicum.filmorate.util.validator.string;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotSpacesValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSpacesConstrain {
    String message() default "Неправильная строка.";
}
