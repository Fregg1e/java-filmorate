package ru.yandex.practicum.filmorate.util.validator.date;

import ru.yandex.practicum.filmorate.util.validator.date.impl.PastLocalDateValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastLocalDateValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PastLocalDateConstrain {
    String message() default "Неправильная дата.";
    String current();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
