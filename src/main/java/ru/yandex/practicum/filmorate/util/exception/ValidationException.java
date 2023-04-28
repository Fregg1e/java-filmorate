package ru.yandex.practicum.filmorate.util.exception;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) {
        super(message);
    }
}
