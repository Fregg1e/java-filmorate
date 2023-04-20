package ru.yandex.practicum.filmorate.util.validator.date;

import javax.validation.Constraint;
import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = LocalDateValidate.class)
public @interface FilmDateConstrain {
}
