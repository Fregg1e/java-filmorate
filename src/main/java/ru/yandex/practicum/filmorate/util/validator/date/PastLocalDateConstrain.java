package ru.yandex.practicum.filmorate.util.validator.date;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastLocalDateValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PastLocalDateConstrain {
    String message() default "Неправильная дата.";
    String current();
}
